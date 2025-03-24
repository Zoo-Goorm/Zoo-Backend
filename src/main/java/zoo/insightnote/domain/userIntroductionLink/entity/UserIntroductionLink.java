package zoo.insightnote.domain.userIntroductionLink.entity;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import zoo.insightnote.domain.user.entity.User;

@Entity
@Getter
@NoArgsConstructor
public class UserIntroductionLink {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 소개 링크 URL 또는 다른 관련 정보를 저장할 필드
    private String linkUrl;

    // 단방향 N:1 매핑 - 여러 개의 IntroductionLink가 하나의 User를 참조합니다.
    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Builder
    public UserIntroductionLink(String linkUrl, User user) {
        this.linkUrl = linkUrl;
        this.user = user;
    }
}