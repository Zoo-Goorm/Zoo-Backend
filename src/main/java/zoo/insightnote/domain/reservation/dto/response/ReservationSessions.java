package zoo.insightnote.domain.reservation.dto.response;

public record ReservationSessions (
        Long sessionId,
        String sessionName,
        String speakerName,
        String timeRange
) {}
