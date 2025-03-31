package zoo.insightnote.domain.comment.service;

import java.util.List;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import zoo.insightnote.domain.comment.dto.request.CommentIdResponse;
import zoo.insightnote.domain.comment.dto.request.CommentListResponse;

import zoo.insightnote.domain.comment.dto.response.CommentCreateRequest;
import zoo.insightnote.domain.comment.dto.response.CommentUpdateRequest;

import zoo.insightnote.domain.comment.entity.Comment;
import zoo.insightnote.domain.comment.mapper.CommentMapper;
import zoo.insightnote.domain.comment.repository.CommentRepository;
import zoo.insightnote.domain.insight.entity.Insight;
import zoo.insightnote.domain.insight.repository.InsightRepository;
import zoo.insightnote.domain.user.entity.User;
import zoo.insightnote.domain.user.service.UserService;
import zoo.insightnote.global.exception.CustomException;
import zoo.insightnote.global.exception.ErrorCode;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final UserService userService;
    private final InsightRepository insightRepository;

    public CommentIdResponse createComment(Long insightId, String userName, CommentCreateRequest request) {

        Insight insight = insightRepository.findById(insightId)
                .orElseThrow(() -> new CustomException(ErrorCode.INSIGHT_NOT_FOUND));

        User user = userService.findByUsername(userName);

        Comment comment = CommentMapper.toEntity(insight, user, request);

        commentRepository.save(comment);

        return new CommentIdResponse(comment.getId());
    }

    @Transactional
    public CommentIdResponse updateComment(Long insightId, String userName, Long commentId, CommentUpdateRequest request) {

        Comment comment = findCommentById(commentId);
        User user = userService.findByUsername(userName);

        validateAuthor(comment, user.getId());

        comment.update(request.content());

        return new CommentIdResponse(comment.getId());
    }

    public void  deleteComment(Long insightId, String userName, Long commentId) {

        Comment comment = findCommentById(commentId);
        User user = userService.findByUsername(userName);

        validateAuthor(comment, user.getId());

        commentRepository.deleteById(commentId);

    }

    public List<CommentListResponse> getCommentsByInsight(Long insightId) {
        List<Comment> comments = commentRepository.findByInsightIdWithUser(insightId);

        return comments.stream()
                .map(CommentListResponse::new)
                .collect(Collectors.toList());
    }

    public Comment findCommentById(Long commentId) {
        return commentRepository.findById(commentId)
                .orElseThrow(() -> new CustomException(ErrorCode.COMMENT_NOT_FOUND));
    }

    private void validateAuthor(Comment comment, Long userId) {
        if (!comment.getUser().getId().equals(userId)) {
            throw new CustomException(ErrorCode.UNAUTHORIZED_COMMENT_MODIFICATION);
        }
    }

}
