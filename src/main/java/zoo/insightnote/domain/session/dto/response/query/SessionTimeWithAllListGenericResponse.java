package zoo.insightnote.domain.session.dto.response.query;

import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.Map;

@Getter
@Builder
public class SessionTimeWithAllListGenericResponse<T> {
    private Map<String, List<SessionTimeWithListGenericResponse<T>>> sessionsByDay;
}