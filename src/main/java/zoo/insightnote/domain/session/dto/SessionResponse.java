package zoo.insightnote.domain.session.dto;

import zoo.insightnote.domain.session.entity.SessionStatus;

import java.time.LocalDateTime;

public interface SessionResponse {
    record Default(
            Long id,
            String name,
            String shortDescription,
            String longDescription,
            Integer maxCapacity,
            LocalDateTime startTime,
            LocalDateTime endTime,
            String location,
            SessionStatus status
    ) implements SessionResponse {
    }
}