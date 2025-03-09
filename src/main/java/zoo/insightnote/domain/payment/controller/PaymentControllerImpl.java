package zoo.insightnote.domain.payment.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import zoo.insightnote.domain.payment.dto.request.PaymentApproveRequestDto;
import zoo.insightnote.domain.payment.dto.request.PaymentRequestReadyDto;
import zoo.insightnote.domain.payment.dto.response.KakaoPayApproveResponseDto;
import zoo.insightnote.domain.payment.dto.response.KakaoPayReadyResponseDto;
import zoo.insightnote.domain.payment.service.KakaoPayService;
import zoo.insightnote.domain.payment.service.PaymentService;

@RestController
@RequestMapping("/api/v1/payment")
@RequiredArgsConstructor
public class PaymentControllerImpl implements PaymentController{
    private final KakaoPayService kakaoPayService;
    private final PaymentService paymentService;

    // 주문 정보를 가지고 카카오페이 API에 결제 요청
    @PostMapping("/request")
    public ResponseEntity<KakaoPayReadyResponseDto> requestPayment(@RequestBody @Valid PaymentRequestReadyDto requestDto) {
        ResponseEntity<KakaoPayReadyResponseDto> response = kakaoPayService.requestKakaoPayment(requestDto);
        return response;
    }

    // 카카오페이 API에서 결제 요청 승인
    @GetMapping("/approve")
    public ResponseEntity<KakaoPayApproveResponseDto> approvePayment(
            @RequestParam(value = "order_id") Long orderId,
            @RequestParam(value = "user_id") Long userId,
            @RequestParam(value = "pg_token") String pgToken
    ) {
        PaymentApproveRequestDto requestDto = new PaymentApproveRequestDto(orderId, userId, pgToken);
        return paymentService.approvePayment(requestDto);
    }
}
