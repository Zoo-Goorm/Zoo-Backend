package zoo.insightnote.domain.insight.dto.response;

import java.util.List;

public record SessionInsightListResponse(
        boolean hasNext,
        long totalElements,
        int totalPages,
        int pageNumber,
        int pageSize,
        List<SessionInsight> content
) {}