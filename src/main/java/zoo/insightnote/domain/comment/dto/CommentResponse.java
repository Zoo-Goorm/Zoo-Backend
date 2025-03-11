package zoo.insightnote.domain.comment.dto;

import java.time.LocalDate;

public interface CommentResponse {

    record Delete(Long commentId) implements CommentResponse{

    }

    record Default(Long commentId, String content, LocalDate createAt, String author) implements CommentResponse{

    }
}
