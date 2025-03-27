package zoo.insightnote.domain.insight.dto.request;

import lombok.Getter;


@Getter
public class InsightUpdateRequest {
    private String memo;
    private Boolean isPublic;
    private Boolean isAnonymous;
    private Boolean isDraft;
}