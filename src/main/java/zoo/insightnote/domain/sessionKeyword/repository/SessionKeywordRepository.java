package zoo.insightnote.domain.sessionKeyword.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import zoo.insightnote.domain.session.entity.Session;
import zoo.insightnote.domain.sessionKeyword.entity.SessionKeyword;

import java.util.List;

public interface SessionKeywordRepository extends JpaRepository<SessionKeyword, Long> {

    // 세션에 해당하는 키워드 삭제
    void deleteBySession(Session session);

    // 세션에 해당하는 키워드 목록 조회
    List<SessionKeyword> findBySession(Session session);
}