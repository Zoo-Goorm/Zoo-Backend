package zoo.insightnote.domain.comment.dto.response;
import java.time.LocalDateTime;

public record CommentDefaultResponse (
     Long commentId,
     String content,
     LocalDateTime createAt,
     String author
){
}