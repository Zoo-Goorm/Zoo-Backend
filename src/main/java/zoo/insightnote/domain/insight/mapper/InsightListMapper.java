package zoo.insightnote.domain.insight.mapper;

import org.springframework.data.domain.Page;
import zoo.insightnote.domain.insight.dto.response.InsightChangeCategory;
import zoo.insightnote.domain.insight.dto.response.InsightListResponse;
import zoo.insightnote.domain.insight.dto.response.query.InsightListQuery;

import java.util.List;
import java.util.stream.Collectors;

public class InsightListMapper {

    public static InsightChangeCategory toBuildInsight(InsightListQuery insightDto) {
        return InsightChangeCategory.builder()
                .id(insightDto.getId())
                .memo(insightDto.getMemo())
                .isPublic(insightDto.getIsPublic())
                .isAnonymous(insightDto.getIsAnonymous())
                .createdAt(insightDto.getCreatedAt())
                .updatedAt(insightDto.getUpdatedAt())
                .sessionId(insightDto.getSessionId())
                .sessionName(insightDto.getSessionName())
                .likeCount(insightDto.getLikeCount())
                .latestImageUrl(insightDto.getLatestImageUrl())
                .interestCategory(splitToList(insightDto.getInterestCategory()))
                .commentCount(insightDto.getCommentCount())
                .displayName(insightDto.getDisplayName())
                .job(insightDto.getJob())
                .isLiked(insightDto.getIsLiked())
                .build();
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
        return InsightListResponse.builder()
                .hasNext(page.hasNext())
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .pageNumber(pageNumber)
                .pageSize(pageSize)
                .content(makeInsightList(page.getContent()))
                .build();
    }


    private static List<String> splitToList(String interestCategory) {
        if (interestCategory != null && !interestCategory.isEmpty()) {
            return List.of(interestCategory.split("\\s*,\\s*"));
        }
        return List.of();
    }
}