package zoo.insightnote.domain.insight.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import zoo.insightnote.domain.insight.entity.Insight;

public interface InsightRepository extends JpaRepository<Insight, Long> {
}
