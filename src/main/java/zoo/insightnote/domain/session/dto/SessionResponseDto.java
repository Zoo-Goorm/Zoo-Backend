package zoo.insightnote.domain.session.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import zoo.insightnote.domain.session.entity.SessionStatus;

import java.time.LocalDate;
import java.util.List;


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
        private LocalDate startTime;
        private LocalDate endTime;
        private String speakerName;
        private List<String> keywords;
        private SessionStatus status;
    }

    @Getter
    @Builder
    public static class SessionAllRes {
        private Long id;
        private String name;
        private List<String> keywords;
        private String shortDescription;
        private LocalDate startTime;
        private LocalDate endTime;
        private String location;
    }

    @Getter
    @Builder
    public static class SessionDetailedRes {
        private Long id;
        private String name;
        private List<String> keywords;
        private String speakerName;
        private String speakerImageUrl;
        private String shortDescription;
        private Integer maxCapacity;
        private Integer participantCount;
        private LocalDate startTime;
        private LocalDate endTime;
        private SessionStatus status;
        private String location;
    }

}