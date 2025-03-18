package zoo.insightnote.domain.insight.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import zoo.insightnote.domain.image.dto.ImageRequest;

import java.util.List;

public class InsightRequestDto {

    @Getter
    @AllArgsConstructor
    public static class CreateDto {
        private Long sessionId;
        private Long userId;
        private String memo;
        private Boolean isPublic;
        private Boolean isAnonymous;
        private List<ImageRequest.UploadImage> images;
    }

    @Getter
    @AllArgsConstructor
    public static class UpdateDto {
        private String memo;
        private Boolean isPublic;
        private Boolean isAnonymous;
        private List<ImageRequest.UploadImage> images;
    }
}