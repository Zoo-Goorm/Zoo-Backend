package zoo.insightnote.domain.session.dto.response;

import zoo.insightnote.domain.session.entity.SessionStatus;

import java.time.LocalDateTime;
import java.util.List;

public record SessionCreateResponse(
        Long id,
        String name,
        String shortDescription,
        String location,
        Integer maxCapacity,
        LocalDateTime startTime,
        LocalDateTime endTime,
        String speakerName,
        List<String> keywords,
        SessionStatus status
) {}