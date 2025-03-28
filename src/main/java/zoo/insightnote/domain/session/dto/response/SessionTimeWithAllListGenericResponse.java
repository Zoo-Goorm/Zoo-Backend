package zoo.insightnote.domain.session.dto.response;
import java.util.List;
import java.util.Map;

public record SessionTimeWithAllListGenericResponse<T>(
        Map<String, List<SessionTimeWithListGenericResponse<T>>> sessionsByDay
) {}