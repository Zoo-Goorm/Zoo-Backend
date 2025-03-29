package zoo.insightnote.domain.insight.dto.response.query;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
public class SessionInsightListQuery {
    private Long id;
    private String memo;
    private Boolean isPublic;
    private Boolean isAnonymous;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Long likeCount;
    private Long commentCount;
    private String displayName;
    private String job;

    @Setter
    private Boolean isLiked;

    @Setter
    private Boolean hasSpeakerComment;

    public SessionInsightListQuery(
            Long id, String memo, Boolean isPublic, Boolean isAnonymous,
            LocalDateTime createdAt, LocalDateTime updatedAt,
            Long likeCount, Long commentCount, String displayName, String job
    ) {
        this.id = id;
        this.memo = memo;
        this.isPublic = isPublic;
        this.isAnonymous = isAnonymous;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.likeCount = likeCount;
        this.commentCount = commentCount;
        this.displayName = displayName;
        this.job = job;
    }
}