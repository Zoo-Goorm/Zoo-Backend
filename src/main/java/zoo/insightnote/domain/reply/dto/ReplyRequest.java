package zoo.insightnote.domain.reply.dto;

public interface ReplyRequest {
    record Create(String content) implements ReplyRequest{

    }
}
