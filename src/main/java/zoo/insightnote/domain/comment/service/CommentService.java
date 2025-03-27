package zoo.insightnote.domain.comment.service;

import java.util.List;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import zoo.insightnote.domain.comment.dto.res.CommentIdResDto;
import zoo.insightnote.domain.comment.dto.res.CommentListResDto;

import zoo.insightnote.domain.comment.dto.req.CommentCreateReqDto;
import zoo.insightnote.domain.comment.dto.req.CommentUpdateReqDto;
import zoo.insightnote.domain.comment.dto.res.CommentDefaultResDto;

import zoo.insightnote.domain.comment.entity.Comment;
import zoo.insightnote.domain.comment.mapper.CommentMapper;
import zoo.insightnote.domain.comment.repository.CommentRepository;
import zoo.insightnote.domain.insight.entity.Insight;
import zoo.insightnote.domain.insight.repository.InsightRepository;
import zoo.insightnote.domain.insight.service.InsightService;
import zoo.insightnote.domain.user.entity.User;
import zoo.insightnote.domain.user.repository.UserRepository;
import zoo.insightnote.domain.user.service.UserService;
import zoo.insightnote.global.exception.CustomException;
import zoo.insightnote.global.exception.ErrorCode;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final UserService userService;
    private final InsightRepository insightRepository;

    public CommentIdResDto createComment(Long insightId, String userName, CommentCreateReqDto request) {

        Insight insight = insightRepository.findById(insightId)
                .orElseThrow(() -> new CustomException(ErrorCode.INSIGHT_NOT_FOUND));

        User user = userService.findByUsername(userName);

        Comment comment = CommentMapper.toEntity(insight, user, request);

        commentRepository.save(comment);

        return new CommentIdResDto(comment.getId());
    }

    @Transactional
    public CommentIdResDto updateComment(Long insightId, String userName, Long commentId, CommentUpdateReqDto request) {

        Comment comment = findCommentById(commentId);
        User user = userService.findByUsername(userName);

        validateAuthor(comment, user.getId());

        comment.update(request.content());

        return new CommentIdResDto(comment.getId());
    }

    public void  deleteComment(Long insightId, String userName, Long commentId) {

        Comment comment = findCommentById(commentId);
        User user = userService.findByUsername(userName);

        validateAuthor(comment, user.getId());

        commentRepository.deleteById(commentId);

    }

    public List<CommentListResDto> getCommentsByInsight(Long insightId) {

        // 예외처리 로직 필요함

        List<Comment> comments = commentRepository.findByInsightIdWithUser(insightId);

        return comments.stream()
                .map(CommentListResDto::new)
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
