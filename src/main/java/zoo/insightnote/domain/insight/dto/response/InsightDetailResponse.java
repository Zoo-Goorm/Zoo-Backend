package zoo.insightnote.domain.insight.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class InsightDetailResponse {
    private Long id;
    private String name;
    private String shortDescription;
    private List<String> keywords;
    private String memo;
    private UserProfileDto profile;
    private Long likeCount;
    private String voteTitle;
    private List<InsightVoteOption> voteOptions;
    private Boolean isLiked;
    private Boolean hasSpeakerComment;

    @Getter
    @Builder
    public static class UserProfileDto {
        private String name;
        private String email;
        private List<String> interestCategory;
        private List<String> linkUrls;
    }
}