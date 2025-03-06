package zoo.insightnote.domain.comment.mapper;

import zoo.insightnote.domain.comment.dto.CommentRequest;
import zoo.insightnote.domain.comment.dto.CommentResponse;
import zoo.insightnote.domain.comment.entity.Comment;
import zoo.insightnote.domain.insight.entity.Insight;
import zoo.insightnote.domain.user.entity.User;

public class CommentMapper {

    public static Comment toEntity(Insight insight, User user, CommentRequest.Create request) {
        return Comment.builder()
                .insight(insight)
                .user(user)
                .content(request.content())
                .build();
    }

    public static CommentResponse toResponse(Comment comment) {
        return new CommentResponse.Default(
                comment.getId(),
                comment.getContent(),
                comment.getCreateAt(),
                comment.getUser() == null ? "Anonymous" : "user"
        );
    }

}
