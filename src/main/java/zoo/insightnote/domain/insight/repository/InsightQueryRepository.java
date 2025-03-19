package zoo.insightnote.domain.insight.repository;

import zoo.insightnote.domain.insight.entity.Insight;

import java.time.LocalDate;
import java.util.List;


public interface InsightQueryRepository {

    List<Insight> findTopInsights();

    List<Insight> findInsightsByEventDay(LocalDate eventDay, Integer offset, Integer limit);
}
