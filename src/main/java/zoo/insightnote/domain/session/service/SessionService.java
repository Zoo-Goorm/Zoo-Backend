package zoo.insightnote.domain.session.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import zoo.insightnote.domain.event.entity.Event;
import zoo.insightnote.domain.event.service.EventService;
import zoo.insightnote.domain.image.dto.ImageRequest;
import zoo.insightnote.domain.image.entity.EntityType;
import zoo.insightnote.domain.keyword.entity.Keyword;
import zoo.insightnote.domain.keyword.service.KeywordService;
import zoo.insightnote.domain.session.dto.SessionRequestDto;
import zoo.insightnote.domain.session.dto.SessionResponseDto;
import zoo.insightnote.domain.session.entity.Session;
import zoo.insightnote.domain.session.mapper.SessionMapper;
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
    public SessionResponseDto.SessionRes createSession(SessionRequestDto.Create request) {

        Event event = eventService.findById(request.getEventId());

        Speaker speaker = speakerService.findById(request.getSpeakerId());

        Session session = SessionMapper.toEntity(request, event, speaker);

        Session savedSession = sessionRepository.save(session);

        imageService.saveImages(savedSession.getId(), EntityType.SESSION, request.getImages());

        List<Keyword> keywords = request.getKeywords().stream()
                .map(keywordService::findOrCreateByName)
                .toList();
        sessionKeywordService.saveSessionKeywords(savedSession, keywords);

        return SessionMapper.  toResponse(session, request.getKeywords());
    }


    // 세션 업데이트
    @Transactional
    public SessionResponseDto.SessionRes updateSession(Long sessionId, SessionRequestDto.Update request) {
        Session session = sessionRepository.findById(sessionId)
                .orElseThrow(() -> new CustomException(ErrorCode.SESSION_NOT_FOUND));
        session.update(request);

        // 세션 키워드 업데이트 로직 필요함
        List<Keyword> newKeywords = request.getKeywords().stream()
                .map(keywordService::findOrCreateByName)
                .toList();
        sessionKeywordService.updateSessionKeywords(session, newKeywords);

        imageService.updateImages(new ImageRequest.UploadImages(session.getId(), EntityType.SESSION, request.getImages()));

        return SessionMapper.toResponse(session, request.getKeywords());
    }

    // 세션 삭제
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
    public Map<String, List<SessionResponseDto.SessionAllRes>> getAllSessions() {
        return sessionQueryRepository.findAllSessionsWithKeywords();
    }


    // 2. 세션 목록 상세 조회 (연사 이미지, 인원수 포함, 키워드 포함)
    @Transactional(readOnly = true)
    public Map<String, List<SessionResponseDto.SessionDetailedRes>> getAllSessionsWithDetails() {
        return sessionQueryRepository.findAllSessionsWithDetails(EntityType.SPEAKER);
    }

    @Transactional(readOnly = true)
    public SessionResponseDto.SessionSpeakerDetailRes getSessionDetails(Long sessionId) {
        SessionResponseDto.SessionSpeakerDetailQueryDto result = sessionQueryRepository.findSessionAndSpeakerDetail(sessionId);

        if (result == null) {
            throw new CustomException(ErrorCode.SESSION_NOT_FOUND); // 커스텀 예외로 처리
        }

        return SessionMapper.toSessionSpeakerDetailRes(result);
    }

    public Session findSessionBySessionId(Long sessionId) {
        return sessionRepository.findById(sessionId)
                .orElseThrow(() -> new CustomException(ErrorCode.SESSION_NOT_FOUND));
    }

    public void validateSessionTime(List<Long> sessionIds) {
        List<Session> sessions = sessionIds.stream()
                .map(this::findSessionBySessionId)
                .toList();


        for (int i = 0; i < sessions.size(); i++) {
            for (int j = i + 1; j < sessions.size(); j++) {
                Session s1 = sessions.get(i);
                Session s2 = sessions.get(j);

                boolean overlap = s1.getStartTime().isBefore(s2.getEndTime()) &&
                        s2.getStartTime().isBefore(s1.getEndTime());

                if (overlap) {
                    throw new CustomException(ErrorCode.DUPLICATE_SESSION_TIME);
                }
            }
        }
    }

    public void validationParticipantCountOver(List<Long> sessionIds) {
        for (Long sessionId : sessionIds) {
            Session session = findSessionBySessionId(sessionId);

            if (session.getMaxCapacity() <= session.getParticipantCount()) {
                throw new CustomException(ErrorCode.SESSION_CAPACITY_EXCEEDED);
            }
        }
    }
}