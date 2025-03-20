package zoo.insightnote.domain.insight.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

public class InsightResponseDto {

    @Getter
    @AllArgsConstructor
    @Builder
    public static class InsightRes {
        private Long id;
        private Long sessionId;
        private String memo;
        private Boolean isPublic;
        private Boolean isAnonymous;
        private Boolean isDraft;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;
    }

    @Getter
    @Builder
    public static class InsightDetailRes {
        private Long id;
        private String name;
        private String shortDescription;
        private List<String> keywords;
        private String memo;
        private UserProfileDto profile;

        @Getter
        @Builder
        public static class UserProfileDto {
            private String name;
            private String email;
            private List<String> interestCategory;
            private List<String> linkUrls;
        }
    }

    @Getter
    public static class InsightWithDetailsQueryDto {
        private final Long id;
        private final String memo;
        private final String voteTitle;
        private final Boolean isPublic;
        private final Boolean isAnonymous;
        private final Boolean isDraft;
        private final Long sessionId;
        private final String sessionName;
        private final String sessionShortDescription;
        private final Long userId;
        private final String userName;
        private final String email;
        private final String interestCategory;
        private final String keywords;
        private final String introductionLinks;

        @Builder
        public InsightWithDetailsQueryDto(
                Long id, String memo, String voteTitle, Boolean isPublic, Boolean isAnonymous, Boolean isDraft,
                Long sessionId, String sessionName, String sessionShortDescription,
                Long userId, String userName, String email, String interestCategory,
                String keywords, String introductionLinks
        ) {
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
        }
    }
}