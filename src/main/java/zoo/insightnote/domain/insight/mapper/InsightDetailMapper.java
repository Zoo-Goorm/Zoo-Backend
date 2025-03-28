package zoo.insightnote.domain.insight.mapper;

import zoo.insightnote.domain.insight.dto.response.InsightDetailResponse;
import zoo.insightnote.domain.insight.dto.response.UserProfileResponse;
import zoo.insightnote.domain.insight.dto.response.query.InsightDetailQuery;

import java.util.Arrays;
import java.util.List;

public class InsightDetailMapper {

    public static InsightDetailResponse toDetailPageResponse(InsightDetailQuery dto) {
        UserProfileResponse profile = new UserProfileResponse(
                dto.getUserName(),
                dto.getEmail(),
                splitToList(dto.getInterestCategory()),
                splitToList(dto.getIntroductionLinks())
        );

        return new InsightDetailResponse(
                dto.getId(),
                dto.getSessionName(),
                dto.getSessionShortDescription(),
                splitToList(dto.getKeywords()),
                dto.getMemo(),
                profile,
                dto.getLikeCount(),
                dto.getVoteTitle(),
                dto.getVoteOptions(),
                dto.getIsLiked(),
                dto.getHasSpeakerComment()
        );
    }
//    public static InsightDetailResponse toDetailPageResponse(
//            InsightDetailQuery insightDto
//    ) {
//        return InsightDetailResponse.builder()
//                .id(insightDto.getId())
//                .name(insightDto.getSessionName())
//                .shortDescription(insightDto.getSessionShortDescription())
//                .keywords(splitToList(insightDto.getKeywords()))
//                .memo(insightDto.getMemo())
//                .likeCount(insightDto.getLikeCount())
//                .isLiked(insightDto.getIsLiked())
//                .hasSpeakerComment(insightDto.getHasSpeakerComment())
//                .profile(InsightDetailResponse.UserProfileDto.builder()
//                        .name(insightDto.getUserName())
//                        .email(insightDto.getEmail())
//                        .interestCategory(splitToList(insightDto.getInterestCategory()))
//                        .linkUrls(splitToList(insightDto.getIntroductionLinks()))
//                        .build())
//                .voteTitle(insightDto.getVoteTitle())
//                .voteOptions(insightDto.getVoteOptions())
//                .build();
//    }

    private static List<String> splitToList(String str) {
        return (str != null && !str.isBlank()) ? Arrays.asList(str.split("\\s*,\\s*")) : List.of();
    }
}