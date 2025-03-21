package zoo.insightnote.domain.comment.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import zoo.insightnote.domain.comment.entity.Comment;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Getter
@AllArgsConstructor
public class CommentResponseDto {
    private Long id;
    private String name;
    private String createdAt;
    private boolean edited;
    private String role;
    private String content;

    public CommentResponseDto(Comment comment) {
        this.id = comment.getId();
        this.name = comment.getUser().getName();  // User 엔티티에 name 필드가 있다고 가정
        this.createdAt = formatDate(comment.getCreateAt());
        this.edited = comment.isUpdated();
        this.role = comment.getUser().getRole().toString(); // User 엔티티에 role 필드가 있다고 가정
        this.content = comment.getContent();
    }

    private String formatDate(LocalDateTime dateTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return dateTime.format(formatter);
    }
}