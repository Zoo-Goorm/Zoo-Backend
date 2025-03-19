package zoo.insightnote.domain.reservation.repository;

import com.querydsl.core.Tuple;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import zoo.insightnote.domain.event.entity.QEvent;
import zoo.insightnote.domain.reservation.dto.response.UserTicketInfoResponseDto;
import zoo.insightnote.domain.reservation.entity.QReservation;
import zoo.insightnote.domain.session.entity.QSession;

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

        return queryFactory
                .select(
                        session.id,
                        session.startTime,
                        session.endTime,
                        session.eventDay
                )
                .from(reservation)
                .join(reservation.session, session)
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

    public UserTicketInfoResponseDto processUserTicketInfo(String username) {
        QSession session = QSession.session;

        // 1️⃣ 유저가 신청한 세션 정보 조회
        List<Tuple> reservationSessions = findUserReservationInfo(username);
        List<Tuple> eventSessions = findEventInfo();

        // 2️⃣ 날짜별 세션 정보 저장
        Map<String, List<UserTicketInfoResponseDto.reservationSessions>> registeredSessions = new LinkedHashMap<>();
        Set<String> eventDates = new LinkedHashSet<>();
        Set<String> userReservedDates = new HashSet<>();

        for (Tuple row : reservationSessions) {
            String eventDay = row.get(session.eventDay).format(DateTimeFormatter.ofPattern("M월 d일"));
            String timeRange = row.get(session.startTime).format(DateTimeFormatter.ofPattern("HH:mm"))
                    + "~" + row.get(session.endTime).format(DateTimeFormatter.ofPattern("HH:mm"));
            Long sessionId = row.get(session.id);

            // 날짜별 세션 등록
            registeredSessions.computeIfAbsent(eventDay, k -> new ArrayList<>()).add(
                    new UserTicketInfoResponseDto.reservationSessions(timeRange, sessionId)
            );

            // 유저가 등록한 날짜 저장
            userReservedDates.add(eventDay);
        }

        for (Tuple row : eventSessions) {
            eventDates.add(row.get(0, String.class)); // 이벤트 시작 날짜
            eventDates.add(row.get(1, String.class)); // 이벤트 종료 날짜
        }

        // 3️⃣ 티켓 여부 설정
        Map<String, Boolean> tickets = new LinkedHashMap<>();
        for (String date : eventDates) {
            tickets.put(date, userReservedDates.contains(date));
        }

        // 4️⃣ DTO로 반환
        return UserTicketInfoResponseDto.builder()
                .tickets(tickets)
                .registeredSessions(registeredSessions)
                .build();
    }


}