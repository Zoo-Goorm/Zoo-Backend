package zoo.insightnote.domain.insight.mapper;

import org.springframework.data.domain.Page;
import zoo.insightnote.domain.insight.dto.InsightRequestDto;
import zoo.insightnote.domain.insight.dto.InsightResponseDto;
import zoo.insightnote.domain.insight.entity.Insight;
import zoo.insightnote.domain.session.entity.Session;
import zoo.insightnote.domain.user.entity.User;

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

//    public static Insight toEntityInsightBuild(InsightRequestDto.CreateDto request, Session session, User user) {
//        return Insight.builder()
//                .session(session)
//                .user(user)
//                .memo(request.getMemo())
//                .isPublic(request.getIsPublic())
//                .isAnonymous(request.getIsAnonymous())
//                .isDraft(request.getIsDraft())
//                .build();
//    }

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


    private static List<String> splitToList(String str) {
        return (str != null && !str.isBlank()) ? Arrays.asList(str.split("\\s*,\\s*")) : List.of();
    }
}
