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

    //  좋아요 등록/취소 API
    @PostMapping("/{insightId}/like")
    public ResponseEntity<String> toggleLike(@PathVariable Long insightId,
                                             @RequestParam Long userId) {
        int result = insightService.toggleLike(userId, insightId);
        String message = result == 1 ? "좋아요가 등록되었습니다." : "좋아요가 취소되었습니다.";
        return ResponseEntity.ok(message);
    }

}
