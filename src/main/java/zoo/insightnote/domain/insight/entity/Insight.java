package zoo.insightnote.domain.insight.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import zoo.insightnote.domain.session.entity.Session;
import zoo.insightnote.global.entity.BaseTimeEntity;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Insight extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "session_id")
    private Session session;

    @Column(columnDefinition = "TEXT")
    private String memo;

    @Column(nullable = false)
    private Boolean isPublic;

    public static Insight create(Session session, String memo, Boolean isPublic) {
        return Insight.builder()
                .session(session)
                .memo(memo)
                .isPublic(isPublic != null ? isPublic : true)
                .build();
    }

    public void changeIsPublic(boolean isPublic) {
        this.isPublic = isPublic;
    }
}
