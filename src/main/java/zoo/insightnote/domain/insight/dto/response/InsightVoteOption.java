package zoo.insightnote.domain.insight.dto.response;

public record InsightVoteOption(
        Long optionId,
        String optionText,
        String voteCount
) {}