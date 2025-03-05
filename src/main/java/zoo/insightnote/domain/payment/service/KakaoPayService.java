package zoo.insightnote.domain.payment.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;
import zoo.insightnote.domain.payment.dto.request.PaymentRequestDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import java.util.Map;
import java.util.HashMap;

@Service
@RequiredArgsConstructor
@Slf4j
public class KakaoPayService {

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    @Value("${kakao.api.cid}")
    private String cid;

    @Value("${kakao.api.admin-key}")
    private String adminKey;

    public void requestPayment(PaymentRequestDto requestDto) {
    public ResponseEntity<KakaoPayReadyResponseDto> requestPayment(PaymentRequestDto requestDto) {
        HttpEntity<String> paymentHttpEntity = createPaymentHttpEntity(requestDto);

        try {
            ResponseEntity<KakaoPayReadyResponseDto> response = restTemplate.exchange(
                    "https://open-api.kakaopay.com/online/v1/payment/ready",
                    HttpMethod.POST,
                    paymentHttpEntity,
                    KakaoPayReadyResponseDto.class
            );
            log.info("카카오페이 결제 요청 성공: {}", response.getBody().getTid());
            saveTidKey(requestDto.getOrderId(), response.getBody().getTid());

            return response;
        } catch (Exception e) {
            log.error("카카오페이 결제 요청 실패", e);
            throw new RuntimeException("카카오페이 결제 요청 중 오류 발생");
        }
    }

    private HttpEntity<String> createPaymentHttpEntity(PaymentRequestDto requestDto) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "SECRET_KEY " + adminKey);
        headers.set("Content-Type", "application/json");

        Map<String, Object> params = new HashMap<>();
        params.put("cid", cid);
        params.put("partner_order_id", requestDto.getOrderId());
        params.put("partner_user_id", requestDto.getUserId());
        params.put("item_name", requestDto.getItemName());
        params.put("quantity", requestDto.getQuantity());
        params.put("total_amount", requestDto.getTotalAmount());
        params.put("tax_free_amount", 0);

        params.put("approval_url", "https://localhost:8080/api/v1/payment/approve");
        params.put("cancel_url", "https://localhost:8080/api/v1/payment/cancel");
        params.put("fail_url", "https://localhost:8080/api/v1/payment/fail");

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
}

