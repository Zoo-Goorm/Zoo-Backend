package zoo.insightnote.domain.insight.dto.response.query;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class InsightTopListQuery {
    private Long id;
    private String memo;
    private Boolean isPublic;
    private Boolean isAnonymous;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Long likeCount;
    private String imageUrl;
    private Long commentCount;
    private String displayName;
    private String job;
    private String interestCategory;

    @Setter  // isLiked 필드에 대해서만 setter를 생성
    private Boolean isLiked;

    public InsightTopListQuery(
            Long id,
            String memo,
            Boolean isPublic,
            Boolean isAnonymous,
            LocalDateTime createdAt,
            LocalDateTime updatedAt,
            Long likeCount,
            String imageUrl,
            Long commentCount,
            String displayName,
            String job,
            String interestCategory
    ) {
        this.id = id;
        this.memo = memo;
        this.isPublic = isPublic;
        this.isAnonymous = isAnonymous;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.likeCount = likeCount;
        this.imageUrl = imageUrl;
        this.commentCount = commentCount;
        this.displayName = displayName;
        this.job = job;
        this.interestCategory = interestCategory;
    }
    
}