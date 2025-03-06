package zoo.insightnote.domain.reply.mapper;

import zoo.insightnote.domain.comment.entity.Comment;
import zoo.insightnote.domain.reply.dto.ReplyResponse;
import zoo.insightnote.domain.reply.dto.ReplyResponse.Default;
import zoo.insightnote.domain.reply.entity.Reply;
import zoo.insightnote.domain.user.entity.User;

public class ReplyMapper {
    public static Reply toEntity(Comment comment, User user, String content) {
        return Reply.builder()
                .comment(comment)
                .user(user)
                .content(content)
                .build();
    }

    public static ReplyResponse toResponse(Reply reply) {
        return new Default(
                reply.getComment().getId(),
                reply.getId(),
                reply.getContent(),
                reply.getCreateAt(),
                reply.getUser().getName()
        );
    }
}
