package zoo.insightnote.domain.session.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class SessionTimeWithListGenericResponse<T> {
    private String timeRange;
    private List<T> sessions;
}
