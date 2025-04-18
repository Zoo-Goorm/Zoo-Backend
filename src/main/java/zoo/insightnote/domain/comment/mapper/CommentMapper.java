package zoo.insightnote.domain.comment.mapper;

import zoo.insightnote.domain.comment.dto.request.CommentCreateRequest;
import zoo.insightnote.domain.comment.dto.response.CommentDefaultResponse;

import zoo.insightnote.domain.comment.entity.Comment;
import zoo.insightnote.domain.insight.entity.Insight;
import zoo.insightnote.domain.user.entity.User;

public class CommentMapper {

    public static Comment toEntity(Insight insight, User user, CommentCreateRequest request) {
        return Comment.builder()
                .insight(insight)
                .user(user)
                .content(request.content())
                .build();
    }

    public static CommentDefaultResponse toResponse(Comment comment) {
        return new CommentDefaultResponse(
                comment.getId(),
                comment.getContent(),
                comment.getCreateAt(),
                comment.getUser() == null ? "Anonymous" : "user"
        );
    }

}
