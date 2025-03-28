package zoo.insightnote.domain.session.mapper;

import zoo.insightnote.domain.event.entity.Event;
import zoo.insightnote.domain.session.dto.SessionRequestDto;
import zoo.insightnote.domain.session.dto.SessionResponseDto;
import zoo.insightnote.domain.session.dto.request.SessionCreateRequest;
import zoo.insightnote.domain.session.dto.response.SessionCreateResponse;
import zoo.insightnote.domain.session.entity.Session;
import zoo.insightnote.domain.speaker.entity.Speaker;

import java.util.List;

public class SessionCreateMapper {

    public static Session toEntity(SessionCreateRequest request, Event event, Speaker speaker) {
        return Session.create(request, event, speaker);
    }


    public static SessionCreateResponse toResponse(Session session, List<String> keywords) {
        return SessionCreateResponse.builder()
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
