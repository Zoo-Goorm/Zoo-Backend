package zoo.insightnote.domain.session.service;

import com.querydsl.core.Tuple;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import zoo.insightnote.domain.event.entity.Event;
import zoo.insightnote.domain.event.service.EventService;
import zoo.insightnote.domain.image.dto.ImageRequest;
import zoo.insightnote.domain.image.entity.EntityType;
import zoo.insightnote.domain.keyword.entity.Keyword;
import zoo.insightnote.domain.keyword.service.KeywordService;
import zoo.insightnote.domain.session.dto.request.SessionCreateRequest;
import zoo.insightnote.domain.session.dto.request.SessionUpdateRequest;
import zoo.insightnote.domain.session.dto.response.*;
import zoo.insightnote.domain.session.dto.response.query.*;
import zoo.insightnote.domain.session.entity.Session;
import zoo.insightnote.domain.session.mapper.*;
import zoo.insightnote.domain.session.repository.SessionCustomQueryRepository;
import zoo.insightnote.domain.session.repository.SessionRepository;
import zoo.insightnote.domain.sessionKeyword.service.SessionKeywordService;
import zoo.insightnote.domain.speaker.entity.Speaker;
import zoo.insightnote.domain.speaker.service.SpeakerService;
import zoo.insightnote.domain.image.service.ImageService;
import zoo.insightnote.global.exception.CustomException;
import zoo.insightnote.global.exception.ErrorCode;

import java.util.*;

@Service
@RequiredArgsConstructor
public class SessionService {

    private final SessionRepository sessionRepository;
    private final SessionCustomQueryRepository sessionQueryRepository;
    private final EventService eventService;
    private final SpeakerService speakerService;
    private final ImageService imageService;
    private final SessionKeywordService sessionKeywordService;
    private final KeywordService keywordService;

    @Transactional
    public SessionCreateResponse createSession(SessionCreateRequest request) {

        Event event = eventService.findById(request.eventId());
        Speaker speaker = speakerService.findById(request.speakerId());

        Session session = SessionCreateMapper.toEntity(request, event, speaker);
        Session savedSession = sessionRepository.save(session);

        imageService.saveImages(savedSession.getId(), EntityType.SESSION, request.images());

        List<Keyword> keywords = request.keywords().stream()
                .map(keywordService::findOrCreateByName)
                .toList();
        sessionKeywordService.saveSessionKeywords(savedSession, keywords);

        return SessionCreateMapper.of(session, request.keywords());
    }

    @Transactional
    public SessionUpdateResponse updateSession(Long sessionId, SessionUpdateRequest request) {
        Session session = sessionRepository.findById(sessionId)
                .orElseThrow(() -> new CustomException(ErrorCode.SESSION_NOT_FOUND));
        session.update(request);

        List<Keyword> newKeywords = request.keywords().stream()
                .map(keywordService::findOrCreateByName)
                .toList();
        sessionKeywordService.updateSessionKeywords(session, newKeywords);
        imageService.updateImages(new ImageRequest.UploadImages(session.getId(), EntityType.SESSION, request.images()));

        return SessionUpdateMapper.toResponse(session, request.keywords());
    }

    @Transactional
    public void deleteSession(Long sessionId) {
        Session session = sessionRepository.findById(sessionId)
                .orElseThrow(() -> new CustomException(ErrorCode.SESSION_NOT_FOUND));

        sessionKeywordService.deleteSessionKeywords(session);

        imageService.deleteImagesByEntity(session.getId(), EntityType.SESSION);

        sessionRepository.delete(session);
    }


    // 세션 목록 일반 페이지 (이미지 제외)
    @Transactional(readOnly = true)
    public SessionTimeWithAllListGenericResponse<SessionDetailResponse> getAllSessions() {
        List<Tuple> results = sessionQueryRepository.findAllSessionsWithKeywords();
        return SessionListMapper.toResponse(results);
    }

    // 2. 세션 목록 상세 조회 (연사 이미지, 인원수 포함, 키워드 포함)
    @Transactional(readOnly = true)
    public SessionTimeWithAllListGenericResponse<SessionDetaileWithImageAndCountResponse> getAllSessionsWithDetails() {
        List<Tuple> results = sessionQueryRepository.findAllSessionsWithDetails(EntityType.SPEAKER);
        return SessionListWithImageMapper.toResponse(results);
    }


    @Transactional(readOnly = true)
    public SessionWithSpeakerDetailResponse getSessionDetails(Long sessionId) {

        SessionSpeakerDetailQuery result = sessionQueryRepository.findSessionAndSpeakerDetail(sessionId);
        if (result == null) {
            throw new CustomException(ErrorCode.SESSION_NOT_FOUND);
        }

        return SessionWithSpeakerMapper.toResponse(result);
    }


    public Session findSessionBySessionId(Long sessionId) {
        return sessionRepository.findById(sessionId)
                .orElseThrow(() -> new CustomException(ErrorCode.SESSION_NOT_FOUND));
    }

}