package zoo.insightnote.domain.session.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.Map;


@Getter
@Builder
public class SessionTimeWithAllListResponse {
    private Map<String, List<SessionTimeWithListResponse>> sessionsByDay;
}
