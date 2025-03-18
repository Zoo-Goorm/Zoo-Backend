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
import zoo.insightnote.domain.session.repository.SessionRepository;
import zoo.insightnote.domain.user.entity.User;
import zoo.insightnote.domain.user.repository.UserRepository;
import zoo.insightnote.global.exception.CustomException;
import zoo.insightnote.global.exception.ErrorCode;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentService {

    private final KakaoPayService kakaoPayService;
    private final PaymentRepository paymentRepository;
    private final UserRepository userRepository;
    private final SessionRepository sessionRepository;
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
        User user = findUserById(Long.valueOf(response.getPartner_user_id()));
        saveSessionsInfo(user, sessionIds);
        savePaymentInfo(response, sessionIds.get(0));
        updateUserInfo(userInfo);

        return ResponseEntity.ok(response);

    }

    private void savePaymentInfo(KakaoPayApproveResponseDto responseDto, Long sessionId) {
        User user = findUserById(Long.valueOf(responseDto.getPartner_user_id()));
        Session sessionInfo = findSessionById(sessionId);

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

    private void saveSessionsInfo(User user, List<Long> sessionIds) {
        for (Long sessionId : sessionIds) {
            Session sessionInfo = findSessionById(sessionId);

            Reservation sessionReservation = Reservation.builder()
                    .user(user)
                    .session(sessionInfo)
                    .checked(false)
                    .build();

            reservationRepository.save(sessionReservation);
        }
    }

    private void updateUserInfo(UserInfoDto userInfo) {
        User user = findUserByEmail(userInfo.getEmail());
        user.update(
                userInfo.getEmail(),
                userInfo.getName(),
                userInfo.getPhoneNumber(),
                userInfo.getJob(),
                userInfo.getInterestCategory()
        );
    }

    // TODO : 유저 도메인 개발 완료시 삭제
    private User findUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(null, "User 사용자를 찾을 수 없습니다."));
    }

    private User findUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException(null, "Email 사용자를 찾을 수 없습니다."));
    }

    // TODO : 유저 도메인 개발 완료시 삭제
    private Session findSessionById(Long eventId) {
        return sessionRepository.findById(eventId)
                .orElseThrow(() -> new CustomException(null, "event 사용자를 찾을 수 없습니다."));
    }
}
