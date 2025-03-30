package zoo.insightnote.domain.reservation.mapper;

import zoo.insightnote.domain.reservation.dto.response.ReservationSessions;
import zoo.insightnote.domain.reservation.dto.response.UserTicketInfoResponse;

import java.util.List;
import java.util.Map;

public class UserTicketInfoMapper {
    public static ReservationSessions toBuildReservationSessions(Long sessionId, String sessionName, String speakerName, String timeRange) {
        return new ReservationSessions(sessionId, sessionName, speakerName, timeRange);
    }

    public static UserTicketInfoResponse toBuildUserTicketInfoResponse(
            Long eventId,
            Map<String, Boolean> tickets,
            Map<String, List<ReservationSessions>> registeredSessions
    ) {
        return new UserTicketInfoResponse(eventId, tickets, registeredSessions);
    }
}
