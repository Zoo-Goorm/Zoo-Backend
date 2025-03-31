package zoo.insightnote.domain.comment.dto.request;
import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.AccessLevel;

@Getter
public record CommentDefaultResponse (
     Long commentId,
     String content,
     LocalDateTime createAt,
     String author
){
}