package zoo.insightnote.domain.reservation.repository;

import com.querydsl.core.Tuple;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import zoo.insightnote.domain.event.entity.QEvent;
import zoo.insightnote.domain.reservation.entity.QReservation;
import zoo.insightnote.domain.session.entity.QSession;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


@Repository
@RequiredArgsConstructor
@Slf4j
public class ReservationQueryRepository {
    private final JPAQueryFactory queryFactory;

    public List<Tuple> findUserReservationInfo(Long userId) {
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
                .where(reservation.user.id.eq(userId))
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

        Map<String, List<Map<String, Object>>> registeredSessions = new LinkedHashMap<>();


        for (Tuple row : reservationSessions) {
            String eventDay = row.get(session.eventDay).format(DateTimeFormatter.ofPattern("M월 d일"));
            String timeRange = row.get(session.startTime).format(DateTimeFormatter.ofPattern("HH:mm"))
                    + "~" + row.get(session.endTime).format(DateTimeFormatter.ofPattern("HH:mm"));
            Long sessionId = row.get(session.id);

            registeredSessions.computeIfAbsent(eventDay, k -> new ArrayList<>()).add(
                    Map.of("timeRange", timeRange, "sessionId", sessionId)
            );
        }

        Map<String, Boolean> tickets = new LinkedHashMap<>();
        for (String date : eventDates) {
            tickets.put(date, registeredSessions.containsKey(date));
        }

        Map<String, Object> result = new LinkedHashMap<>();
        result.put("tickets", tickets);
        result.put("registeredSessions", registeredSessions);

        return result;

    }

}