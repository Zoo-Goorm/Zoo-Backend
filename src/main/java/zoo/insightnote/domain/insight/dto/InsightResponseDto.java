package zoo.insightnote.domain.insight.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

public class InsightResponseDto {

    @Getter
    @AllArgsConstructor
    @Builder
    public static class InsightRes {
        private Long id;
        private Long sessionId;
        private String memo;
        private Boolean isPublic;
        private LocalDate createdAt;
        private LocalDate updatedAt;
    }

}