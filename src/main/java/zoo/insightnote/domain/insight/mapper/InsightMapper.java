package zoo.insightnote.domain.insight.mapper;

import org.springframework.data.domain.Page;
import zoo.insightnote.domain.insight.dto.InsightResponseDto;
import zoo.insightnote.domain.insight.entity.Insight;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class InsightMapper {

//    public static Insight toEntity(InsightRequestDto.CreateInsight request, Session session, User user) {
//        return Insight.create(
//                session,
//                user,
//                request.getMemo(),
//                request.getIsPublic(),
//                request.getIsAnonymous(),
//                request.getIsDraft()
////                request.getVoteTitle()
//        );
//    }

    public static InsightResponseDto.InsightRes toResponse(Insight insight) {
        return InsightResponseDto.InsightRes.builder()
                .id(insight.getId())
                .sessionId(insight.getSession().getId())
                .memo(insight.getMemo())
                .isPublic(insight.getIsPublic())
                .isAnonymous(insight.getIsAnonymous())
                .isDraft(insight.getIsDraft())
                .createdAt(insight.getCreateAt())
                .updatedAt(insight.getUpdatedAt())
                .build();
    }


    public static InsightResponseDto.InsightDetailPageRes toDetailPageResponse(
            InsightResponseDto.InsightDetailQueryDto insightDto
    ) {
        return InsightResponseDto.InsightDetailPageRes.builder()
                .id(insightDto.getId())
                .name(insightDto.getSessionName())
                .shortDescription(insightDto.getSessionShortDescription())
                .keywords(splitToList(insightDto.getKeywords()))
                .memo(insightDto.getMemo())
                .likeCount(insightDto.getLikeCount())
                .isLiked(insightDto.getIsLiked())
                .profile(InsightResponseDto.InsightDetailPageRes.UserProfileDto.builder()
                        .name(insightDto.getUserName())
                        .email(insightDto.getEmail())
                        .interestCategory(splitToList(insightDto.getInterestCategory()))
                        .linkUrls(splitToList(insightDto.getIntroductionLinks()))
                        .build())
                .voteTitle(insightDto.getVoteTitle())
                .voteOptions(insightDto.getVoteOptions())
                .build();
    }


    public static InsightResponseDto.SessionInsightList toBuildSessionInsigh(
            InsightResponseDto.SessionInsightListQueryDto dto
    ) {
        return InsightResponseDto.SessionInsightList.builder()
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


    public static InsightResponseDto.InsightList toBuildInsight (
            InsightResponseDto.InsightListQueryDto insightDto
    ) {
        return InsightResponseDto.InsightList.builder()
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

    public static List<InsightResponseDto.InsightList> makeInsightList(
            List<InsightResponseDto.InsightListQueryDto> insightDtos
    ) {
        return insightDtos.stream()
                .map(InsightMapper::toBuildInsight)
                .collect(Collectors.toList());
    }


    public static InsightResponseDto.InsightListPageRes toListPageResponse(
            Page<InsightResponseDto.InsightListQueryDto> page,
            int pageNumber,
            int pageSize
    ) {
        return InsightResponseDto.InsightListPageRes.builder()
                .hasNext(page.hasNext())
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .pageNumber(pageNumber)
                .pageSize(pageSize)
                .content(makeInsightList(page.getContent()))
                .build();
    }

    public static List<InsightResponseDto.SessionInsightList> makeSessionInsightList(
            List<InsightResponseDto.SessionInsightListQueryDto> insightDtos
    ) {
        return insightDtos.stream()
                .map(InsightMapper::toBuildSessionInsigh)
                .collect(Collectors.toList());
    }

    public static InsightResponseDto.SessionInsightListPageRes  toSessionInsightPageResponse(
            Page<InsightResponseDto.SessionInsightListQueryDto> page,
            int pageNumber,
            int pageSize
    ) {
        return InsightResponseDto.SessionInsightListPageRes.builder()
                .hasNext(page.hasNext())
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .pageNumber(pageNumber)
                .pageSize(pageSize)
                .content(makeSessionInsightList(page.getContent()))
                .build();
    }


    public static InsightResponseDto.InsightTopRes toBuildTopInsight(
            InsightResponseDto.InsightTopListQueryDto dto
    ) {
        return InsightResponseDto.InsightTopRes.builder()
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
                .interestCategory(splitToList(dto.getInterestCategory()))
                .isLiked(dto.getIsLiked())
                .build();
    }

    public static List<InsightResponseDto.InsightTopRes> toTopInsightList(
            List<InsightResponseDto.InsightTopListQueryDto> topList
    ) {
        return topList.stream()
                .map(InsightMapper::toBuildTopInsight)
                .collect(Collectors.toList());
    }


    public static InsightResponseDto.MyInsightListQueryDto toBuildMyInsightList(InsightResponseDto.MyInsightListQueryDto dto) {
        return InsightResponseDto.MyInsightListQueryDto.builder()
                .insightId(dto.getInsightId())
                .memo(dto.getMemo())
                .isPublic(dto.getIsPublic())
                .isAnonymous(dto.getIsAnonymous())
                .updatedAt(dto.getUpdatedAt())
                .sessionId(dto.getSessionId())
                .sessionName(dto.getSessionName())
                .build();
    }

    public static InsightResponseDto.MyInsightListPageRes toMyListPageResponse(
            Page<InsightResponseDto.MyInsightListQueryDto> page,
            int pageNumber,
            int pageSize
    ) {
        List<InsightResponseDto.MyInsightListQueryDto> content = page.getContent().stream()
                .map(InsightMapper::toBuildMyInsightList)
                .collect(Collectors.toList());

        return InsightResponseDto.MyInsightListPageRes.builder()
                .hasNext(page.hasNext())
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .pageNumber(pageNumber)
                .pageSize(pageSize)
                .content(content)
                .build();
    }


    private static List<String> splitToList(String str) {
        return (str != null && !str.isBlank()) ? Arrays.asList(str.split("\\s*,\\s*")) : List.of();
    }
}
