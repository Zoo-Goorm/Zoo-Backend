package zoo.insightnote.domain.insight.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class MyInsightList {
    private Long insightId;
    private String memo;
    private Boolean isPublic;
    private Boolean isAnonymous;
    private Boolean isDraft;
    private LocalDateTime updatedAt;
    private Long sessionId;
    private String sessionName;
}