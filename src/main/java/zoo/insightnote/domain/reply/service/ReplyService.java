package zoo.insightnote.domain.reply.service;

import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import zoo.insightnote.domain.comment.entity.Comment;
import zoo.insightnote.domain.comment.service.CommentService;
import zoo.insightnote.domain.reply.dto.ReplyRequest;
import zoo.insightnote.domain.reply.dto.ReplyRequest.Update;
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

        User user = findUserById(userId);

        Reply reply = ReplyMapper.toEntity(comment, user, request.content());

        replyRepository.save(reply);

        return ReplyMapper.toResponse(reply);
    }

    public List<ReplyResponse> findRepliesByCommentId(Long commentId) {

        List<Reply> replies = replyRepository.findAllByCommentId(commentId);

        List<ReplyResponse> responses = new ArrayList<>();
        for (Reply reply : replies) {
            responses.add(ReplyMapper.toResponse(reply));
        }

        return responses;
    }

    private Comment hasComment(Long commentId) {
        try {
            return commentService.findCommentById(commentId);
        } catch (CustomException e) {
            throw new CustomException(ErrorCode.DELETED_COMMENT_CANNOT_HAVE_REPLY);
        }
    }

    public ReplyResponse updateReply(Long commentId, Long replyId, Long userId, Update request) {

        hasComment(commentId);

        User user = findUserById(userId);

        Reply reply = findReplyById(replyId);

        validateAuthor(reply, user.getId());

        reply.update(request.content());

        return ReplyMapper.toResponse(reply);
    }

    public ReplyResponse deleteReply(Long commentId, Long replyId, Long userId) {

        hasComment(commentId);

        User user = findUserById(userId);

        Reply reply = findReplyById(replyId);

        validateAuthor(reply, user.getId());

        replyRepository.deleteById(reply.getId());

        return ReplyMapper.toResponse(reply);
    }

    // TODO : 유저 도메인 개발 완료시 삭제
    private User findUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(null, "사용자를 찾을 수 없습니다."));
    }

    public Reply findReplyById(Long replyId) {
        return replyRepository.findById(replyId)
                .orElseThrow(() -> new CustomException(ErrorCode.DELETED_COMMENT_CANNOT_HAVE_REPLY));
    }

    private void validateAuthor(Reply reply, Long userId) {
        if (!reply.getUser().getId().equals(userId)) {
            throw new CustomException(ErrorCode.UNAUTHORIZED_COMMENT_MODIFICATION);
        }
    }
}
