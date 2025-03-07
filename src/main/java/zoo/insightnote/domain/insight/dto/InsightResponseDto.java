package zoo.insightnote.domain.insight.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class InsightResponseDto {

    private Long id;
    private Long sessionId;
    private String memo;
    private Boolean isPublic;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

}