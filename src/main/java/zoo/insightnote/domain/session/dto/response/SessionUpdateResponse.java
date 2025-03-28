package zoo.insightnote.domain.session.dto.response;

import lombok.Builder;
import lombok.Getter;
import zoo.insightnote.domain.session.entity.SessionStatus;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
public class SessionUpdateResponse {
    private Long id;
    private String name;
    private String shortDescription;
    private String location;
    private Integer maxCapacity;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String speakerName;
    private List<String> keywords;
    private SessionStatus status;
}
