package zoo.insightnote.domain.comment.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import zoo.insightnote.domain.comment.dto.request.CommentCreateRequest;
import zoo.insightnote.domain.comment.dto.request.CommentUpdateRequest;
import zoo.insightnote.domain.comment.dto.response.CommentIdResponse;
import zoo.insightnote.domain.comment.dto.response.CommentListResponse;


@Tag(name = "Comment API", description = "댓글 관련 API")
public interface CommentController {

    @Operation(summary = "댓글 작성", description = "사용자가 특정 인사이트에 댓글을 작성합니다.")
    ResponseEntity<CommentIdResponse> writeComment(
            @Parameter(description = "인사이트 ID") @PathVariable Long insightId,
            @Parameter(hidden = true) @AuthenticationPrincipal UserDetails userDetails,
            @RequestBody CommentCreateRequest request);

    @Operation(summary = "댓글 수정", description = "작성자가 본인의 댓글을 수정합니다.")
    ResponseEntity<CommentIdResponse> updateComment(
            @Parameter(description = "인사이트 ID") @PathVariable Long insightId,
            @Parameter(description = "댓글 ID") @PathVariable Long commentId,
            @Parameter(hidden = true) @AuthenticationPrincipal UserDetails userDetails,
            @RequestBody CommentUpdateRequest request);

    @Operation(summary = "댓글 삭제", description = "작성자가 본인의 댓글을 삭제합니다.")
    ResponseEntity<Void> deleteComment(
            @Parameter(description = "인사이트 ID") @PathVariable Long insightId,
            @Parameter(description = "댓글 ID") @PathVariable Long commentId,
            @Parameter(hidden = true) @AuthenticationPrincipal UserDetails userDetails);

    @Operation(summary = "댓글 목록 조회", description = "특정 인사이트의 댓글 목록을 조회합니다.")
    ResponseEntity<List<CommentListResponse>> getListComments(
            @Parameter(description = "인사이트 ID") @PathVariable Long insightId);
}