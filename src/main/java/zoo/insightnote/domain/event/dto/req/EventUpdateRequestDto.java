package zoo.insightnote.domain.event.dto.req;

import java.time.LocalDateTime;

public record EventUpdateRequestDto(
        String name,
        String description,
        String location,
        LocalDateTime startTime,
        LocalDateTime endTime
) {}