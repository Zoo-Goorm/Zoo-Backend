package zoo.insightnote.domain.comment.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

public record CommentCreateRequest(
        @Schema(description = "댓글 내용", example = "작성하신 노트 잘보았습니다!")
        @NotBlank(message = "댓글 내용은 비어 있을 수 없습니다.")
        String content
) {}