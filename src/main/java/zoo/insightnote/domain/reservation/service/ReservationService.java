package zoo.insightnote.domain.reservation.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import zoo.insightnote.domain.reservation.dto.response.UserTicketInfoResponseDto;
import zoo.insightnote.domain.reservation.entity.Reservation;
import zoo.insightnote.domain.reservation.repository.ReservationCustomQueryRepository;
import zoo.insightnote.domain.reservation.repository.ReservationRepository;
import zoo.insightnote.domain.session.entity.Session;
import zoo.insightnote.domain.session.repository.SessionRepository;
import zoo.insightnote.domain.session.service.SessionService;
import zoo.insightnote.domain.user.entity.User;
import zoo.insightnote.domain.user.service.UserService;
import zoo.insightnote.global.exception.CustomException;
import zoo.insightnote.global.exception.ErrorCode;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReservationService {
    private final ReservationCustomQueryRepository reservationQueryRepository;
    private final SessionRepository sessionRepository;
    private final ReservationRepository reservationRepository;
    private final UserService userService;
    private final SessionService sessionService;

    public UserTicketInfoResponseDto getUserTicketInfo(String username) {
        UserTicketInfoResponseDto userTicketInfo = reservationQueryRepository.processUserTicketInfo(username);
        return userTicketInfo;
    }

    @Transactional
    public void cancelAndAddSession(Long cancelSessionId, Long addSessionId, String username) {
        cancelSession(cancelSessionId, username);
        addSession(addSessionId, username);
    }

    public void addSession(Long sessionId, String username) {
        // 유저가 예약한 세션의 리스트
        List<Long> sessionIds = reservationRepository.findSessionIdsByUsername(username);

        // 이미 신청한 세션인지 확인
        if (sessionIds.contains(sessionId)) {
            throw new CustomException(ErrorCode.ALREADY_RESERVED_SESSION);
        }

        List<Object[]> reservedSessionTimes = sessionRepository.findReservedSessionTimes(sessionIds);
        Object[] targetSessionTime = sessionRepository.findTargetSessionTime(sessionId).get(0);

        LocalDateTime startTime = (LocalDateTime) targetSessionTime[0]; // 신청 세션 시작 시간
        LocalDateTime endTime = (LocalDateTime) targetSessionTime[1];   // 신청 세션 종료 시간

        // 신청하려는 세션의 시간대와 기존 예약된 세션의 시간대들이 겹치는지 확인
        for (Object[] time : reservedSessionTimes) {
            LocalDateTime reservedStart = (LocalDateTime) time[0];      // 이미 신청되어 있는 세션 시작 시간
            LocalDateTime reservedEnd = (LocalDateTime) time[1];        // 이미 신청되어 있는 세션 종료 시간

            boolean isOverlap = startTime.isBefore(reservedEnd) && reservedStart.isBefore(endTime);
            if (isOverlap) {
                throw new CustomException(ErrorCode.DUPLICATE_SESSION_TIME);
            }
        }

        User user = userService.findByUsername(username);
        Session session = sessionService.findSessionBySessionId(sessionId);

        Reservation savedReservation = Reservation.create(user, session, false);
        reservationRepository.save(savedReservation);
    }

    public void cancelSession(Long sessionId, String username) {
        Reservation reservedSession = reservationRepository.findReservedSession(username, sessionId)
                .orElseThrow(() -> new CustomException(ErrorCode.SESSION_NOT_FOUND));
        reservationRepository.delete(reservedSession);
    }

    @Transactional
    public void cancelAndAddSession(Long cancelSessionId, Long addSessionId, String username) {
        cancelSession(cancelSessionId, username);
        addSession(addSessionId, username);
    }
    public void saveReservationsInfo(List<Long> sessionIds, User user) {
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


}
