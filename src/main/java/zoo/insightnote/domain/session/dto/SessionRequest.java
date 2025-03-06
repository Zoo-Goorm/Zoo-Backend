package zoo.insightnote.domain.session.dto;

import zoo.insightnote.domain.image.dto.ImageRequest;
import zoo.insightnote.domain.session.entity.SessionStatus;

import java.time.LocalDateTime;
import java.util.List;

public interface SessionRequest {
    record Create(
            Long eventId,
            Long speakerId,
            Integer eventDay,
            String name,
            String shortDescription,
            String longDescription,
            Integer maxCapacity,
            LocalDateTime startTime,
            LocalDateTime endTime,
            SessionStatus status,
            String videoLink,
            String location,
            List<ImageRequest.UploadImage> images
    ) implements SessionRequest {
    }
}