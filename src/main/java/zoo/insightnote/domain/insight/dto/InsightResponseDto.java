package zoo.insightnote.domain.insight.dto;

import lombok.*;

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
        private Boolean isLiked;

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

    @Setter(AccessLevel.PUBLIC)
    private List<VoteOptionDto> voteOptions;

    @Setter(AccessLevel.PUBLIC)
    private Boolean isLiked;

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
        private Boolean isLiked;
    }

    @Getter
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
        private String interestCategory;

        @Setter
        private Boolean isLiked;

        public InsightTopListQueryDto(
                Long id, String memo, Boolean isPublic, Boolean isAnonymous,
                LocalDateTime createdAt, LocalDateTime updatedAt, Long likeCount,
                String imageUrl, Long commentCount, String displayName,
                String job, String interestCategory
        ) {
            this.id = id;
            this.memo = memo;
            this.isPublic = isPublic;
            this.isAnonymous = isAnonymous;
            this.createdAt = createdAt;
            this.updatedAt = updatedAt;
            this.likeCount = likeCount;
            this.imageUrl = imageUrl;
            this.commentCount = commentCount;
            this.displayName = displayName;
            this.job = job;
            this.interestCategory = interestCategory;
        }
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

        @Setter
        private Boolean isLiked;

        public InsightListQueryDto(
                Long id, String memo, Boolean isPublic, Boolean isAnonymous,
                LocalDateTime createdAt, LocalDateTime updatedAt,
                Long sessionId, String sessionName,
                Long likeCount, String latestImageUrl, String interestCategory,
                Long commentCount, String displayName, String job
        ) {
            this.id = id;
            this.memo = memo;
            this.isPublic = isPublic;
            this.isAnonymous = isAnonymous;
            this.createdAt = createdAt;
            this.updatedAt = updatedAt;
            this.sessionId = sessionId;
            this.sessionName = sessionName;
            this.likeCount = likeCount;
            this.latestImageUrl = latestImageUrl;
            this.interestCategory = interestCategory;
            this.commentCount = commentCount;
            this.displayName = displayName;
            this.job = job;
        }
    }


    @Getter
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

        @Setter
        private Boolean isLiked;

        @Setter
        private Boolean hasSpeakerComment;

        // @AllArgsConstructor 를 사용하지 않고 생성자를 직접 쓰는 이유는 쿼리 로직에서 isLiked 는 select에 포함되는게 아닌 서브로 추가된 쿼리여서
        // @AllArgsConstructor가 인식을 하지 못하고 오류가 발생합니다 그래서 이처럼 복잡한 쿼리 경우는 생성자를 직접 적어줘야 합니다
        public SessionInsightListQueryDto(
                Long id, String memo, Boolean isPublic, Boolean isAnonymous,
                LocalDateTime createdAt, LocalDateTime updatedAt,
                Long likeCount, Long commentCount, String displayName, String job
        ) {
            this.id = id;
            this.memo = memo;
            this.isPublic = isPublic;
            this.isAnonymous = isAnonymous;
            this.createdAt = createdAt;
            this.updatedAt = updatedAt;
            this.likeCount = likeCount;
            this.commentCount = commentCount;
            this.displayName = displayName;
            this.job = job;
        }
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
        private Boolean isLiked;
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
        private Boolean isLiked;
        private Boolean hasSpeakerComment;
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