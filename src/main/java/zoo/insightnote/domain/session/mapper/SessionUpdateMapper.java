package zoo.insightnote.domain.session.mapper;

import zoo.insightnote.domain.session.dto.response.SessionUpdateResponse;
import zoo.insightnote.domain.session.entity.Session;

import java.util.List;

public class SessionUpdateMapper {

    public static SessionUpdateResponse toResponse(Session session, List<String> keywords) {
        return new SessionUpdateResponse(
                session.getId(),
                session.getName(),
                session.getShortDescription(),
                session.getLocation(),
                session.getMaxCapacity(),
                session.getStartTime(),
                session.getEndTime(),
                session.getSpeaker().getName(),
                keywords,
                session.getStatus()
        );
    }
}
