package zoo.insightnote.domain.comment.mapper;

import zoo.insightnote.domain.comment.dto.req.CommentCreateReqDto;
import zoo.insightnote.domain.comment.dto.res.CommentDefaultResDto;

import zoo.insightnote.domain.comment.entity.Comment;
import zoo.insightnote.domain.insight.entity.Insight;
import zoo.insightnote.domain.user.entity.User;

public class CommentMapper {

    public static Comment toEntity(Insight insight, User user, CommentCreateReqDto request) {
        return Comment.builder()
                .insight(insight)
                .user(user)
                .content(request.content())
                .build();
    }

    public static CommentDefaultResDto toResponse(Comment comment) {
        return new CommentDefaultResDto (
                comment.getId(),
                comment.getContent(),
                comment.getCreateAt(),
                comment.getUser() == null ? "Anonymous" : "user"
        );
    }

}
