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
}
