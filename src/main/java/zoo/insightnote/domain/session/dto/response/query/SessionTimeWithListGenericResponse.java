package zoo.insightnote.domain.session.dto.response.query;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class SessionTimeWithListGenericResponse<T> {
    private String timeRange;
    private List<T> sessions;
}
