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

    @Override
    @PostMapping
    public ResponseEntity<InsightResponseDto.InsightRes> createInsight(
            @RequestBody InsightRequestDto.CreateDto request) {
        InsightResponseDto.InsightRes insight = insightService.createInsight(request);
        return ResponseEntity.ok(insight);
    }

    @Override
    @PutMapping("/{insightId}")
    public ResponseEntity<InsightResponseDto.InsightRes> updateInsight(
            @PathVariable Long insightId,
            @RequestBody InsightRequestDto.UpdateDto request) {
        InsightResponseDto.InsightRes updatedInsight = insightService.updateInsight(insightId, request);
        return ResponseEntity.ok(updatedInsight);
    }

    @Override
    @DeleteMapping("/{insightId}")
    public ResponseEntity<Void> deleteInsight(@PathVariable Long insightId) {
        insightService.deleteInsight(insightId);
        return ResponseEntity.noContent().build();
    }

    @Override
    @GetMapping("/{insightId}")
    public ResponseEntity<InsightResponseDto.InsightRes> getInsightById(@PathVariable Long insightId) {
        InsightResponseDto.InsightRes insight = insightService.getInsightById(insightId);
        return ResponseEntity.ok(insight);
    }
}
