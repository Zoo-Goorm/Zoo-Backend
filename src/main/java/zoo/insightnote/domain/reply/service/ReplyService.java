package zoo.insightnote.domain.reply.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import zoo.insightnote.domain.comment.entity.Comment;
import zoo.insightnote.domain.comment.service.CommentService;
import zoo.insightnote.domain.reply.dto.ReplyRequest;
import zoo.insightnote.domain.reply.dto.ReplyResponse;
import zoo.insightnote.domain.reply.entity.Reply;
import zoo.insightnote.domain.reply.mapper.ReplyMapper;
import zoo.insightnote.domain.reply.repository.ReplyRepository;
import zoo.insightnote.domain.user.entity.User;
import zoo.insightnote.domain.user.repository.UserRepository;
import zoo.insightnote.global.exception.CustomException;
import zoo.insightnote.global.exception.ErrorCode;

@Service
@RequiredArgsConstructor
public class ReplyService {

    private final ReplyRepository replyRepository;
    private final CommentService commentService;
    private final UserRepository userRepository;

    public ReplyResponse createReply(Long commentId, Long userId, ReplyRequest.Create request) {

        Comment comment = hasComment(commentId);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(null, "사용자를 찾을 수 없습니다."));

        Reply reply = ReplyMapper.toEntity(comment, user, request.content());

        replyRepository.save(reply);

        return ReplyMapper.toResponse(reply);
    }

    private Comment hasComment(Long commentId) {
        try {
            return commentService.findCommentById(commentId);
        } catch (CustomException e) {
            throw new CustomException(ErrorCode.DELETED_COMMENT_CANNOT_HAVE_REPLY);
        }
    }

}
