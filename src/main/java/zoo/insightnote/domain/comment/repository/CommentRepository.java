package zoo.insightnote.domain.comment.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import zoo.insightnote.domain.comment.entity.Comment;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    @Query("SELECT c FROM Comment c WHERE c.insight.id = :insightId")
    List<Comment> findAllByInsightId(Long insightId);

    @Query("SELECT c FROM Comment c JOIN FETCH c.user WHERE c.insight.id = :insightId")
    List<Comment> findByInsightIdWithUser(@Param("insightId") Long insightId);

    // 뱃지 기능 관련 쿼리
    @Query("""
    SELECT DISTINCT c.insight.id
    FROM Comment c
    JOIN c.user u
    WHERE c.insight.id IN :insightIds
    AND u.role = 'SPEAKER'
    """)
    List<Long> findInsightIdsWithSpeakerComments(@Param("insightIds") List<Long> insightIds);

    // 뱃지 기능 관련 쿼리
    @Query("""
    SELECT COUNT(c)
    FROM Comment c
    WHERE c.insight.id = :insightId
      AND c.user.role = 'SPEAKER'
    """)
    long countSpeakerCommentsOnInsight(@Param("insightId") Long insightId);

}
