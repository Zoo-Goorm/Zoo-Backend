package zoo.insightnote.domain.voteResponse.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import zoo.insightnote.domain.user.entity.User;
import zoo.insightnote.domain.voteOption.entity.VoteOption;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class VoteResponse {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vote_option_id", nullable = false)
    private VoteOption voteOption;

    @Column(nullable = false)
    private LocalDateTime votedAt;

    @Builder
    public VoteResponse(User user, VoteOption voteOption) {
        this.user = user;
        this.voteOption = voteOption;
        this.votedAt = LocalDateTime.now();
    }
}