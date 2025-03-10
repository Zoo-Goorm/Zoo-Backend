package zoo.insightnote.domain.insight.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

public class InsightResponseDto {

    @Getter
    @AllArgsConstructor
    public static class InsightRes {
        private Long id;
        private Long sessionId;
        private String memo;
        private Boolean isPublic;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;
    }

}