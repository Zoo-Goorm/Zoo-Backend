package zoo.insightnote.domain.voteOption.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import zoo.insightnote.domain.insight.entity.Insight;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class VoteOption {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "insight_id", nullable = false)
    private Insight insight;

    @Column(nullable = false)
    private String optionText;

    @Builder
    public VoteOption(Insight insight, String optionText) {
        this.insight = insight;
        this.optionText = optionText;
    }
}