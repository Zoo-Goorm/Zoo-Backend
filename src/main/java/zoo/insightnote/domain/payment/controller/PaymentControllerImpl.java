package zoo.insightnote.domain.payment.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import zoo.insightnote.domain.payment.dto.request.PaymentApproveRequestDto;
import zoo.insightnote.domain.payment.dto.request.PaymentRequestReadyDto;
import zoo.insightnote.domain.payment.dto.response.KakaoPayApproveResponseDto;
import zoo.insightnote.domain.payment.dto.response.KakaoPayReadyResponseDto;
import zoo.insightnote.domain.payment.service.KakaoPayService;
import zoo.insightnote.domain.payment.service.PaymentService;
import zoo.insightnote.domain.user.entity.User;
import zoo.insightnote.domain.user.service.UserService;

@RestController
@RequestMapping("/api/v1/payment")
@RequiredArgsConstructor
public class PaymentControllerImpl implements PaymentController {
    private final KakaoPayService kakaoPayService;
    private final PaymentService paymentService;
    private final UserService userService;

    // 주문 정보를 가지고 카카오페이 API에 결제 요청
    @PostMapping("/request")
    public ResponseEntity<KakaoPayReadyResponseDto> requestPayment(
            @RequestBody @Valid PaymentRequestReadyDto requestDto,
            @AuthenticationPrincipal UserDetails userDetails) {
        User user = userService.findByUsername(userDetails.getUsername());
        return kakaoPayService.requestKakaoPayment(requestDto, user);
    }

    // 카카오페이 API에서 결제 요청 승인
    @GetMapping("/approve")
    public ResponseEntity<KakaoPayApproveResponseDto> approvePayment(
            @RequestParam(value = "order_id") Long orderId,
            @RequestParam(value = "user_id") Long userId,
            @RequestParam(value = "pg_token") String pgToken,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        PaymentApproveRequestDto requestDto = new PaymentApproveRequestDto(orderId, userId, pgToken,
                userDetails.getUsername());
        return paymentService.approvePayment(requestDto);
    }
}
