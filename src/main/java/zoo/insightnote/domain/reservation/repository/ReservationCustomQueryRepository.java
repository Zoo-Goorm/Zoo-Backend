package zoo.insightnote.domain.reservation.repository;

import com.querydsl.core.Tuple;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import zoo.insightnote.domain.event.entity.Event;
import zoo.insightnote.domain.event.entity.QEvent;
import zoo.insightnote.domain.reservation.dto.response.ReservationSessions;
import zoo.insightnote.domain.reservation.dto.response.UserTicketInfoResponse;
import zoo.insightnote.domain.reservation.entity.QReservation;
import zoo.insightnote.domain.reservation.mapper.UserTicketInfoMapper;
import zoo.insightnote.domain.session.entity.QSession;
import zoo.insightnote.domain.speaker.entity.QSpeaker;

import java.time.format.DateTimeFormatter;
import java.util.*;

@Repository
@RequiredArgsConstructor
@Slf4j
public class ReservationCustomQueryRepository {
    private final JPAQueryFactory queryFactory;

    public List<Tuple> findUserReservationInfo(String username) {
        QReservation reservation = QReservation.reservation;
        QSession session = QSession.session;
        QSpeaker speaker = QSpeaker.speaker;

        return queryFactory
                .select(
                        session.id,
                        session.event,
                        session.startTime,
                        session.endTime,
                        session.eventDay,
                        session.name,
                        speaker.name
                )
                .from(reservation)
                .join(reservation.session, session)
                .join(session.speaker, speaker)
                .where(reservation.user.username.eq(username))
                .fetch();
    }

    public List<Tuple> findEventInfo() {
        QEvent event = QEvent.event;

        return queryFactory
                .select(
                        Expressions.stringTemplate("DATE_FORMAT({0}, '%c월 %e일')", event.startTime),
                        Expressions.stringTemplate("DATE_FORMAT({0}, '%c월 %e일')", event.endTime)
                )
                .from(event)
                .fetch();
    }

    public UserTicketInfoResponse processUserTicketInfo(String username) {
        QSession session = QSession.session;
        QSpeaker speaker = QSpeaker.speaker;

        // 유저가 신청한 세션 정보 조회
        List<Tuple> reservationSessions = findUserReservationInfo(username);
        List<Tuple> eventSessions = findEventInfo();

        // 이벤트 아이디 조회
        Event event = reservationSessions.get(0).get(session.event);

        // 날짜별 세션 정보 저장
        Map<String, List<ReservationSessions>> registeredSessions = new LinkedHashMap<>();
        Set<String> eventDates = new LinkedHashSet<>();
        Set<String> userReservedDates = new HashSet<>();

        for (Tuple row : reservationSessions) {
            Long sessionId = row.get(session.id);
            String sessionName = row.get(session.name);
            String speakerName = row.get(speaker.name);
            String eventDay = row.get(session.eventDay).format(DateTimeFormatter.ofPattern("M월 d일"));
            String timeRange = row.get(session.startTime).format(DateTimeFormatter.ofPattern("HH:mm"))
                    + "~" + row.get(session.endTime).format(DateTimeFormatter.ofPattern("HH:mm"));

            // 날짜별 세션 등록
            ReservationSessions dto = UserTicketInfoMapper.toBuildReservationSessions(sessionId, sessionName, speakerName, timeRange);
            registeredSessions.computeIfAbsent(eventDay, k -> new ArrayList<>()).add(dto);

            // 유저가 등록한 날짜 저장
            userReservedDates.add(eventDay);
        }

        for (Tuple row : eventSessions) {
            eventDates.add(row.get(0, String.class)); // 이벤트 시작 날짜
            eventDates.add(row.get(1, String.class)); // 이벤트 종료 날짜
        }

        // 티켓 여부 설정
        Map<String, Boolean> tickets = new LinkedHashMap<>();
        for (String date : eventDates) {
            tickets.put(date, userReservedDates.contains(date));
        }

        // DTO로 반환
        return UserTicketInfoMapper.toBuildUserTicketInfoResponse(
                event.getId(),
                tickets,
                registeredSessions
        );
    }
}