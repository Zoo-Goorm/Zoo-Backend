package zoo.insightnote.domain.session.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import zoo.insightnote.domain.session.entity.Session;

import java.util.List;
import java.util.Optional;

public interface SessionRepository extends JpaRepository<Session, Long> {
    Optional<Session> findByEventId(Long eventId);
    Optional<Session> findById(Long sessionId);

    // 기존 예약 세션 시간 조회
    @Query("""
    SELECT s.startTime, s.endTime
    FROM Session s
    WHERE s.id IN :sessionIds
""")
    List<Object[]> findReservedSessionTimes(@Param("sessionIds") List<Long> sessionIds);

    // 신청하려는 세션 시간 조회
    @Query("""
    SELECT s.startTime, s.endTime
    FROM Session s
    WHERE s.id = :sessionId
""")
    List<Object[]> findTargetSessionTime(@Param("sessionId") Long sessionId);


}


