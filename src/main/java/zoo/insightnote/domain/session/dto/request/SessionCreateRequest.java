package zoo.insightnote.domain.session.dto.request;

import lombok.Getter;
import zoo.insightnote.domain.image.dto.ImageRequest;
import zoo.insightnote.domain.session.entity.SessionStatus;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Getter
public class SessionCreateRequest {
    private Long eventId;
    private Long speakerId;
    private LocalDate eventDay;
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
