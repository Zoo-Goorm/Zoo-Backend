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
import zoo.insightnote.domain.session.entity.SessionStatus;
import zoo.insightnote.domain.session.mapper.SessionMapper;
import zoo.insightnote.domain.session.repository.SessionRepository;
import zoo.insightnote.domain.sessionKeyword.repository.SessionKeywordRepository;
import zoo.insightnote.domain.sessionKeyword.service.SessionKeywordService;
import zoo.insightnote.domain.speaker.entity.Speaker;
import zoo.insightnote.domain.speaker.service.SpeakerService;
import zoo.insightnote.domain.image.service.ImageService;
import zoo.insightnote.global.exception.CustomException;
import zoo.insightnote.global.exception.ErrorCode;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class SessionService {

    private final SessionRepository sessionRepository;
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

        return SessionMapper.toResponse(session, request.getKeywords());
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

        imageService.updateImages(new ImageRequest.UploadImages(
                session.getId(),
                EntityType.SESSION,
                request.getImages()
        ));

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

    // 세션 단일 조회
    @Transactional(readOnly = true)
    public SessionResponseDto.SessionRes getSessionById(Long sessionId) {
        Session session = sessionRepository.findById(sessionId)
                .orElseThrow(() -> new CustomException(ErrorCode.SESSION_NOT_FOUND));

        List<String> keywords = sessionKeywordService.getKeywordsBySession(session);

        return SessionMapper.toResponse(session, keywords);
    }


    // 1. 세션 전체 조회 (연사 이미지 제외, 인원수 제외, 키워드 포함)
    @Transactional(readOnly = true)
    public List<SessionResponseDto.SessionAllRes> getAllSessions() {
        List<Object[]> results = sessionRepository.findAllSessions();

        // 세션 ID별로 키워드 매핑
        Map<Long, SessionResponseDto.SessionAllRes> sessionMap = new LinkedHashMap<>();

        for (Object[] row : results) {
            Long sessionId = (Long) row[0];
            String keyword = (String) row[2];

            sessionMap.computeIfAbsent(sessionId, id -> SessionResponseDto.SessionAllRes.builder()
                    .id(sessionId)
                    .name((String) row[1])
                    .shortDescription((String) row[3])
                    .location((String) row[4])
                    .startTime((LocalDateTime) row[5])
                    .endTime((LocalDateTime) row[6])
                    .keywords(new ArrayList<>())
                    .build());

            if (keyword != null && !sessionMap.get(sessionId).getKeywords().contains(keyword)) {
                sessionMap.get(sessionId).getKeywords().add(keyword);
            }
        }

        return new ArrayList<>(sessionMap.values());
    }

    // 2. 세션 상세 조회 (연사 이미지, 인원수 포함, 키워드 포함)
    @Transactional(readOnly = true)
    public List<SessionResponseDto.SessionDetailedRes> getAllSessionsWithDetails() {
        List<Object[]> results = sessionRepository.findAllSessionsWithDetails(EntityType.SPEAKER);

        // 세션 ID별로 데이터 매핑
        Map<Long, SessionResponseDto.SessionDetailedRes> sessionMap = new LinkedHashMap<>();

        for (Object[] row : results) {
            Long sessionId = (Long) row[0];
            String keyword = (String) row[2];


            sessionMap.computeIfAbsent(sessionId, id -> SessionResponseDto.SessionDetailedRes.builder()
                    .id(sessionId)
                    .name((String) row[1])
                    .shortDescription((String) row[3])
                    .maxCapacity((Integer) row[4])
                    .participantCount((Integer) row[5])
                    .location((String) row[6])
                    .speakerName((String) row[7])
                    .speakerImageUrl((String) row[8])
                    .startTime((LocalDateTime) row[9])
                    .endTime((LocalDateTime) row[10])
                    .status((SessionStatus) row[11])
                    .keywords(new ArrayList<>())
                    .build());

            if (keyword != null && !sessionMap.get(sessionId).getKeywords().contains(keyword)) {
                sessionMap.get(sessionId).getKeywords().add(keyword);
            }
        }

        return new ArrayList<>(sessionMap.values());
    }

}