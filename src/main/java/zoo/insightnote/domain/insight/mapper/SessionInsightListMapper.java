package zoo.insightnote.domain.insight.mapper;

import org.springframework.data.domain.Page;
import zoo.insightnote.domain.insight.dto.response.SessionInsight;
import zoo.insightnote.domain.insight.dto.response.SessionInsightListResponse;
import zoo.insightnote.domain.insight.dto.response.UserProfileResponse;
import zoo.insightnote.domain.insight.dto.response.query.SessionInsightListQuery;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class SessionInsightListMapper {

    public static SessionInsightListResponse toSessionInsightListResponse(Page<SessionInsightListQuery> insightPage) {
        return new SessionInsightListResponse(
                insightPage.hasNext(),
                insightPage.getTotalElements(),
                insightPage.getTotalPages(),
                insightPage.getPageable().getPageNumber(),
                insightPage.getSize(),
                makeSessionInsightList(insightPage.getContent())
        );
    }

    private static List<SessionInsight> makeSessionInsightList(List<SessionInsightListQuery> insightDtos) {
        return insightDtos.stream()
                .map(dto -> toBuildSessionInsight(dto))
                .collect(Collectors.toList());
    }

    public static SessionInsight toBuildSessionInsight(SessionInsightListQuery dto) {
        UserProfileResponse newProfile = new UserProfileResponse(
                dto.getName(),
                dto.getEmail(),
                splitToList(dto.getInterestCategory()),
                splitToList(dto.getIntroductionLinks())
        );

        return new SessionInsight(
                dto.getId(),
                dto.getMemo(),
                dto.getIsPublic(),
                dto.getIsAnonymous(),
                dto.getCreatedAt(),
                dto.getUpdatedAt(),
                dto.getLikeCount(),
                dto.getCommentCount(),
                dto.getDisplayName(),
                dto.getJob(),
                dto.getIsLiked(),
                dto.getHasSpeakerComment(),
                newProfile,
                dto.getIsMine()
        );
    }



    private static List<String> splitToList(String str) {
        return (str != null && !str.isBlank()) ? Arrays.asList(str.split("\\s*,\\s*")) : List.of();
    }
}
