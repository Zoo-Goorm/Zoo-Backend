package zoo.insightnote.domain.event.dto.req;

import java.time.LocalDateTime;
import java.util.List;

public record EventUpdateRequestDto(
        String name,
        String description,
        String location,
        LocalDateTime startTime,
        LocalDateTime endTime,
        List<String> imageUrls
) {}