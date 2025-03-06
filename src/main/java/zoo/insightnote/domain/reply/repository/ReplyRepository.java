package zoo.insightnote.domain.reply.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import zoo.insightnote.domain.reply.entity.Reply;

public interface ReplyRepository extends JpaRepository<Reply, Long> {

    @Query("SELECT r FROM Reply r where r.comment.id = :commentId")
    List<Reply> findAllByCommentId(Long commentId);
}
