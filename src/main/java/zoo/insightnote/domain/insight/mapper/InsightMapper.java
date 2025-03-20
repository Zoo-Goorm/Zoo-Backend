package zoo.insightnote.domain.insight.mapper;

import zoo.insightnote.domain.insight.dto.InsightRequestDto;
import zoo.insightnote.domain.insight.dto.InsightResponseDto;
import zoo.insightnote.domain.insight.entity.Insight;
import zoo.insightnote.domain.session.entity.Session;
import zoo.insightnote.domain.user.entity.User;
import zoo.insightnote.domain.userIntroductionLink.entity.UserIntroductionLink;
import zoo.insightnote.domain.voteOption.entity.VoteOption;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class InsightMapper {

    public static Insight toEntity(InsightRequestDto.CreateDto request, Session session, User user) {
        return Insight.create(
                session,
                user,
                request.getMemo(),
                request.getIsPublic(),
                request.getIsAnonymous(),
                request.getIsDraft(),
                request.getVoteTitle()
        );
    }

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

    public static InsightResponseDto.InsightDetailRes toDetailResponse(
            InsightResponseDto.InsightWithDetailsQueryDto insightDto
    ) {
        return InsightResponseDto.InsightDetailRes.builder()
                .id(insightDto.getId())
                .name(insightDto.getSessionName())
                .shortDescription(insightDto.getSessionShortDescription())
                .keywords(splitToList(insightDto.getKeywords()))  // 변환
                .memo(insightDto.getMemo())
                .likeCount(insightDto.getLikeCount())
                .profile(InsightResponseDto.InsightDetailRes.UserProfileDto.builder()
                        .name(insightDto.getUserName())
                        .email(insightDto.getEmail())
                        .interestCategory(splitToList(insightDto.getInterestCategory()))  // 변환
                        .linkUrls(splitToList(insightDto.getIntroductionLinks()))  // 변환
                        .build())
                .build();
    }

    private static List<String> splitToList(String str) {
        return (str != null && !str.isBlank()) ? Arrays.asList(str.split("\\s*,\\s*")) : List.of();
    }
}
