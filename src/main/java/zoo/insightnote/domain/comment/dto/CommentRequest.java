package zoo.insightnote.domain.comment.dto;


import io.swagger.v3.oas.annotations.media.Schema;

public interface CommentRequest {
    record Create(
            @Schema(description = "댓글 내용", example = "작성하신 노트 잘보았습니다!")
            String content
    ) implements CommentRequest {
    }
}
