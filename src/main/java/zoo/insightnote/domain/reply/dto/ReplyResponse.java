package zoo.insightnote.domain.reply.dto;

import java.time.LocalDateTime;

public interface ReplyResponse {
    record Default(Long parentCommentId, Long childCommentId, String content, LocalDateTime createAt,
                   String author) implements ReplyResponse {

    }
}
