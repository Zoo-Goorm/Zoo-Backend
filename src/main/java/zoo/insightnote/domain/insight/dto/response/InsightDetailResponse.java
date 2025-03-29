package zoo.insightnote.domain.insight.dto.response;

import java.util.List;

public record InsightDetailResponse(
        Long id,
        String name,
        String shortDescription,
        List<String> keywords,
        String memo,
        UserProfileResponse profile,
        Long likeCount,
        String voteTitle,
        List<InsightVoteOption> voteOptions,
        Boolean isLiked,
        Boolean hasSpeakerComment
) {}