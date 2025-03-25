package zoo.insightnote.domain.insight.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
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
    @AllArgsConstructor
    public static class InsightIdRes {
        private Long id;
    }


    @Getter
    @Builder
    public static class InsightDetailPageRes {
        private Long id;
        private String name;
        private String shortDescription;
        private List<String> keywords;
        private String memo;
        private UserProfileDto profile;
        private Long likeCount;
        private String voteTitle;
        private List<VoteOptionDto> voteOptions;

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
@AllArgsConstructor
public static class InsightDetailQueryDto {
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
    private List<VoteOptionDto> voteOptions;

    // QueryDSL이 사용하는 생성자,  Projections.constructor()가 생성자를 찾지 못하기 때문에 별도로 표기함
    // @AllArgsConstructor 있어도 해당 생성자 없으면 실행 오류나옴
    public InsightDetailQueryDto(Long id, String memo, String voteTitle, Boolean isPublic, Boolean isAnonymous,
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

    // 투표 옵션 경우 쿼리에서 가져오는게 아니라 별도로 조회 후 추가하는 Setter 메서드
    public void setVoteOptions(List<VoteOptionDto> voteOptions) {
        this.voteOptions = voteOptions;
    }
}


    @Getter
    @AllArgsConstructor
    public static class VoteOptionDto {
        private Long optionId;
        private String optionText;
        private String voteCount;
    }



    @Getter
    @AllArgsConstructor
    @Builder
    public static class InsightTopRes {
        private Long id;
        private String memo;
        private Boolean isPublic;
        private Boolean isAnonymous;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;
        private Long likeCount;
        private String imageUrl;
        private Long commentCount;
        private String displayName;
        private String job;
        private List<String> interestCategory;
    }

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class InsightTopListQueryDto {
        private Long id;
        private String memo;
        private Boolean isPublic;
        private Boolean isAnonymous;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;
        private Long likeCount;
        private String imageUrl;
        private Long commentCount;
        private String displayName;
        private String job;
        private String interestCategory; // ← raw string (쉼표 구분)
    }


    @Getter
    @AllArgsConstructor
    public static class InsightListQueryDto {
        private Long id;
        private String memo;
        private Boolean isPublic;
        private Boolean isAnonymous;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;
        private Long sessionId;
        private String sessionName;
        private Long likeCount;
        private String latestImageUrl;
        private String interestCategory;
        private Long commentCount;
        private String displayName;
        private String job;
    }


    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SessionInsightListQueryDto {
        private Long id;
        private String memo;
        private Boolean isPublic;
        private Boolean isAnonymous;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;
        private Long likeCount;
        private Long commentCount;
        private String displayName;
        private String job;
    }


    @Getter
    @AllArgsConstructor
    @Builder
    public static class InsightList {
        private Long id;
        private String memo;
        private Boolean isPublic;
        private Boolean isAnonymous;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;
        private Long sessionId;
        private String sessionName;
        private Long likeCount;
        private String latestImageUrl;
        private List<String> interestCategory;
        private Long commentCount;
        private String displayName;
        private String job;
    }

    @Getter
    @AllArgsConstructor
    @Builder
    public static class SessionInsightList {
        private Long id;
        private String memo;
        private Boolean isPublic;
        private Boolean isAnonymous;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;
        private Long likeCount;
        private Long commentCount;
        private String displayName;
        private String job;
    }

    @Getter
    @AllArgsConstructor
    @Builder
    public static class InsightListPageRes {
        private boolean hasNext;
        private long totalElements;
        private int totalPages;
        private int pageNumber;
        private int pageSize;
        private List<InsightResponseDto.InsightList> content; // 인사이트 리스트
    }

    @Getter
    @AllArgsConstructor
    @Builder
    public static class SessionInsightListPageRes {
        private boolean hasNext;
        private long totalElements;
        private int totalPages;
        private int pageNumber;
        private int pageSize;
        private List<InsightResponseDto.SessionInsightList> content; // 인사이트 리스트
    }


    @Getter
    @AllArgsConstructor
    @Builder
    public static class MyInsightListPageRes {
        private boolean hasNext;
        private long totalElements;
        private int totalPages;
        private int pageNumber;
        private int pageSize;
        private List<InsightResponseDto.MyInsightListQueryDto> content; // 인사이트 리스트
    }

    @Getter
    @Builder
    @AllArgsConstructor
    public static class MyInsightListQueryDto {
        private Long insightId;
        private String memo;
        private Boolean isPublic;
        private Boolean isAnonymous;
        private Boolean isDraft;
        private LocalDateTime updatedAt;
        private Long sessionId;
        private String sessionName;
    }


}