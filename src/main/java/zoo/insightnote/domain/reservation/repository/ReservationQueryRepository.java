package zoo.insightnote.domain.reservation.repository;

import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import zoo.insightnote.domain.reservation.entity.QReservation;
import zoo.insightnote.domain.session.entity.QSession;

import java.util.List;

@Repository
@RequiredArgsConstructor
@Slf4j
public class ReservationQueryRepository {
    private final JPAQueryFactory queryFactory;

    public void findUserTicketInfo(Long userId) {
        QReservation reservation = QReservation.reservation;
        QSession session = QSession.session;

        List<Tuple> results = queryFactory
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

        log.info("result: {}", results);
    }

}
