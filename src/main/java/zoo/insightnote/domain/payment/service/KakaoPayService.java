package zoo.insightnote.domain.payment.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.http.*;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import zoo.insightnote.domain.payment.dto.request.PaymentApproveRequestDto;
import zoo.insightnote.domain.payment.dto.request.PaymentCancelRequestDto;
import zoo.insightnote.domain.payment.dto.request.PaymentRequestReadyDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import zoo.insightnote.domain.payment.dto.response.KakaoPayApproveResponseDto;
import zoo.insightnote.domain.payment.dto.response.KakaoPayCancelResponseDto;
import zoo.insightnote.domain.payment.dto.response.KakaoPayReadyResponseDto;
import zoo.insightnote.domain.user.entity.User;
import zoo.insightnote.global.exception.CustomException;
import zoo.insightnote.global.exception.ErrorCode;

import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class KakaoPayService {
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;
    private final PaymentRedisService paymentRedisService;

    @Value("${kakao.api.cid}")
    private String cid;

    @Value("${kakao.api.admin-key}")
    private String adminKey;

    // 결제 요청
    public ResponseEntity<KakaoPayReadyResponseDto> requestKakaoPayment(PaymentRequestReadyDto requestDto, User user, Long orderId) {
        HttpEntity<String> paymentReqeustHttpEntity = createPaymentReqeustHttpEntity(requestDto, user, orderId);

        try {
            ResponseEntity<KakaoPayReadyResponseDto> response = restTemplate.exchange(
                    "https://open-api.kakaopay.com/online/v1/payment/ready",
                    HttpMethod.POST,
                    paymentReqeustHttpEntity,
                    KakaoPayReadyResponseDto.class
            );

            String tid = response.getBody().getTid();
            log.info("✅ 카카오페이 결제 요청 성공");

            paymentRedisService.saveTidKey(orderId, tid);
            paymentRedisService.saveSessionIds(orderId, requestDto.getSessionIds());
            paymentRedisService.saveUserInfo(orderId, requestDto.getUserInfo());

            return response;
        } catch (Exception e) {
            log.error("❌ 카카오페이 결제 요청 실패", e);
            throw new CustomException(ErrorCode.KAKAO_PAY_REQUEST_FAILED);
        }
    }

    // 결제 승인 요청
    @Transactional
    public KakaoPayApproveResponseDto approveKakaoPayment(String tid, PaymentApproveRequestDto requestDto, User user) {
        HttpEntity<String> paymentApproveHttpEntity = createPaymentApproveHttpEntity(requestDto, user, tid);

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

    public KakaoPayCancelResponseDto cancelKakaoPayment(PaymentCancelRequestDto requestDto) {
        HttpEntity<String> paymentCancelHttpEntity = createPaymentCancelHttpEntity(requestDto, requestDto.getTid());

        try {
            ResponseEntity<KakaoPayCancelResponseDto> response = restTemplate.exchange(
                    "https://open-api.kakaopay.com/online/v1/payment/cancel",
                    HttpMethod.POST,
                    paymentCancelHttpEntity,
                    KakaoPayCancelResponseDto.class
            );

            log.info("✅ 카카오페이 결제 취소 성공");

            return response.getBody();
        } catch (Exception e) {
            log.error("❌ 카카오페이 결제 취소 실패", e);
            throw new CustomException(ErrorCode.KAKAO_PAY_CANCEL_FAILED);
        }
    }

    private HttpEntity<String> createKakaoHttpEntity(Map<String, Object> params) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "SECRET_KEY " + adminKey);
        headers.set("Content-Type", "application/json");

        try {
            String jsonParams = objectMapper.writeValueAsString(params);
            return new HttpEntity<>(jsonParams, headers);
        } catch (JsonProcessingException e) {
            log.error("❌ JSON 변환 실패", e);
            throw new CustomException(ErrorCode.JSON_PROCESSING_ERROR);
        }
    }

    private HttpEntity<String> createPaymentReqeustHttpEntity(PaymentRequestReadyDto requestDto, User user, Long orderId) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "SECRET_KEY " + adminKey);
        headers.set("Content-Type", "application/json");

        Map<String, Object> params = new HashMap<>();
        params.put("cid", cid);
        params.put("partner_order_id", orderId);
        params.put("partner_user_id", user.getId());
        params.put("item_name", requestDto.getItemName());
        params.put("quantity", requestDto.getQuantity());
        params.put("total_amount", requestDto.getTotalAmount());
        params.put("tax_free_amount", 0);

        params.put("approval_url", "http://localhost:8080/api/v1/payment/approve?order_id=" + orderId + "&user_id=" + user.getId());
        params.put("cancel_url", "http://localhost:8080/api/v1/payment/cancel");
        params.put("fail_url", "http://localhost:8080/api/v1/payment/fail");

        return createKakaoHttpEntity(params);
    }

    private HttpEntity<String> createPaymentApproveHttpEntity(PaymentApproveRequestDto requestDto, User user, String tid) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "SECRET_KEY " + adminKey);
        headers.set("Content-Type", "application/json");

        Map<String, Object> params = new HashMap<>();
        params.put("cid", cid);
        params.put("tid", tid);
        params.put("partner_order_id", requestDto.getOrderId());
        params.put("partner_user_id", user.getId());
        params.put("pg_token", requestDto.getPgToken());

        return createKakaoHttpEntity(params);
    }

    private HttpEntity<String> createPaymentCancelHttpEntity(PaymentCancelRequestDto requestDto, String tid) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "SECRET_KEY " + adminKey);
        headers.set("Content-Type", "application/json");

        Map<String, Object> params = new HashMap<>();
        params.put("cid", cid);
        params.put("tid", tid);
        params.put("cancel_amount", requestDto.getCancelAmount());
        params.put("cancel_tax_free_amount", requestDto.getCancelTaxFreeAmount());

        return createKakaoHttpEntity(params);
    }
}