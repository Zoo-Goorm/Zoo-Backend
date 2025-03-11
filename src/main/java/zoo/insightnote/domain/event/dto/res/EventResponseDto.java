package zoo.insightnote.domain.event.dto.res;

import zoo.insightnote.domain.event.entity.Event;
import java.time.LocalDate;

public record EventResponseDto(
        Long id,
        String name,
        String description,
        String location,
        LocalDate startTime,
        LocalDate endTime
) {
    public static EventResponseDto fromEntity(Event event) {
        return new EventResponseDto(
                event.getId(),
                event.getName(),
                event.getDescription(),
                event.getLocation(),
                event.getStartTime(),
                event.getEndTime()
        );
    }
}