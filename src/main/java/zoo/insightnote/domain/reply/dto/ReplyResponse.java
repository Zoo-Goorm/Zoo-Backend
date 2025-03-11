package zoo.insightnote.domain.reply.dto;

import java.time.LocalDate;

public interface ReplyResponse {
    record Default(Long parentCommentId, Long childCommentId, String content, LocalDate createAt,
                   String author) implements ReplyResponse {

    }
}
