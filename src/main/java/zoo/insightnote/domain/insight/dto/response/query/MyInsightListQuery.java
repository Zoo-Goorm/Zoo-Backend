package zoo.insightnote.domain.insight.dto.response.query;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class MyInsightListQuery {
    private Long insightId;
    private String memo;
    private Boolean isPublic;
    private Boolean isAnonymous;
    private Boolean isDraft;
    private LocalDateTime updatedAt;
    private Long sessionId;
    private String sessionName;
}
