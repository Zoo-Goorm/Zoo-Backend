package zoo.insightnote.domain.session.dto.response.query;

public record SessionSpeakerDetailQuery(
        String sessionName,
        String longDescription,
        String location,
        Integer maxCapacity,
        Integer participantCount,
        String speakerName,
        String keywords,
        String careers,
        String imageUrl
) {}