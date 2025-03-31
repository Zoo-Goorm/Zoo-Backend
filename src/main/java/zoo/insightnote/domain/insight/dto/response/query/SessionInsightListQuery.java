package zoo.insightnote.domain.insight.dto.response.query;

import lombok.Getter;
import lombok.Setter;
import zoo.insightnote.domain.insight.dto.response.UserProfileResponse;

import java.time.LocalDateTime;
import java.util.List;

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
    private String name;
    private String email;
    private String interestCategory;
    private String introductionLinks;

    @Setter
    private Boolean isLiked;

    @Setter
    private Boolean hasSpeakerComment;


    public SessionInsightListQuery(
            Long id, String memo, Boolean isPublic, Boolean isAnonymous,
            LocalDateTime createdAt, LocalDateTime updatedAt,
            Long likeCount, Long commentCount, String displayName, String job,
            String name, String email, String interestCategory, String introductionLinks
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
        this.name = name;
        this.email = email;
        this.interestCategory = interestCategory;
        this.introductionLinks = introductionLinks;
    }
}