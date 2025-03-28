package zoo.insightnote.domain.insight.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class SessionInsightListResponse {
    private boolean hasNext;
    private long totalElements;
    private int totalPages;
    private int pageNumber;
    private int pageSize;
    private List<SessionInsight> content;
}