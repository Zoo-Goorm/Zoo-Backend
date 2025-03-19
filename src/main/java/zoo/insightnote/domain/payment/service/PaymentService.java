package zoo.insightnote.domain.payment.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import zoo.insightnote.domain.payment.dto.etc.UserInfoDto;
import zoo.insightnote.domain.payment.dto.request.PaymentApproveRequestDto;
import zoo.insightnote.domain.payment.dto.response.KakaoPayApproveResponseDto;
import zoo.insightnote.domain.payment.entity.Payment;
import zoo.insightnote.domain.payment.entity.PaymentStatus;
import zoo.insightnote.domain.payment.repository.PaymentRepository;
import zoo.insightnote.domain.reservation.entity.Reservation;
import zoo.insightnote.domain.reservation.repository.ReservationRepository;
import zoo.insightnote.domain.session.entity.Session;
import zoo.insightnote.domain.session.service.SessionService;
import zoo.insightnote.domain.user.entity.User;
import zoo.insightnote.domain.user.service.UserService;
import zoo.insightnote.global.exception.CustomException;
import zoo.insightnote.global.exception.ErrorCode;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentService {

    private final KakaoPayService kakaoPayService;
    private final UserService userService;
    private final SessionService sessionService;
    private final PaymentRepository paymentRepository;
    private final ReservationRepository reservationRepository;

    @Transactional
    public ResponseEntity<KakaoPayApproveResponseDto> approvePayment(PaymentApproveRequestDto requestDto) {
        String tid = kakaoPayService.getTidKey(requestDto.getOrderId());
        if (tid == null) {
            log.error("❌ Redis에서 tid 조회 실패! (orderId={})",  requestDto.getOrderId());
            throw new CustomException(ErrorCode.PAYMENT_NOT_FOUND);
        }

        List<Long> sessionIds = kakaoPayService.getSessionIds(requestDto.getOrderId());
        if (sessionIds == null) {
            log.error("❌ Redis에서 sessions 조회 실패! (orderId={})",  requestDto.getOrderId());
            throw new CustomException(ErrorCode.PAYMENT_NOT_FOUND);
        }

        UserInfoDto userInfo = kakaoPayService.getUserInfo(requestDto.getOrderId());
        if (userInfo == null) {
            log.error("❌ Redis에서 sessions 조회 실패! (orderId={})",  requestDto.getOrderId());
            throw new CustomException(ErrorCode.PAYMENT_NOT_FOUND);
        }

        KakaoPayApproveResponseDto response = kakaoPayService.approveKakaoPayment(tid, requestDto);

        User user = userService.findByUsername(requestDto.getUsername());
        savePaymentInfo(response, sessionIds.get(0), user);
        saveReservationsInfo(sessionIds, user);
        updateUserInfo(userInfo);

        return ResponseEntity.ok(response);

    }

    private void savePaymentInfo(KakaoPayApproveResponseDto responseDto, Long sessionId, User user) {
        Session sessionInfo = sessionService.findSessionBySessionId(sessionId);

        Payment payment = Payment.builder()
                .user(user)
                .event(sessionInfo.getEvent())
                .email(user.getEmail())
                .phoneNumber(user.getPhoneNumber())
                .amount(responseDto.getAmount().getTotalAmount())
                .paymentStatus(PaymentStatus.COMPLETED)
                .build();

        paymentRepository.save(payment);
    }

    private void saveReservationsInfo(List<Long> sessionIds, User user) {
        for (Long sessionId : sessionIds) {
            Session sessionInfo = sessionService.findSessionBySessionId(sessionId);

            Reservation savedReservation = Reservation.create(
                    user,
                    sessionInfo,
                    false
            );

            reservationRepository.save(savedReservation);
        }
    }

    private void updateUserInfo(UserInfoDto userInfo) {
        User user = userService.findUserByEmail(userInfo.getEmail());
        user.update(
                userInfo.getName(),
                userInfo.getPhoneNumber(),
                userInfo.getJob(),              // 직업
                userInfo.getOccupation(),       // 직군
                userInfo.getInterestCategory(),
                userInfo.isOnline()
        );
    }
}
