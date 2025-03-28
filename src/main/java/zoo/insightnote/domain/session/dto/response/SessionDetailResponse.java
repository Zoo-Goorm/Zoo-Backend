package zoo.insightnote.domain.session.dto.response;


import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.Set;

@Getter
@Builder
public class SessionDetailResponse {
    private Long id;
    private String name;
    private String shortDescription;
    private String location;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String timeRange;
    private Set<String> keywords;
}
