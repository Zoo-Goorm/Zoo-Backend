package zoo.insightnote.domain.insight.dto.response;

import lombok.Builder;
import lombok.Getter;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
public class InsightChangeCategory {
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
    private List<String> interestCategory;
    private Long commentCount;
    private String displayName;
    private String job;
    private Boolean isLiked;
}