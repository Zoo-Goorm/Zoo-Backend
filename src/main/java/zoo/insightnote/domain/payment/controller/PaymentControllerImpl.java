package zoo.insightnote.domain.payment.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import zoo.insightnote.domain.payment.dto.request.PaymentApproveRequestDto;
import zoo.insightnote.domain.payment.dto.request.PaymentRequestDto;
import zoo.insightnote.domain.payment.service.KakaoPayService;

@RestController
@RequestMapping("/api/v1/payment")
@RequiredArgsConstructor
public class PaymentControllerImpl implements PaymentController{
    private final KakaoPayService kakaoPayService;

    // 주문 정보를 가지고 카카오페이 API에 결제 요청
    @PostMapping("/request")
    public void requestPayment(@RequestBody PaymentRequestDto requestDto) {
        kakaoPayService.requestPayment(requestDto);
    }
}
