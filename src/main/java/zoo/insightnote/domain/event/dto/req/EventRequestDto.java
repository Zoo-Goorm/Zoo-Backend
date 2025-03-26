package zoo.insightnote.domain.event.dto.req;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import zoo.insightnote.domain.image.dto.ImageRequest;

import java.time.LocalDateTime;
import java.util.List;

public record EventRequestDto(
        @Email
        String name,
        @NotBlank
        String description,
        String location,
        LocalDateTime startTime,
        LocalDateTime endTime,
        List<ImageRequest.UploadImage> images
) {}