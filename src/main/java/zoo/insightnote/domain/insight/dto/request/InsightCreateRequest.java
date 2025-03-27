package zoo.insightnote.domain.insight.dto.request;

import lombok.Getter;

@Getter
public class InsightCreateRequest {
    private Long sessionId;
    private String memo;
    private Boolean isPublic;
    private Boolean isAnonymous;
    private Boolean isDraft;
}