package zoo.insightnote.domain.reply.controller;

import java.util.Collections;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import zoo.insightnote.domain.reply.dto.ReplyRequest;
import zoo.insightnote.domain.reply.dto.ReplyResponse;
import zoo.insightnote.domain.reply.service.ReplyService;

@RestController
@RequestMapping("/api/v1/insights")
@RequiredArgsConstructor
public class ReplyControllerImpl implements ReplyController {

    private final ReplyService replyService;

    @PostMapping("/{insightId}/comments/{commentId}/replies")
    public ResponseEntity<ReplyResponse> writeReply(@PathVariable Long insightId,
                                                    @PathVariable Long commentId,
                                                    @AuthenticationPrincipal UserDetails userDetails,
                                                    @RequestBody ReplyRequest.Create request) {
        userDetails = validateUser(userDetails);

        ReplyResponse response = replyService.createReply(commentId, Long.valueOf(userDetails.getUsername()), request);

        return ResponseEntity.ok().body(response);
    }

    @GetMapping("/{insightId}/comments/{commentId}/replies")
    public ResponseEntity<List<ReplyResponse>> listReplies(@PathVariable String insightId,
                                                           @PathVariable Long commentId) {
        List<ReplyResponse> response = replyService.findRepliesByCommentId(commentId);

        return ResponseEntity.ok().body(response);
    }

    // 임시 로직
    private UserDetails validateUser(UserDetails userDetails) {
        if (userDetails != null) {
            return userDetails;
        }
        return new User("001", "password", Collections.emptyList());
    }
}
