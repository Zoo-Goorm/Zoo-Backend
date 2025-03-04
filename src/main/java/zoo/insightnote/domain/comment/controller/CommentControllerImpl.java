package zoo.insightnote.domain.comment.controller;

import java.util.Collections;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import zoo.insightnote.domain.comment.dto.CommentRequest.Create;
import zoo.insightnote.domain.comment.dto.CommentResponse;
import zoo.insightnote.domain.comment.service.CommentService;

@RestController
@RequestMapping("/api/v1/insights")
@RequiredArgsConstructor
public class CommentControllerImpl implements CommentController {

    private final CommentService commentService;

    @PostMapping("/{insightId}/comments")
    public ResponseEntity<CommentResponse> writeComment(@PathVariable("insightId") Long insightId, @AuthenticationPrincipal UserDetails userDetails, @RequestBody Create request) {
        // 임시 로직
        if (userDetails == null) {
            userDetails = new User("1", "temp", Collections.emptyList());
        }
        return ResponseEntity.ok().body(commentService.createComment(insightId, Long.valueOf(userDetails.getUsername()), request));
    }

}


