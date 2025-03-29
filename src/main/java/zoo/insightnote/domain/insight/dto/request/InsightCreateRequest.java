package zoo.insightnote.domain.insight.dto.request;

public record InsightCreateRequest(
        Long sessionId,
        String memo,
        Boolean isPublic,
        Boolean isAnonymous,
        Boolean isDraft
) {}