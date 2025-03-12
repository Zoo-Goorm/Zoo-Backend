package zoo.insightnote.domain.session.dto;

import lombok.*;
import zoo.insightnote.domain.session.entity.SessionStatus;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;


public class SessionResponseDto {

    @Getter
    @AllArgsConstructor
    @Builder
    public static class SessionRes {
        private Long id;
        private String name;
        private String shortDescription;
        private String location;
        private Integer maxCapacity;
        private LocalDateTime startTime;
        private LocalDateTime endTime;
        private String speakerName;
        private List<String> keywords;
        private SessionStatus status;
    }

    @Getter
    @Builder(toBuilder = true)
    public static class SessionAllRes {
        private Long id;
        private String name;
        private Set<String> keywords;
        private String shortDescription;
        private LocalDateTime startTime;
        private LocalDateTime endTime;
        private String timeRange;
        private String location;
    }

    @Getter
    @Builder(toBuilder = true)
    public static class SessionDetailedRes {
        private String timeRange;
        private List<SessionDetail> sessions;

        @Data
        @Builder
        @NoArgsConstructor
        @AllArgsConstructor
        public static class SessionDetail {
            private Long id;
            private String name;
            private String shortDescription;
            private Integer maxCapacity;
            private Integer participantCount;
            private String location;
            private String speakerName;
            private String speakerImageUrl;
            private LocalDateTime startTime;
            private LocalDateTime endTime;
            private SessionStatus status;
            private Set<String> keywords;
        }
    }
}