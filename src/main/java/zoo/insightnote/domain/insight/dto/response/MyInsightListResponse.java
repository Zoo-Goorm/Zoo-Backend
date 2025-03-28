package zoo.insightnote.domain.insight.dto.response;

import java.util.List;

public record MyInsightListResponse(
        boolean hasNext,
        long totalElements,
        int totalPages,
        int pageNumber,
        int pageSize,
        List<MyInsightList> content
) {}