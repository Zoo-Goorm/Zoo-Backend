package zoo.insightnote.domain.insight.dto.request;

public record InsightUpdateRequest(
        String memo,
        Boolean isPublic,
        Boolean isAnonymous,
        Boolean isDraft
) {}