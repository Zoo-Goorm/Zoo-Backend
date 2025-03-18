package zoo.insightnote.domain.image.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import zoo.insightnote.domain.image.entity.EntityType;

import java.util.List;

public class ImageRequest {

    @Getter
    @AllArgsConstructor
    public static class UploadImage {
        private String fileName;
        private String fileUrl;
    }

    @Getter
    @AllArgsConstructor
    public static class UploadImages {
        private Long entityId;
        private EntityType entityType;
        private List<UploadImage> images;
    }
}