package zoo.insightnote.domain.payment.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import zoo.insightnote.domain.payment.dto.etc.UserInfoDto;
import zoo.insightnote.domain.payment.dto.request.PaymentApproveRequestDto;
import zoo.insightnote.domain.payment.dto.request.PaymentCancelRequestDto;
import zoo.insightnote.domain.payment.dto.request.PaymentRequestReadyDto;
import zoo.insightnote.domain.payment.dto.response.KakaoPayApproveResponseDto;
import zoo.insightnote.domain.payment.dto.response.KakaoPayReadyResponseDto;
import zoo.insightnote.domain.payment.entity.Payment;
import zoo.insightnote.domain.payment.repository.PaymentRepository;
import zoo.insightnote.domain.reservation.service.ReservationService;
import zoo.insightnote.domain.session.entity.Session;
import zoo.insightnote.domain.session.service.SessionService;
import zoo.insightnote.domain.user.entity.User;
import zoo.insightnote.domain.user.service.UserService;
import zoo.insightnote.global.exception.CustomException;
import zoo.insightnote.global.exception.ErrorCode;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentService {

    private final KakaoPayService kakaoPayService;
    private final UserService userService;
    private final SessionService sessionService;
    private final PaymentRedisService paymentRedisService;
    private final ReservationService reservationService;
    private final PaymentRepository paymentRepository;

    public ResponseEntity<KakaoPayReadyResponseDto> requestPayment(PaymentRequestReadyDto request, User user) {
        Long orderId = createOrderId();
        sessionService.validateSessionTime(request.getSessionIds());
        reservationService.validateReservedSession(user, request.getSessionIds());
        return kakaoPayService.requestKakaoPayment(request, user, orderId);
    }

    @Transactional
    public ResponseEntity<KakaoPayApproveResponseDto> approvePayment(PaymentApproveRequestDto requestDto) {
        String tid = paymentRedisService.getTidKey(requestDto.getOrderId());
        List<Long> sessionIds = paymentRedisService.getSessionIds(requestDto.getOrderId());
        UserInfoDto userInfo = paymentRedisService.getUserInfo(requestDto.getOrderId());
        User user = userService.findByUsername(requestDto.getUsername());

        KakaoPayApproveResponseDto response = kakaoPayService.approveKakaoPayment(tid, requestDto, user);
        try {
            savePaymentInfo(response, sessionIds.get(0), user, tid, userInfo.isOnline());
            reservationService.saveReservationsInfo(sessionIds, user);
            userService.updateUserInfo(userInfo, user);
        } catch (Exception e) {
            log.error("❌ 결제 후 내부 로직 실패 → 카카오페이 결제 취소");

            PaymentCancelRequestDto cancelRequestDto = PaymentCancelRequestDto.builder()
                    .tid(tid)
                    .cancelAmount(response.getAmount().getTotal())
                    .cancelTaxFreeAmount(response.getAmount().getTax_free())
                    .build();

            kakaoPayService.cancelKakaoPayment(cancelRequestDto);
            throw new CustomException(ErrorCode.KAKAO_PAY_APPROVE_FAILED);
        }
        return ResponseEntity.ok(response);
    }

    private Payment savePaymentInfo(KakaoPayApproveResponseDto responseDto, Long sessionId, User user, String tid, Boolean isOnline) {
        Session sessionInfo = sessionService.findSessionBySessionId(sessionId);

        Payment payment = Payment.create(
                user,
                sessionInfo,
                tid,
                responseDto.getAmount().getTotal(),
                isOnline
        );

        return paymentRepository.save(payment);
    }

    private Long createOrderId() {
        Long orderId = Math.abs(UUID.randomUUID().getMostSignificantBits());
        return orderId;
    }
}
