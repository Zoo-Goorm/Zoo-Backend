package zoo.insightnote.domain.reply.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import zoo.insightnote.domain.reply.entity.Reply;

public interface ReplyRepository extends JpaRepository<Reply, Long> {
}
