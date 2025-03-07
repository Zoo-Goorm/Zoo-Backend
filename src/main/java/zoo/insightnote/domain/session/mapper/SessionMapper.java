package zoo.insightnote.domain.session.mapper;

import zoo.insightnote.domain.event.entity.Event;
import zoo.insightnote.domain.session.dto.SessionRequest;
import zoo.insightnote.domain.session.dto.SessionResponse;
import zoo.insightnote.domain.session.entity.Session;
import zoo.insightnote.domain.speaker.entity.Speaker;

public class SessionMapper {

    public static Session toEntity(SessionRequest.Create request, Event event, Speaker speaker) {
        return Session.builder()
                .event(event)
                .speaker(speaker)
                .eventDay(request.eventDay())
                .name(request.name())
                .shortDescription(request.shortDescription())
                .longDescription(request.longDescription())
                .maxCapacity(request.maxCapacity())
                .startTime(request.startTime())
                .endTime(request.endTime())
                .status(request.status())
                .videoLink(request.videoLink())
                .location(request.location())
                .build();
    }

    // Session엔티티 객체를 dto에서 정의한 record르 변환
    public static SessionResponse.Default toResponse(Session session) {
        return new SessionResponse.Default(
                session.getId(),
                session.getName(),
                session.getShortDescription(),
                session.getLongDescription(),
                session.getMaxCapacity(),
                session.getStartTime(),
                session.getEndTime(),
                session.getLocation(),
                session.getStatus()
        );
    }
}
