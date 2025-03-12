package zoo.insightnote.domain.payment.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
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
    private final RedisService redisService;
    private final UserRepository userRepository;
    private final SessionRepository sessionRepository;
    private final ReservationRepository reservationRepository;

    @Transactional
    public ResponseEntity<KakaoPayApproveResponseDto> approvePayment(PaymentApproveRequestDto requestDto) {
        String tid = redisService.getTidKey(requestDto.getOrderId());
        String sessionIds = redisService.getSessionIds(requestDto.getOrderId());

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            List<Long> sessionIdList = objectMapper.readValue(sessionIds, new TypeReference<List<Long>>() {});


            KakaoPayApproveResponseDto response = kakaoPayService.approveKakaoPayment(tid, requestDto);
            saveSessionsInfo(response, sessionIdList);
            savePaymentInfo(response, sessionIdList.get(0));

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("❌ JSON 변환 오류 (sessionIds 파싱 실패)", e);
            throw new CustomException(ErrorCode.JSON_PROCESSING_ERROR);
        }
    }

    private void savePaymentInfo(KakaoPayApproveResponseDto responseDto, Long sessionId) {
        User userInfo = findUserById(Long.valueOf(responseDto.getPartner_user_id()));
        Session sessionInfo = findSessionById(sessionId);

        Payment payment = Payment.builder()
                .user(userInfo)
                .event(sessionInfo.getEvent())
                .email(userInfo.getEmail())
                .phoneNumber(userInfo.getPhoneNumber())
                .amount(responseDto.getAmount().getTotalAmount())
                .paymentStatus(PaymentStatus.COMPLETED)
                .build();

        paymentRepository.save(payment);
    }

    private void saveSessionsInfo(KakaoPayApproveResponseDto response, List<Long> sessionIds) {
        User userInfo = findUserById(Long.valueOf(response.getPartner_user_id()));

        for (Long sessionId : sessionIds) {
            Session sessionInfo = findSessionById(sessionId);

            Reservation sessionReservation = Reservation.builder()
                    .user(userInfo)
                    .session(sessionInfo)
                    .checked(false)
                    .build();

            reservationRepository.save(sessionReservation);
        }
    }

    // TODO : 유저 도메인 개발 완료시 삭제
    private User findUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(null, "User 사용자를 찾을 수 없습니다."));
    }

    // TODO : 유저 도메인 개발 완료시 삭제
    private Session findSessionById(Long eventId) {
        return sessionRepository.findById(eventId)
                .orElseThrow(() -> new CustomException(null, "event 사용자를 찾을 수 없습니다."));
    }
}
