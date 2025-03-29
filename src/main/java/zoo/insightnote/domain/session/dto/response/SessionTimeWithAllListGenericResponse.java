package zoo.insightnote.domain.session.dto.response;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.List;
import java.util.Map;

public record SessionTimeWithAllListGenericResponse<T>(
        Map<String, List<SessionTimeWithListGenericResponse<T>>> map
) {
    @JsonValue
    public Map<String, List<SessionTimeWithListGenericResponse<T>>> unwrap() {
        return map;
    }
}