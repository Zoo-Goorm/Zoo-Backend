package zoo.insightnote.domain.session.dto.response.query;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class SessionWithSpeakerDetailResponse {
    private String sessionName;
    private String longDescription;
    private String location;
    private Integer maxCapacity;
    private Integer participantCount;
    private String speakerName;
    private List<String> keywords;
    private List<String> careers;
    private String imageUrl;
}
