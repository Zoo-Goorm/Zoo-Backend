package zoo.insightnote.domain.session.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.Set;

@Getter
@Builder
public class SessionDetaileWithImageAndCountResponse {
    private Long id;
    private String name;
    private String shortDescription;
    private String location;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String timeRange;
    private Integer participantCount;
    private Integer maxCapacity;
    private Set<String> keywords;
    private String speakerImageUrl; // 연사 이미지
    private String speakerName;
}
