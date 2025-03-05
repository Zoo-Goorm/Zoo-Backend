package zoo.insightnote.domain.payment.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import zoo.insightnote.domain.payment.dto.request.PaymentRequestDto;
import zoo.insightnote.domain.payment.dto.response.KakaoPayReadyResponseDto;

public interface PaymentController {
    @PostMapping("/request")
    ResponseEntity<KakaoPayReadyResponseDto> requestPayment(@RequestBody PaymentRequestDto request);
}
