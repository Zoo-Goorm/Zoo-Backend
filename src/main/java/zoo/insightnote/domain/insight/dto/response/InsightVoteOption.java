package zoo.insightnote.domain.insight.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class InsightVoteOption {
    private Long optionId;
    private String optionText;
    private String voteCount;
}