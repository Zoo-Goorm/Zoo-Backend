package zoo.insightnote.domain.session.mapper;

import zoo.insightnote.domain.event.entity.Event;
import zoo.insightnote.domain.session.dto.request.SessionCreateRequest;
import zoo.insightnote.domain.session.dto.response.SessionCreateResponse;
import zoo.insightnote.domain.session.entity.Session;
import zoo.insightnote.domain.speaker.entity.Speaker;

import java.util.List;

public class SessionCreateMapper {

    public static Session toEntity(SessionCreateRequest request, Event event, Speaker speaker) {
        return Session.create(request, event, speaker);
    }


    public static SessionCreateResponse of(Session session, List<String> keywords) {
        return new SessionCreateResponse(
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
