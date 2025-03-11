package zoo.insightnote.domain.reply.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import zoo.insightnote.domain.reply.dto.ReplyRequest;
import zoo.insightnote.domain.reply.dto.ReplyResponse;

@Tag(name = "Reply API", description = "대댓글 관련 API")
public interface ReplyController {

    @Operation(summary = "대댓글 작성", description = "사용자가 특정 댓글에 대댓글을 작성합니다.")
    ResponseEntity<ReplyResponse> writeReply(
            @Parameter(description = "인사이트 ID") @PathVariable Long insightId,
            @Parameter(description = "댓글 ID") @PathVariable Long commentId,
            @Parameter(hidden = true) @AuthenticationPrincipal UserDetails userDetails,
            @RequestBody ReplyRequest.Create request);

    @Operation(summary = "대댓글 목록 조회", description = "특정 댓글에 대한 대댓글 목록을 조회합니다.")
    ResponseEntity<List<ReplyResponse>> listReplies(
            @Parameter(description = "인사이트 ID") @PathVariable Long insightId,
            @Parameter(description = "댓글 ID") @PathVariable Long commentId);

    @Operation(summary = "대댓글 수정", description = "작성자가 본인의 대댓글을 수정합니다.")
    ResponseEntity<ReplyResponse> updateReply(
            @Parameter(description = "인사이트 ID") @PathVariable Long insightId,
            @Parameter(description = "댓글 ID") @PathVariable Long commentId,
            @Parameter(description = "대댓글 ID") @PathVariable Long replyId,
            @Parameter(hidden = true) @AuthenticationPrincipal UserDetails userDetails,
            @RequestBody ReplyRequest.Update request);

    @Operation(summary = "대댓글 삭제", description = "작성자가 본인의 대댓글을 삭제합니다.")
    ResponseEntity<ReplyResponse> deleteReply(
            @Parameter(description = "인사이트 ID") @PathVariable Long insightId,
            @Parameter(description = "댓글 ID") @PathVariable Long commentId,
            @Parameter(description = "대댓글 ID") @PathVariable Long replyId,
            @Parameter(hidden = true) @AuthenticationPrincipal UserDetails userDetails);
}
