package zoo.insightnote.domain.insight.dto.response.query;

import lombok.Getter;
import lombok.Setter;
import zoo.insightnote.domain.insight.dto.response.InsightVoteOption;

import java.util.List;

@Getter
public class InsightDetailQuery {

    private Long id;
    private String memo;
    private String voteTitle;
    private Boolean isPublic;
    private Boolean isAnonymous;
    private Boolean isDraft;
    private Long sessionId;
    private String sessionName;
    private String sessionShortDescription;
    private Long userId;
    private String userName;
    private String email;
    private String interestCategory;
    private String keywords;
    private String introductionLinks;
    private Long likeCount;

    @Setter
    private List<InsightVoteOption> voteOptions;

    @Setter
    private Boolean isLiked;

    @Setter
    private Boolean hasSpeakerComment;

    public InsightDetailQuery(Long id, String memo, String voteTitle, Boolean isPublic, Boolean isAnonymous,
                              Boolean isDraft, Long sessionId, String sessionName, String sessionShortDescription,
                              Long userId, String userName, String email, String interestCategory, String keywords,
                              String introductionLinks, Long likeCount) {
        this.id = id;
        this.memo = memo;
        this.voteTitle = voteTitle;
        this.isPublic = isPublic;
        this.isAnonymous = isAnonymous;
        this.isDraft = isDraft;
        this.sessionId = sessionId;
        this.sessionName = sessionName;
        this.sessionShortDescription = sessionShortDescription;
        this.userId = userId;
        this.userName = userName;
        this.email = email;
        this.interestCategory = interestCategory;
        this.keywords = keywords;
        this.introductionLinks = introductionLinks;
        this.likeCount = likeCount;
    }
}
