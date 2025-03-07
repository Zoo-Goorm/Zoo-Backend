package zoo.insightnote.domain.insight.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import zoo.insightnote.domain.insight.dto.InsightRequestDto;
import zoo.insightnote.domain.insight.dto.InsightResponseDto;
import zoo.insightnote.domain.insight.service.InsightService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/insights")
public class InsightControllerImpl implements InsightController{

    private final InsightService insightService;

    @PostMapping
    public ResponseEntity<InsightResponseDto> createInsight(@RequestBody InsightRequestDto.CreateDto request) {
        InsightResponseDto insight = insightService.createInsight(request);
        return ResponseEntity.ok(insight);
    }

    @PutMapping("/{insightId}")
    public ResponseEntity<InsightResponseDto> updateInsight(
            @PathVariable Long insightId,
            @RequestBody InsightRequestDto.UpdateDto request) {
        InsightResponseDto updatedInsight = insightService.updateInsight(insightId, request);
        return ResponseEntity.ok(updatedInsight);
    }

    @DeleteMapping("/{insightId}")
    public ResponseEntity<Void> deleteInsight(@PathVariable Long insightId) {
        insightService.deleteInsight(insightId);
        return ResponseEntity.noContent().build();
    }

}
