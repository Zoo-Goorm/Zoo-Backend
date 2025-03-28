package zoo.insightnote.domain.insight.dto.response;

import java.util.List;

public record InsightListResponse(
        boolean hasNext,
        long totalElements,
        int totalPages,
        int pageNumber,
        int pageSize,
        List<InsightChangeCategory> content
) {}

