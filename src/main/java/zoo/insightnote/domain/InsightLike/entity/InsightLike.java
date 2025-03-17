package zoo.insightnote.domain.InsightLike.entity;

import jakarta.persistence.*;
import lombok.*;
import zoo.insightnote.domain.insight.entity.Insight;
import zoo.insightnote.domain.user.entity.User;
import zoo.insightnote.global.entity.BaseTimeEntity;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class InsightLike {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "insight_id", nullable = false)
    private Insight insight;

    @Builder
    public static InsightLike of(User user, Insight insight) {
        return InsightLike.builder()
                .user(user)
                .insight(insight)
                .build();
    }
}