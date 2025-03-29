package zoo.insightnote.domain.insight.dto.response.query;

import java.time.LocalDateTime;

public record MyInsightListQuery(
        Long insightId,
        String memo,
        Boolean isPublic,
        Boolean isAnonymous,
        Boolean isDraft,
        LocalDateTime updatedAt,
        Long sessionId,
        String sessionName
) {}