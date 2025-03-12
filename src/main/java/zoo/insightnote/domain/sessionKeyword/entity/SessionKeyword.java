package zoo.insightnote.domain.sessionKeyword.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Builder;
import lombok.NoArgsConstructor;
import zoo.insightnote.domain.keyword.entity.Keyword;
import zoo.insightnote.domain.session.entity.Session;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SessionKeyword {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "session_id")
    private Session session;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "keyword_id")
    private Keyword keyword;

    @Builder
    private SessionKeyword(Session session, Keyword keyword) {
        this.session = session;
        this.keyword = keyword;
    }

    // 빌더를 활용한 생성 메서드
    public static SessionKeyword create(Session session, Keyword keyword) {
        return SessionKeyword.builder()
                .session(session)
                .keyword(keyword)
                .build();
    }

}