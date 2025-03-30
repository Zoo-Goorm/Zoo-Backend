package zoo.insightnote.domain.payment.controller;

import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import zoo.insightnote.domain.email.service.EmailService;
import zoo.insightnote.domain.payment.dto.request.PaymentApproveRequest;
import zoo.insightnote.domain.payment.dto.request.PaymentReadyRequest;
import zoo.insightnote.domain.payment.dto.response.KakaoPayApproveResponse;
import zoo.insightnote.domain.payment.dto.response.KakaoPayReadyResponseDto;
import zoo.insightnote.domain.payment.mapper.PaymentApproveMapper;
import zoo.insightnote.domain.payment.service.PaymentService;
import zoo.insightnote.domain.user.entity.User;
import zoo.insightnote.domain.user.service.UserService;

@RestController
@RequestMapping("/api/v1/payment")
@RequiredArgsConstructor
public class PaymentControllerImpl implements PaymentController {
    private final PaymentService paymentService;
    private final UserService userService;
    private final EmailService emailService;

    // 주문 정보를 가지고 카카오페이 API에 결제 요청
    @PostMapping("/request")
    public ResponseEntity<KakaoPayReadyResponseDto> requestPayment(
            @RequestBody @Valid PaymentReadyRequest requestDto,
            @AuthenticationPrincipal UserDetails userDetails) {
        User user = userService.findByUsername(userDetails.getUsername());
        return paymentService.requestPayment(requestDto, user);
    }

    // 카카오페이 API에서 결제 요청 승인
    @GetMapping("/approve")
    public ResponseEntity<KakaoPayApproveResponse> approvePayment(
            @RequestParam(value = "order_id") Long orderId,
            @RequestParam(value = "user_id") Long userId,
            @RequestParam(value = "pg_token") String pgToken,
            @AuthenticationPrincipal UserDetails userDetails
    ) throws MessagingException {
        PaymentApproveRequest requestDto = PaymentApproveMapper.toBuildPaymentApprove(orderId, userId, pgToken, userDetails.getUsername());
        ResponseEntity<KakaoPayApproveResponse> response = paymentService.approvePayment(requestDto);
        User user = userService.findByUsername(userDetails.getUsername());
        emailService.sendPaymentSuccess(user);
        return response;
    }
}
