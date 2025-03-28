package zoo.insightnote.domain.insight.dto.response;

import java.time.LocalDateTime;

public record MyInsightList(
        Long insightId,
        String memo,
        Boolean isPublic,
        Boolean isAnonymous,
        Boolean isDraft,
        LocalDateTime updatedAt,
        Long sessionId,
        String sessionName
) {}