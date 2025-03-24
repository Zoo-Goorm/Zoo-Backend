package zoo.insightnote.domain.comment.controller;

import java.util.Collections;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import zoo.insightnote.domain.comment.dto.CommentRequest;
import zoo.insightnote.domain.comment.dto.CommentRequest.Create;
import zoo.insightnote.domain.comment.dto.CommentResponse;
import zoo.insightnote.domain.comment.dto.CommentResponseDto;
import zoo.insightnote.domain.comment.service.CommentService;

@RestController
@RequestMapping("/api/v1/insights")
@RequiredArgsConstructor
public class CommentControllerImpl implements CommentController {

    private final CommentService commentService;

    @Override
    @PostMapping("/{insightId}/comments")
    public ResponseEntity<CommentResponse> writeComment(@PathVariable Long insightId,
                                                        @AuthenticationPrincipal UserDetails userDetails,
                                                        @RequestBody Create request) {
        userDetails = validateUser(userDetails);
        CommentResponse response = commentService.createComment(insightId, Long.valueOf(userDetails.getUsername()), request);
        return ResponseEntity.ok().body(response);
    }

//    @Override
//    @GetMapping("/{insightId}/comments")
//    public ResponseEntity<List<CommentResponse>> listComments(@PathVariable Long insightId) {
//        return ResponseEntity.ok().body(commentService.findCommentsByInsightId(insightId));
//    }

    @Override
    @PutMapping("/{insightId}/comments/{commentId}")
    public ResponseEntity<CommentResponse> updateComment(@PathVariable Long insightId,
                                                         @PathVariable Long commentId,
                                                         @AuthenticationPrincipal UserDetails userDetails,
                                                         @RequestBody CommentRequest.Update request) {
        userDetails = validateUser(userDetails);
        CommentResponse response = commentService.updateComment(insightId, Long.valueOf(userDetails.getUsername()), commentId, request);
        return ResponseEntity.ok().body(response);
    }

    @Override
    @DeleteMapping("/{insightId}/comments/{commentId}")
    public ResponseEntity<CommentResponse> deleteComment(@PathVariable Long insightId,
                                                         @PathVariable Long commentId,
                                                         @AuthenticationPrincipal UserDetails userDetails) {
        userDetails = validateUser(userDetails);
        CommentResponse response = commentService.deleteComment(insightId, Long.valueOf(userDetails.getUsername()), commentId);
        return ResponseEntity.ok().body(response);
    }

    // 임시 로직
    private UserDetails validateUser(UserDetails userDetails) {
        if (userDetails != null) {
            return userDetails;
        }
        return new User("001", "password", Collections.emptyList());
    }


    // 댓글 리스트 출력
    @Override
    @GetMapping("/comments/{insightId}")
    public ResponseEntity<List<CommentResponseDto>> getListComments(@PathVariable Long insightId) {
        List<CommentResponseDto> comments = commentService.getCommentsByInsight(insightId);
        return ResponseEntity.ok(comments);
    }
}


