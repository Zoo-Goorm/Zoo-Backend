package zoo.insightnote.domain.InsightLike.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import zoo.insightnote.domain.InsightLike.entity.InsightLike;
import zoo.insightnote.domain.insight.entity.Insight;
import zoo.insightnote.domain.user.entity.User;

import java.util.Optional;

public interface InsightLikeRepository extends JpaRepository<InsightLike, Long> {

    Optional<InsightLike> findByUserAndInsight(User user, Insight insight);

    // 더미데이터 중복 방지용
    boolean existsByUserAndInsight(User user, Insight insight);
}