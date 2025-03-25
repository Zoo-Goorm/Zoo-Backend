package zoo.insightnote.domain.insight.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import zoo.insightnote.domain.image.dto.ImageRequest;

import java.util.List;

public class InsightRequestDto {

    @Getter
    @AllArgsConstructor
    public static class CreateInsight {
        private Long sessionId;
        private String memo;
        private Boolean isPublic;
        private Boolean isAnonymous;
        private Boolean isDraft;
//        private String voteTitle;
//        private List<String> voteOptions;
//        private List<ImageRequest.UploadImage> images;
    }

    @Getter
    @AllArgsConstructor
    public static class UpdateInsight {
        private String memo;
        private Boolean isPublic;
        private Boolean isAnonymous;
        private Boolean isDraft;
//        private String voteTitle;
//        private List<ImageRequest.UploadImage> images;
    }
}