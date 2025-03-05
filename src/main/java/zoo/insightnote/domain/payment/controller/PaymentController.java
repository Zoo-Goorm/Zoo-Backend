package zoo.insightnote.domain.payment.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import zoo.insightnote.domain.payment.dto.request.PaymentRequestReadyDto;
import zoo.insightnote.domain.payment.dto.response.KakaoPayApproveResponseDto;
import zoo.insightnote.domain.payment.dto.response.KakaoPayReadyResponseDto;

public interface PaymentController {
    @PostMapping("/request")
    ResponseEntity<KakaoPayReadyResponseDto> requestPayment(@RequestBody PaymentRequestReadyDto request);

    @GetMapping("/approve")
    ResponseEntity<KakaoPayApproveResponseDto> approvePayment(@RequestParam Long orderId, @RequestParam Long userId, @RequestParam String pgToken);
}
