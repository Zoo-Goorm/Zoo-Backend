package zoo.insightnote.domain.payment.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.http.*;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import zoo.insightnote.domain.payment.dto.request.PaymentApproveRequestDto;
import zoo.insightnote.domain.payment.dto.request.PaymentRequestReadyDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import zoo.insightnote.domain.payment.dto.response.KakaoPayApproveResponseDto;
import zoo.insightnote.domain.payment.dto.response.KakaoPayReadyResponseDto;
import zoo.insightnote.global.exception.CustomException;
import zoo.insightnote.global.exception.ErrorCode;

import java.util.*;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
@Slf4j
public class KakaoPayService {

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;
    private final RedisTemplate<String, String> redisTemplate;
    private final long PAYMENT_TID_EXPIRATION = 10 * 60; // 10분
    private final long PAYMENT_SESSION_KEYS_EXPIRATION = 5 * 60; // 5분

    @Value("${kakao.api.cid}")
    private String cid;

    @Value("${kakao.api.admin-key}")
    private String adminKey;


    private void saveTidKey(Long orderId, String tid) {
        String tidKey = "payment:tid: " + orderId;
        redisTemplate.opsForValue().set(tidKey, tid, PAYMENT_TID_EXPIRATION, TimeUnit.SECONDS);
    }

    public String getTidKey(Long orderId) {
        String tidKey = "payment:tid: " + orderId;
        return redisTemplate.opsForValue().get(tidKey);
    }

    private void saveSessionIds(Long orderId, List<Long> sessionsId) {
        String sessionIdsKey = "payment:sessions: " + orderId;
        try {
            // ✅ JSON으로 변환하여 Redis에 저장
            String jsonSessionIds = objectMapper.writeValueAsString(sessionsId);
            redisTemplate.opsForValue().set(sessionIdsKey, jsonSessionIds, PAYMENT_SESSION_KEYS_EXPIRATION, TimeUnit.SECONDS);
        } catch (JsonProcessingException e) {
            log.error("❌ JSON 변환 오류 (sessionIds 저장 실패)", e);
            throw new CustomException(ErrorCode.JSON_PROCESSING_ERROR);
        }
    }

    public List<Long> getSessionIds(Long orderId) {
        String sessionIdsKey = "payment:sessions: " + orderId;
        String getSessionsIds = redisTemplate.opsForValue().get(sessionIdsKey);

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readValue(getSessionsIds, new TypeReference<List<Long>>() {});
        } catch (JsonProcessingException e) {
            log.error("❌ JSON 변환 오류 (sessionIds 조회 실패)", e);
            throw new CustomException(ErrorCode.JSON_PROCESSING_ERROR);
        }
    }

    // 결제 요청
    public ResponseEntity<KakaoPayReadyResponseDto> requestKakaoPayment(PaymentRequestReadyDto requestDto) {
        Long orderId = createOrderId();

        HttpEntity<String> paymentReqeustHttpEntity = createPaymentReqeustHttpEntity(requestDto, orderId);

        try {
            ResponseEntity<KakaoPayReadyResponseDto> response = restTemplate.exchange(
                    "https://open-api.kakaopay.com/online/v1/payment/ready",
                    HttpMethod.POST,
                    paymentReqeustHttpEntity,
                    KakaoPayReadyResponseDto.class
            );

            String tid = response.getBody().getTid();
            log.info("✅ 카카오페이 결제 요청 성공");

            saveTidKey(orderId, tid);
            saveSessionIds(orderId, requestDto.getSessionIds());

            return response;
        } catch (Exception e) {
            log.error("❌ 카카오페이 결제 요청 실패", e);
            throw new CustomException(ErrorCode.KAKAO_PAY_REQUEST_FAILED);
        }
    }

    // 결제 승인 요청
    @Transactional
    public KakaoPayApproveResponseDto approveKakaoPayment(String tid, PaymentApproveRequestDto requestDto) {
        HttpEntity<String> paymentApproveHttpEntity = createPaymentApproveHttpEntity(requestDto, tid);

        try {
            ResponseEntity<KakaoPayApproveResponseDto> response = restTemplate.exchange(
                    "https://open-api.kakaopay.com/online/v1/payment/approve",
                    HttpMethod.POST,
                    paymentApproveHttpEntity,
                    KakaoPayApproveResponseDto.class
            );

            log.info("✅ 카카오페이 결제 승인 성공");

            return response.getBody();
        } catch (Exception e) {
            log.error("❌ 카카오페이 결제 승인 실패", e);
            throw new CustomException(ErrorCode.KAKAO_PAY_APPROVE_FAILED);
        }
    }


    private HttpEntity<String> createPaymentReqeustHttpEntity(PaymentRequestReadyDto requestDto, Long orderId) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "SECRET_KEY " + adminKey);
        headers.set("Content-Type", "application/json");

        Map<String, Object> params = new HashMap<>();
        params.put("cid", cid);
        params.put("partner_order_id", orderId);
        params.put("partner_user_id", requestDto.getUserId());
        params.put("item_name", requestDto.getItemName());
        params.put("quantity", requestDto.getQuantity());
        params.put("total_amount", requestDto.getTotalAmount());
        params.put("tax_free_amount", 0);

        params.put("approval_url", "http://localhost:8080/api/v1/payment/approve?order_id=" + orderId + "&user_id=" + requestDto.getUserId());
        params.put("cancel_url", "http://localhost:8080/api/v1/payment/cancel");
        params.put("fail_url", "http://localhost:8080/api/v1/payment/fail");

        String jsonParams;
        try {
            jsonParams = objectMapper.writeValueAsString(params);
        } catch (JsonProcessingException e) {
            log.error("JSON 변환 실패", e);
            throw new CustomException(ErrorCode.JSON_PROCESSING_ERROR);
        }

        HttpEntity<String> requestEntity = new HttpEntity<>(jsonParams, headers);

        return requestEntity;
    }

    private HttpEntity<String> createPaymentApproveHttpEntity(PaymentApproveRequestDto requestDto, String tid) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "SECRET_KEY " + adminKey);
        headers.set("Content-Type", "application/json");

        Map<String, Object> params = new HashMap<>();
        params.put("cid", cid);
        params.put("tid", tid);
        params.put("partner_order_id", requestDto.getOrderId());
        params.put("partner_user_id", requestDto.getUserId());
        params.put("pg_token", requestDto.getPgToken());

        String jsonParams;
        try {
            jsonParams = objectMapper.writeValueAsString(params);
        } catch (JsonProcessingException e) {
            log.error("JSON 변환 실패", e);
            throw new RuntimeException("JSON 변환 오류 발생");
        }

        HttpEntity<String> requestEntity = new HttpEntity<>(jsonParams, headers);

        return requestEntity;
    }

    private Long createOrderId() {
        Long orderId = Math.abs(UUID.randomUUID().getMostSignificantBits());
        return orderId;
    }
}