package zoo.insightnote.domain.reservation.dto.response;


import java.util.List;
import java.util.Map;

public record UserTicketInfoResponse (
        Long eventId,
        Map<String, Boolean> tickets,
        Map<String, List<ReservationSessions>> registeredSessions
) {}


