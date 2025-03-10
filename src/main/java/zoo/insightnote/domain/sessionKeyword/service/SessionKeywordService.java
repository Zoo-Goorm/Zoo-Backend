package zoo.insightnote.domain.sessionKeyword.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import zoo.insightnote.domain.keyword.entity.Keyword;
import zoo.insightnote.domain.keyword.service.KeywordService;
import zoo.insightnote.domain.session.entity.Session;
import zoo.insightnote.domain.sessionKeyword.entity.SessionKeyword;
import zoo.insightnote.domain.sessionKeyword.repository.SessionKeywordRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SessionKeywordService {

    private final SessionKeywordRepository sessionKeywordRepository;

    // 세션과 키워드 연결 저장
    @Transactional
    public void saveSessionKeywords(Session session, List<Keyword> keywords) {
        List<SessionKeyword> sessionKeywords = keywords.stream()
                .map(keyword -> SessionKeyword.create(session, keyword))
                .toList();
        sessionKeywordRepository.saveAll(sessionKeywords);
    }

    // 기존 키워드 조회
    @Transactional(readOnly = true)
    public List<String> getKeywordsBySession(Session session) {
        return sessionKeywordRepository.findBySession(session).stream()
                .map(sk -> sk.getKeyword().getName())
                .toList();
    }

    // 키워드 업데이트 (비교 후 변경)
    @Transactional
    public void updateSessionKeywords(Session session, List<Keyword> newKeywords) {
        // 기존 키워드 조회
        List<Keyword> existingKeywords = sessionKeywordRepository.findBySession(session).stream()
                .map(SessionKeyword::getKeyword)
                .toList();

        // 새로 추가해야 할 키워드
        List<Keyword> keywordsToAdd = newKeywords.stream()
                .filter(newKeyword -> !existingKeywords.contains(newKeyword))
                .toList();

        // 제거해야 할 키워드
        List<Keyword> keywordsToRemove = existingKeywords.stream()
                .filter(existingKeyword -> !newKeywords.contains(existingKeyword))
                .toList();

        // 추가
        saveSessionKeywords(session, keywordsToAdd);

        // 삭제
        sessionKeywordRepository.deleteBySessionAndKeywordIn(session, keywordsToRemove);
    }

    @Transactional
    public void deleteSessionKeywords(Session session) {
        sessionKeywordRepository.deleteBySession(session);
    }

}