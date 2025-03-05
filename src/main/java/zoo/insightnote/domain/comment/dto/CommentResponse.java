package zoo.insightnote.domain.comment.dto;

import java.time.LocalDateTime;

public interface CommentResponse {

    record Delete(Long commentId) implements CommentResponse{

    }

    record Default(Long commentId, String content, LocalDateTime createAt, String author) implements CommentResponse{

    }
}
