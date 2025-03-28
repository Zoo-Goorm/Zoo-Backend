package zoo.insightnote.domain.session.dto.response.query;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SessionSpeakerDetailQuery {
    private String sessionName;
    private String longDescription;
    private String location;
    private Integer maxCapacity;
    private Integer participantCount;
    private String speakerName;
    private String keywords;
    private String careers;
    private String imageUrl;
}

