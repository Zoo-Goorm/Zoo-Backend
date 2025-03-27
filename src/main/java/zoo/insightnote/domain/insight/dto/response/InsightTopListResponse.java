package zoo.insightnote.domain.insight.dto.response;

import lombok.Builder;
import lombok.Getter;
import zoo.insightnote.domain.insight.dto.response.query.InsightTopListQuery;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@Getter
@Builder
public class InsightTopListResponse {
    private Long id;
    private String memo;
    private Boolean isPublic;
    private Boolean isAnonymous;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Long likeCount;
    private String imageUrl;
    private Long commentCount;
    private String displayName;
    private String job;
    private List<String> interestCategory;
    private Boolean isLiked;

    public static InsightTopListResponse from(InsightTopListQuery dto) {
        return InsightTopListResponse.builder()
                .id(dto.getId())
                .memo(dto.getMemo())
                .isPublic(dto.getIsPublic())
                .isAnonymous(dto.getIsAnonymous())
                .createdAt(dto.getCreatedAt())
                .updatedAt(dto.getUpdatedAt())
                .likeCount(dto.getLikeCount())
                .imageUrl(dto.getImageUrl())
                .commentCount(dto.getCommentCount())
                .displayName(dto.getDisplayName())
                .job(dto.getJob())
                .interestCategory(
                        dto.getInterestCategory() != null ?
                                Arrays.asList(dto.getInterestCategory().split("\\s*,\\s*")) : List.of()
                )
                .isLiked(dto.getIsLiked())
                .build();
    }
}