package zoo.insightnote.domain.insight.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

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
    @Builder
    public static class InsightDetailPageRes {
        private Long id;
        private String name;
        private String shortDescription;
        private List<String> keywords;
        private String memo;
        private UserProfileDto profile;
        private Long likeCount;

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
        private final Long likeCount;
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
    }

    @Getter
    @AllArgsConstructor
    @Builder
    public static class InsightListPageRes {
        private boolean hasNext;  // 다음 페이지 존재 여부
        private long totalElements;  // 전체 데이터 개수
        private int totalPages;  // 전체 페이지 개수
        private int pageNumber;  // 현재 페이지 번호
        private int pageSize;  // 페이지 크기
        private List<InsightResponseDto.InsightList> content;  // 실제 데이터
    }

}