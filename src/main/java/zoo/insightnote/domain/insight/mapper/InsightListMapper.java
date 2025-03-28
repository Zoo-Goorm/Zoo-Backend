package zoo.insightnote.domain.insight.mapper;

import org.springframework.data.domain.Page;
import zoo.insightnote.domain.insight.dto.response.InsightChangeCategory;
import zoo.insightnote.domain.insight.dto.response.InsightListResponse;
import zoo.insightnote.domain.insight.dto.response.query.InsightListQuery;

import java.util.List;
import java.util.stream.Collectors;

public class InsightListMapper {

    public static InsightChangeCategory toBuildInsight(InsightListQuery insightDto) {
        return new InsightChangeCategory(
                insightDto.getId(),
                insightDto.getMemo(),
                insightDto.getIsPublic(),
                insightDto.getIsAnonymous(),
                insightDto.getCreatedAt(),
                insightDto.getUpdatedAt(),
                insightDto.getSessionId(),
                insightDto.getSessionName(),
                insightDto.getLikeCount(),
                insightDto.getLatestImageUrl(),
                splitToList(insightDto.getInterestCategory()),
                insightDto.getCommentCount(),
                insightDto.getDisplayName(),
                insightDto.getJob(),
                insightDto.getIsLiked()
        );
    }

    public static List<InsightChangeCategory> makeInsightList(List<InsightListQuery> insightDtos) {
        return insightDtos.stream()
                .map(InsightListMapper::toBuildInsight)
                .collect(Collectors.toList());
    }

    public static InsightListResponse toListPageResponse(
            Page<InsightListQuery> page,
            int pageNumber,
            int pageSize
    ) {
        return new InsightListResponse(
                page.hasNext(),
                page.getTotalElements(),
                page.getTotalPages(),
                pageNumber,
                pageSize,
                makeInsightList(page.getContent())
        );
    }


    private static List<String> splitToList(String interestCategory) {
        if (interestCategory != null && !interestCategory.isEmpty()) {
            return List.of(interestCategory.split("\\s*,\\s*"));
        }
        return List.of();
    }
}