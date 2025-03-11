package zoo.insightnote.domain.session.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import zoo.insightnote.domain.image.entity.EntityType;
import zoo.insightnote.domain.session.dto.SessionResponseDto;
import zoo.insightnote.domain.session.entity.Session;

import java.util.List;

public interface SessionRepository extends JpaRepository<Session, Long> {


// 1. 세션 전체 조회 (연사 이미지 제외, 인원수 제외, 키워드 포함)
    @Query("""
    SELECT s.id, s.name, k.name, s.shortDescription, s.location,s.startTime, s.endTime
    FROM Session s
    LEFT JOIN SessionKeyword sk ON sk.session.id = s.id
    LEFT JOIN Keyword k ON sk.keyword.id = k.id
    ORDER BY s.id ASC
    """)
    List<Object[]> findAllSessions();

    // 2. 세션 상세 조회 (연사 이미지, 인원수 포함, 키워드 포함)
    @Query("""
    SELECT s.id, s.name, k.name, s.shortDescription, s.maxCapacity, s.participantCount, s.location,
        sp.name, img.fileUrl,s.startTime, s.endTime, s.status
    FROM Session s
    JOIN Speaker sp ON s.speaker.id = sp.id
    LEFT JOIN Image img ON img.entityId = sp.id AND img.entityType = :entityType
    LEFT JOIN SessionKeyword sk ON sk.session.id = s.id
    LEFT JOIN Keyword k ON sk.keyword.id = k.id
    ORDER BY s.id ASC
    """)
    List<Object[]> findAllSessionsWithDetails(@Param("entityType") EntityType entityType);
}
