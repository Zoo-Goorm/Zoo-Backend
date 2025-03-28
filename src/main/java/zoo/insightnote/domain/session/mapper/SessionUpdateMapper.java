package zoo.insightnote.domain.session.mapper;

import zoo.insightnote.domain.session.dto.response.SessionUpdateResponse;
import zoo.insightnote.domain.session.entity.Session;

import java.util.List;

public class SessionUpdateMapper {

    public static SessionUpdateResponse toResponse(Session session, List<String> keywords) {
        return SessionUpdateResponse.builder()
                .id(session.getId())
                .name(session.getName())
                .shortDescription(session.getShortDescription())
                .location(session.getLocation())
                .maxCapacity(session.getMaxCapacity())
                .startTime(session.getStartTime())
                .endTime(session.getEndTime())
                .speakerName(session.getSpeaker().getName())
                .keywords(keywords)
                .status(session.getStatus())
                .build();
    }
}
