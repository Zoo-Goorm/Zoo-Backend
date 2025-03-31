package zoo.insightnote.domain.payment.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.http.*;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import zoo.insightnote.domain.payment.dto.request.PaymentApproveRequest;
import zoo.insightnote.domain.payment.dto.request.PaymentCancelRequest;
import zoo.insightnote.domain.payment.dto.request.PaymentReadyRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import zoo.insightnote.domain.payment.dto.response.KakaoPayApproveResponse;
import zoo.insightnote.domain.payment.dto.response.KakaoPayCancelResponse;
import zoo.insightnote.domain.payment.dto.response.KakaoPayReadyResponse;
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
    public ResponseEntity<KakaoPayReadyResponse> requestKakaoPayment(PaymentReadyRequest requestDto, User user, Long orderId) {
        HttpEntity<String> paymentReqeustHttpEntity = createPaymentReqeustHttpEntity(requestDto, user, orderId);

        try {
            ResponseEntity<KakaoPayReadyResponse> response = restTemplate.exchange(
                    "https://open-api.kakaopay.com/online/v1/payment/ready",
                    HttpMethod.POST,
                    paymentReqeustHttpEntity,
                    KakaoPayReadyResponse.class
            );

            String tid = response.getBody().tid();
            log.info("✅ 카카오페이 결제 요청 성공");

            paymentRedisService.saveTidKey(orderId, tid);
            paymentRedisService.saveSessionIds(orderId, requestDto.sessionIds());
            paymentRedisService.saveUserInfo(orderId, requestDto.userInfo());

            return response;
        } catch (Exception e) {
            log.error("❌ 카카오페이 결제 요청 실패", e);
            throw new CustomException(ErrorCode.KAKAO_PAY_REQUEST_FAILED);
        }
    }

    // 결제 승인 요청
    @Transactional
    public KakaoPayApproveResponse approveKakaoPayment(String tid, PaymentApproveRequest requestDto, User user) {
        HttpEntity<String> paymentApproveHttpEntity = createPaymentApproveHttpEntity(requestDto, user, tid);

        try {
            ResponseEntity<KakaoPayApproveResponse> response = restTemplate.exchange(
                    "https://open-api.kakaopay.com/online/v1/payment/approve",
                    HttpMethod.POST,
                    paymentApproveHttpEntity,
                    KakaoPayApproveResponse.class
            );

            log.info("✅ 카카오페이 결제 승인 성공");

            return response.getBody();
        } catch (Exception e) {
            log.error("❌ 카카오페이 결제 승인 실패", e);
            throw new CustomException(ErrorCode.KAKAO_PAY_APPROVE_FAILED);
        }
    }

    public KakaoPayCancelResponse cancelKakaoPayment(PaymentCancelRequest requestDto) {
        HttpEntity<String> paymentCancelHttpEntity = createPaymentCancelHttpEntity(requestDto, requestDto.tid());

        try {
            ResponseEntity<KakaoPayCancelResponse> response = restTemplate.exchange(
                    "https://open-api.kakaopay.com/online/v1/payment/cancel",
                    HttpMethod.POST,
                    paymentCancelHttpEntity,
                    KakaoPayCancelResponse.class
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

    private HttpEntity<String> createPaymentReqeustHttpEntity(PaymentReadyRequest requestDto, User user, Long orderId) {
        Map<String, Object> params = new HashMap<>();
        params.put("cid", cid);
        params.put("partner_order_id", orderId);
        params.put("partner_user_id", user.getId());
        params.put("item_name", requestDto.itemName());
        params.put("quantity", requestDto.quantity());
        params.put("total_amount", requestDto.totalAmount());
        params.put("tax_free_amount", 0);

        params.put("approval_url", "http://localhost:8080/api/v1/payment/approve?order_id=" + orderId + "&user_id=" + user.getId());
        params.put("cancel_url", "http://localhost:8080/api/v1/payment/cancel");
        params.put("fail_url", "http://localhost:8080/api/v1/payment/fail");

        return createKakaoHttpEntity(params);
    }

    private HttpEntity<String> createPaymentApproveHttpEntity(PaymentApproveRequest requestDto, User user, String tid) {
        Map<String, Object> params = new HashMap<>();
        params.put("cid", cid);
        params.put("tid", tid);
        params.put("partner_order_id", requestDto.orderId());
        params.put("partner_user_id", user.getId());
        params.put("pg_token", requestDto.pgToken());

        return createKakaoHttpEntity(params);
    }

    private HttpEntity<String> createPaymentCancelHttpEntity(PaymentCancelRequest requestDto, String tid) {
        Map<String, Object> params = new HashMap<>();
        params.put("cid", cid);
        params.put("tid", tid);
        params.put("cancel_amount", requestDto.cancelAmount());
        params.put("cancel_tax_free_amount", requestDto.cancelTaxFreeAmount());

        return createKakaoHttpEntity(params);
    }
}