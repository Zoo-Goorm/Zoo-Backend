package zoo.insightnote.domain.comment.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import zoo.insightnote.domain.comment.entity.Comment;

public interface CommentRepository extends JpaRepository<Comment, Long> {
}
