package zoo.insightnote.domain.session.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import zoo.insightnote.domain.event.entity.Event;
import zoo.insightnote.domain.event.service.EventService;
import zoo.insightnote.domain.image.entity.EntityType;
import zoo.insightnote.domain.session.dto.SessionRequest;
import zoo.insightnote.domain.session.dto.SessionResponse;
import zoo.insightnote.domain.session.entity.Session;
import zoo.insightnote.domain.session.mapper.SessionMapper;
import zoo.insightnote.domain.session.repository.SessionRepository;
import zoo.insightnote.domain.speaker.entity.Speaker;
import zoo.insightnote.domain.speaker.service.SpeakerService;
import zoo.insightnote.domain.image.service.ImageService;

@Service
@RequiredArgsConstructor
public class SessionService {

    private final SessionRepository sessionRepository;
    private final EventService eventService;
    private final SpeakerService speakerService;
    private final ImageService imageService;

    @Transactional
    public SessionResponse.Default createSession(SessionRequest.Create request) {

        Event event = eventService.findById(request.eventId());

        Speaker speaker = speakerService.findById(request.speakerId());

        Session session = SessionMapper.toEntity(request, event, speaker);

        sessionRepository.save(session);

        imageService.saveImages(session.getId(), EntityType.SESSION, request.images());

        return SessionMapper.toResponse(session);
    }

}