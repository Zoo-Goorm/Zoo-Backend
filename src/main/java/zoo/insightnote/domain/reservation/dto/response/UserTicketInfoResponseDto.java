package zoo.insightnote.domain.reservation.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserTicketInfoResponseDto {
    private Long eventId;
    private Map<String, Boolean> tickets;
    private Map<String, List<reservationSessions>> registeredSessions;

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class reservationSessions {
        private Long sessionId;
        private String sessionName;
        private String speakerName;
        private String timeRange;
    }
}
