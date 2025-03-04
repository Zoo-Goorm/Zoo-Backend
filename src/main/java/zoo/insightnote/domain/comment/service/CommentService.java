package zoo.insightnote.domain.comment.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
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

}
