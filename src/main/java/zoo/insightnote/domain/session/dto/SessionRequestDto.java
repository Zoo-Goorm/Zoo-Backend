package zoo.insightnote.domain.session.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import zoo.insightnote.domain.image.dto.ImageRequest;
import zoo.insightnote.domain.session.entity.SessionStatus;

import java.time.LocalDateTime;
import java.util.List;

public class SessionRequestDto {

    @Getter
    @Builder
    @AllArgsConstructor
    public static class Create {
        private Long eventId;
        private Long speakerId;
        private Integer eventDay;
        private String name;
        private String shortDescription;
        private String longDescription;
        private Integer maxCapacity;
        private LocalDateTime startTime;
        private LocalDateTime endTime;
        private SessionStatus status;
        private String videoLink;
        private String location;
        private List<ImageRequest.UploadImage> images;
        private List<String> keywords;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    public static class Update {
        private String name;
        private String shortDescription;
        private String longDescription;
        private Integer maxCapacity;
        private LocalDateTime startTime;
        private LocalDateTime endTime;
        private SessionStatus status;
        private String videoLink;
        private String location;
        private List<ImageRequest.UploadImage> images;
        private List<String> keywords;
    }
}