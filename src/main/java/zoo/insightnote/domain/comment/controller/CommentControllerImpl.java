package zoo.insightnote.domain.comment.controller;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import zoo.insightnote.domain.comment.dto.response.CommentListResponse;

import zoo.insightnote.domain.comment.dto.request.CommentCreateRequest;
import zoo.insightnote.domain.comment.dto.request.CommentUpdateRequest;
import zoo.insightnote.domain.comment.dto.response.CommentIdResponse;

import zoo.insightnote.domain.comment.service.CommentService;

@RestController
@RequestMapping("/api/v1/insights")
@RequiredArgsConstructor
public class CommentControllerImpl implements CommentController {

    private final CommentService commentService;

    @Override
    @PostMapping("/{insightId}/comments")
    public ResponseEntity<CommentIdResponse> writeComment(
            @PathVariable Long insightId,
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestBody CommentCreateRequest request)
    {
        CommentIdResponse commentId = commentService.createComment(insightId, userDetails.getUsername(), request);
        return ResponseEntity.ok().body(commentId);
    }

    @Override
    @PutMapping("/{insightId}/comments/{commentId}")
    public ResponseEntity<CommentIdResponse> updateComment(
            @PathVariable Long insightId,
            @PathVariable Long commentId,
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestBody CommentUpdateRequest request
    ) {
        CommentIdResponse response = commentService.updateComment(insightId, userDetails.getUsername(), commentId, request);
        return ResponseEntity.ok().body(response);
    }

    @Override
    @DeleteMapping("/{insightId}/comments/{commentId}")
    public ResponseEntity<Void> deleteComment(
            @PathVariable Long insightId,
            @PathVariable Long commentId,
            @AuthenticationPrincipal UserDetails userDetails)
    {
        commentService.deleteComment(insightId, userDetails.getUsername(), commentId);
        return ResponseEntity.noContent().build();
    }

    @Override
    @GetMapping("/comments/{insightId}")
    public ResponseEntity<List<CommentListResponse>> getListComments(@PathVariable Long insightId) {
        List<CommentListResponse> comments = commentService.getCommentsByInsight(insightId);
        return ResponseEntity.ok(comments);
    }
}


