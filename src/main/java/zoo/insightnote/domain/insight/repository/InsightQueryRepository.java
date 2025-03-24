package zoo.insightnote.domain.insight.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import zoo.insightnote.domain.insight.dto.InsightResponseDto;
import zoo.insightnote.domain.insight.entity.Insight;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;


public interface InsightQueryRepository {

    List<InsightResponseDto.InsightTopRes> findTopInsights();

//    Page<InsightResponseDto.InsightListQueryDto> findInsightsByEventDay(LocalDate eventDay, Pageable pageable);
    Page<InsightResponseDto.InsightListQueryDto> findInsightsByEventDay(LocalDate eventDay, Long sessionId, String sortCondition, Pageable pageable);

    Optional<InsightResponseDto.InsightDetailQueryDto> findByIdWithSessionAndUser(Long insightId);

    Page<InsightResponseDto.SessionInsightListQueryDto> findInsightsBySessionId(Long sessionId, String sortCondition, Pageable pageable , Long currentUserId);
}
