package zoo.insightnote.domain.comment.service;

import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import zoo.insightnote.domain.comment.dto.CommentRequest;
import zoo.insightnote.domain.comment.dto.CommentResponse;
import zoo.insightnote.domain.comment.entity.Comment;
import zoo.insightnote.domain.comment.mapper.CommentMapper;
import zoo.insightnote.domain.comment.repository.CommentRepository;
import zoo.insightnote.domain.insight.entity.Insight;
import zoo.insightnote.domain.insight.repository.InsightRepository;
import zoo.insightnote.domain.user.entity.User;
import zoo.insightnote.domain.user.repository.UserRepository;
import zoo.insightnote.global.exception.CustomException;
import zoo.insightnote.global.exception.ErrorCode;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final InsightRepository insightRepository;

    public CommentResponse createComment(Long insightId, Long userId, CommentRequest.Create request) {

        Insight insight = insightRepository.findById(insightId)
                .orElseThrow(() -> new CustomException(null, "인사이트 노트를 찾을 수 없음"));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(null, "사용자를 찾을 수 없음"));

        Comment comment = CommentMapper.toEntity(insight, user, request);

        comment = commentRepository.save(comment);

        return CommentMapper.toResponse(comment);
    }

    public List<CommentResponse> findCommentsByInsightId(Long insightId) {

        List<Comment> comments = commentRepository.findAllByInsightId(insightId);

        List<CommentResponse> responses = new ArrayList<>();
        for (Comment comment : comments) {
            responses.add(CommentMapper.toResponse(comment));
        }

        return responses;
    }

    @Transactional
    public CommentResponse updateComment(Long insightId, Long userId, Long commentId, CommentRequest.Update request) {

        Comment comment = findCommentById(commentId);

        validateAuthor(comment, userId);

        comment.update(request.content());

        return CommentMapper.toResponse(comment);
    }

    public CommentResponse deleteComment(Long insightId, Long userId, Long commentId) {

        Comment comment = findCommentById(commentId);

        validateAuthor(comment, userId);

        commentRepository.deleteById(commentId);

        return new CommentResponse.Delete(commentId);
    }

    private Comment findCommentById(Long commentId) {
        return commentRepository.findById(commentId)
                .orElseThrow(() -> new CustomException(ErrorCode.COMMENT_NOT_FOUND));
    }

    private void validateAuthor(Comment comment, Long userId) {
        if (!comment.getUser().getId().equals(userId)) {
            throw new CustomException(ErrorCode.UNAUTHORIZED_COMMENT_MODIFICATION);
        }
    }

}
