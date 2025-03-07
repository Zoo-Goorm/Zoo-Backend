package zoo.insightnote.domain.image.dto;

import zoo.insightnote.domain.image.entity.EntityType;

import java.util.List;

public interface ImageRequest {
    record UploadImage(
            String fileName,
            String fileUrl
    ) implements ImageRequest {
    }

    record UploadImages(
            Long entityId,
            EntityType entityType,
            List<UploadImage> images
    ) implements ImageRequest {
    }
}