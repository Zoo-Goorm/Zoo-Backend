package zoo.insightnote.domain.insight.dto.response;

import zoo.insightnote.domain.insight.dto.response.query.InsightTopListQuery;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

public record InsightTopListResponse(
        Long id,
        String memo,
        Boolean isPublic,
        Boolean isAnonymous,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        Long likeCount,
        String imageUrl,
        Long commentCount,
        String displayName,
        String job,
        List<String> interestCategory,
        Boolean isLiked
) {
    public static InsightTopListResponse from(InsightTopListQuery dto) {
        return new InsightTopListResponse(
                dto.getId(),
                dto.getMemo(),
                dto.getIsPublic(),
                dto.getIsAnonymous(),
                dto.getCreatedAt(),
                dto.getUpdatedAt(),
                dto.getLikeCount(),
                dto.getImageUrl(),
                dto.getCommentCount(),
                dto.getDisplayName(),
                dto.getJob(),
                dto.getInterestCategory() != null ?
                        Arrays.asList(dto.getInterestCategory().split("\\s*,\\s*")) : List.of(),
                dto.getIsLiked()
        );
    }
}