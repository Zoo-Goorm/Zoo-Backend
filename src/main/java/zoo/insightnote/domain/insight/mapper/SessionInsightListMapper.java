package zoo.insightnote.domain.insight.mapper;

import org.springframework.data.domain.Page;
import zoo.insightnote.domain.insight.dto.response.SessionInsight;
import zoo.insightnote.domain.insight.dto.response.SessionInsightListResponse;
import zoo.insightnote.domain.insight.dto.response.query.SessionInsightListQuery;

import java.util.List;
import java.util.stream.Collectors;

public class SessionInsightListMapper {

    public static SessionInsightListResponse toSessionInsightListResponse(Page<SessionInsightListQuery> insightPage) {
        return SessionInsightListResponse.builder()
                .content(makeSessionInsightList(insightPage.getContent()))
                .pageNumber(insightPage.getPageable().getPageNumber())
                .pageSize(insightPage.getSize())
                .totalElements(insightPage.getTotalElements())
                .totalPages(insightPage.getTotalPages())
                .hasNext(insightPage.hasNext())
                .build();
    }

    private static List<SessionInsight> makeSessionInsightList(List<SessionInsightListQuery> insightDtos) {
        return insightDtos.stream()
                .map(dto -> toBuildSessionInsight(dto))
                .collect(Collectors.toList());
    }

    public static SessionInsight toBuildSessionInsight(SessionInsightListQuery dto) {
        return SessionInsight.builder()
                .id(dto.getId())
                .memo(dto.getMemo())
                .isPublic(dto.getIsPublic())
                .isAnonymous(dto.getIsAnonymous())
                .createdAt(dto.getCreatedAt())
                .updatedAt(dto.getUpdatedAt())
                .likeCount(dto.getLikeCount())
                .commentCount(dto.getCommentCount())
                .displayName(dto.getDisplayName())
                .job(dto.getJob())
                .isLiked(dto.getIsLiked())
                .hasSpeakerComment(dto.getHasSpeakerComment())
                .build();
    }
}
