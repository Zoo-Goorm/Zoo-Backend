package zoo.insightnote.domain.comment.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

public record CommentUpdateRequest(
        @Schema(description = "댓글 내용", example = "작성하신 노트에 잘못된 부분이 있는 것 같습니다!")
        @NotBlank(message = "댓글 내용은 비어 있을 수 없습니다.")
        String content
) {}