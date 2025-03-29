package zoo.insightnote.domain.session.dto.response;

import java.util.List;

public record SessionTimeWithListGenericResponse<T>(
        String timeRange,
        List<T> sessions
) {}