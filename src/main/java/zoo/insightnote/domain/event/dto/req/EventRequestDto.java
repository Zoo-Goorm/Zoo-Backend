package zoo.insightnote.domain.event.dto.req;

import zoo.insightnote.domain.image.dto.ImageRequest;

import java.time.LocalDateTime;
import java.util.List;

public record EventRequestDto(
        String name,
        String description,
        String location,
        LocalDateTime startTime,
        LocalDateTime endTime,
        List<ImageRequest.UploadImage> images
) {}