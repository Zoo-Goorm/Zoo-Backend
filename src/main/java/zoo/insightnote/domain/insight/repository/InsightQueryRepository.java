package zoo.insightnote.domain.insight.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import zoo.insightnote.domain.insight.dto.InsightResponseDto;
import zoo.insightnote.domain.insight.dto.response.query.InsightDetailQuery;
import zoo.insightnote.domain.insight.dto.response.query.InsightListQuery;
import zoo.insightnote.domain.insight.dto.response.query.InsightTopListQuery;
import zoo.insightnote.domain.insight.entity.Insight;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;


public interface InsightQueryRepository {

    List<InsightTopListQuery> findTopInsights(Long userId);

//    Page<InsightResponseDto.InsightListQueryDto> findInsightsByEventDay(LocalDate eventDay, Pageable pageable);
    Page<InsightListQuery> findInsightsByEventDay(LocalDate eventDay, Long sessionId, String sortCondition, Pageable pageable , Long userId);

    Optional<InsightDetailQuery> findByIdWithSessionAndUser(Long insightId, Long userId);

    Page<InsightResponseDto.SessionInsightListQueryDto> findInsightsBySessionId(Long sessionId, String sortCondition, Pageable pageable , Long currentUserId);

    Page<InsightResponseDto.MyInsightListQueryDto> findMyInsights(String username, LocalDate eventDay, Long sessionId, Pageable pageable
    );
}
