package zoo.insightnote.domain.session.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class SessionTimeWithListResponse {
    private String timeRange;
    private List<SessionDetailResponse> sessions;
}
