package zoo.insightnote.domain.session.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import zoo.insightnote.domain.event.entity.Event;
import zoo.insightnote.domain.event.service.EventService;
import zoo.insightnote.domain.image.dto.ImageRequest;
import zoo.insightnote.domain.image.entity.EntityType;
import zoo.insightnote.domain.session.dto.SessionRequest;
import zoo.insightnote.domain.session.dto.SessionResponse;
import zoo.insightnote.domain.session.entity.Session;
import zoo.insightnote.domain.session.mapper.SessionMapper;
import zoo.insightnote.domain.session.repository.SessionRepository;
import zoo.insightnote.domain.speaker.entity.Speaker;
import zoo.insightnote.domain.speaker.service.SpeakerService;
import zoo.insightnote.domain.image.service.ImageService;
import zoo.insightnote.global.exception.CustomException;
import zoo.insightnote.global.exception.ErrorCode;

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

    @Transactional
    public SessionResponse.Default updateSession(Long sessionId, SessionRequest.Update request) {
        Session session = sessionRepository.findById(sessionId)
                .orElseThrow(() -> new CustomException(ErrorCode.SESSION_NOT_FOUND));
        session.update(request);

        imageService.updateImages(new ImageRequest.UploadImages(
                session.getId(),
                EntityType.SESSION,
                request.images()
        ));

        return SessionMapper.toResponse(session);
    }

    @Transactional
    public void deleteSession(Long sessionId) {
        Session session = sessionRepository.findById(sessionId)
                .orElseThrow(() -> new CustomException(ErrorCode.SESSION_NOT_FOUND));
        
        imageService.deleteImagesByEntity(session.getId(), EntityType.SESSION);

        sessionRepository.delete(session);
    }

    @Transactional(readOnly = true)
    public SessionResponse.Default getSessionById(Long sessionId) {
        Session session = sessionRepository.findById(sessionId)
                .orElseThrow(() -> new CustomException(ErrorCode.SESSION_NOT_FOUND));

        return SessionMapper.toResponse(session);
    }
}