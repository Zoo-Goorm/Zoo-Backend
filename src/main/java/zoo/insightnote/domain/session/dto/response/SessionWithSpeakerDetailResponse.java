package zoo.insightnote.domain.session.dto.response;

import java.util.List;

public record SessionWithSpeakerDetailResponse(
        String sessionName,
        String longDescription,
        String location,
        Integer maxCapacity,
        Integer participantCount,
        String speakerName,
        List<String> keywords,
        List<String> careers,
        String imageUrl
) {}