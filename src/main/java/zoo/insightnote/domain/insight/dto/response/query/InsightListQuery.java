package zoo.insightnote.domain.insight.dto.response.query;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;


@Getter
public class InsightListQuery {
    private Long id;
    private String memo;
    private Boolean isPublic;
    private Boolean isAnonymous;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Long sessionId;
    private String sessionName;
    private Long likeCount;
    private String latestImageUrl;
    private String interestCategory;
    private Long commentCount;
    private String displayName;
    private String job;

    @Setter
    private Boolean isLiked;

    public InsightListQuery(Long id, String memo, Boolean isPublic, Boolean isAnonymous, LocalDateTime createdAt, LocalDateTime updatedAt,
                            Long sessionId, String sessionName, Long likeCount, String latestImageUrl, String interestCategory,
                            Long commentCount, String displayName, String job) {
        this.id = id;
        this.memo = memo;
        this.isPublic = isPublic;
        this.isAnonymous = isAnonymous;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.sessionId = sessionId;
        this.sessionName = sessionName;
        this.likeCount = likeCount;
        this.latestImageUrl = latestImageUrl;
        this.interestCategory = interestCategory;
        this.commentCount = commentCount;
        this.displayName = displayName;
        this.job = job;
    }
}