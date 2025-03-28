package zoo.insightnote.domain.session.dto.response;

import java.time.LocalDateTime;
import java.util.Set;


public record SessionDetailResponse(
        Long id,
        String name,
        String shortDescription,
        String location,
        LocalDateTime startTime,
        LocalDateTime endTime,
        String timeRange,
        Set<String> keywords
) {}