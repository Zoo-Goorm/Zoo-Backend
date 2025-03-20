package zoo.insightnote.domain.insight.repository;

import zoo.insightnote.domain.insight.dto.InsightResponseDto;
import zoo.insightnote.domain.insight.entity.Insight;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;


public interface InsightQueryRepository {

    List<InsightResponseDto.InsightTopRes> findTopInsights();

    List<InsightResponseDto.InsightByEventDayRes> findInsightsByEventDay(LocalDate eventDay, Integer offset, Integer limit);

    Optional<InsightResponseDto.InsightWithDetailsQueryDto> findByIdWithSessionAndUser(Long insightId);
}
