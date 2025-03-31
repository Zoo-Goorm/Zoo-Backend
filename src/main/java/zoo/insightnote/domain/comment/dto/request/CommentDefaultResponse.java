package zoo.insightnote.domain.comment.dto.request;
import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.AccessLevel;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CommentDefaultResponse {
    private Long commentId;
    private String content;
    private LocalDateTime createAt;
    private String author;
}