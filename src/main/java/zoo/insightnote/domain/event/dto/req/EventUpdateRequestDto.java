package zoo.insightnote.domain.event.dto.req;

import zoo.insightnote.domain.image.dto.ImageRequest;

import java.time.LocalDate;
import java.util.List;

public record EventUpdateRequestDto(
        String name,
        String description,
        String location,
        LocalDate startTime,
        LocalDate endTime,
        List<ImageRequest.UploadImage> images
) {}