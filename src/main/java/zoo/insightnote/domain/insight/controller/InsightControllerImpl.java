package zoo.insightnote.domain.insight.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import zoo.insightnote.domain.insight.dto.InsightRequestDto;
import zoo.insightnote.domain.insight.dto.InsightResponseDto;
import zoo.insightnote.domain.insight.service.InsightService;
import zoo.insightnote.domain.user.service.UserService;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class InsightControllerImpl implements InsightController{

    private final InsightService insightService;
    private final UserService userService;

//    @Override
//    @PostMapping
//    public ResponseEntity<InsightResponseDto.InsightRes> createInsight(
//            @RequestBody InsightRequestDto.CreateDto request) {
//
//        InsightResponseDto.InsightRes insight = insightService.saveOrUpdateInsight(request);
//        return ResponseEntity.ok(insight);
//    }

    @Override
    @PostMapping("/insights")
    public ResponseEntity<InsightResponseDto.InsightIdRes> createInsight(InsightRequestDto.CreateInsight request) {
        InsightResponseDto.InsightIdRes response = insightService.createInsight(request);
        return ResponseEntity.ok(response);
    }

    @Override
    @PutMapping("/insights/{insightId}")
    public ResponseEntity<InsightResponseDto.InsightIdRes> updateInsight(
            @PathVariable Long insightId,
            @RequestBody InsightRequestDto.UpdateInsight request
    ) {
        InsightResponseDto.InsightIdRes response = insightService.updateInsight(insightId, request);
        return ResponseEntity.ok(response);
    }

//    @Override
//    public ResponseEntity<InsightResponseDto.InsightRes> updateInsight(Long insightId, InsightRequestDto.UpdateDto request) {
//        InsightResponseDto.InsightRes updated = insightService.updateInsightMemoOnly(insightId, request);
//        return ResponseEntity.ok(updated);
//    }
//

//    @Override
//    @PutMapping("/{insightId}")
//    public ResponseEntity<InsightResponseDto.InsightRes> updateInsight(
//            @PathVariable Long insightId,
//            @RequestBody InsightRequestDto.UpdateDto request) {
//        InsightResponseDto.InsightRes updatedInsight = insightService.updateInsight(insightId, request);
//        return ResponseEntity.ok(updatedInsight);
//    }

    @Override
    @DeleteMapping("/insights/{insightId}")
    public ResponseEntity<Void> deleteInsight(@PathVariable Long insightId) {
        insightService.deleteInsight(insightId);
        return ResponseEntity.noContent().build();
    }

//    @Override
//    @GetMapping("/{insightId}")
//    public ResponseEntity<InsightResponseDto.InsightRes> getInsightById(@PathVariable Long insightId) {
//        InsightResponseDto.InsightRes insight = insightService.getInsightById(insightId);
//        return ResponseEntity.ok(insight);
//    }

    //  좋아요 등록/취소 API
    @PostMapping("/insights/{insightId}/like")
    public ResponseEntity<String> toggleLike(@PathVariable Long insightId,
                                             @RequestParam Long userId) {
        int result = insightService.toggleLike(userId, insightId);
        String message = result == 1 ? "좋아요가 등록되었습니다." : "좋아요가 취소되었습니다.";
        return ResponseEntity.ok(message);
    }



    // 인기순위 상위 3개 가져오기
    @GetMapping("/insights/top")
    public ResponseEntity<List<InsightResponseDto.InsightTopRes>> getTop3PopularInsights() {
        List<InsightResponseDto.InsightTopRes> topInsights = insightService.getTopPopularInsights();
        return ResponseEntity.ok(topInsights);
    }

    @Override
    @GetMapping("/insights/list")
    public ResponseEntity<InsightResponseDto.InsightListPageRes> getInsights(
            @RequestParam("eventDay") LocalDate eventDay,
            @RequestParam(value = "sessionId", required = false) Long sessionId,
            @RequestParam(value = "sort", defaultValue = "latest") String sort,
            @RequestParam(value = "page", defaultValue = "0") int page
    ) {
        InsightResponseDto.InsightListPageRes insights = insightService.getInsightsByEventDay(eventDay, sessionId, sort, page);
        return ResponseEntity.ok(insights);
    }

    // 인사이트 상세 페이지
    @Override
    @GetMapping("/insights/{insightId}")
    public ResponseEntity<InsightResponseDto.InsightDetailPageRes> getInsightDetail(@PathVariable Long insightId) {
        InsightResponseDto.InsightDetailPageRes insightDetail = insightService.getInsightDetail(insightId);
        return ResponseEntity.ok(insightDetail);
    }

    @Override
    @GetMapping("sessions/{sessionId}/insight-notes")
    public ResponseEntity<InsightResponseDto.SessionInsightListPageRes> getInsightsBySession(
            @PathVariable Long sessionId,
            @RequestParam(defaultValue = "latest") String sort,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @AuthenticationPrincipal UserDetails userDetails
    ) {

//        User user = userService.findByUsername(userDetails.getUsername());
        Pageable pageable = PageRequest.of(page, size);
        InsightResponseDto.SessionInsightListPageRes response = insightService.getInsightsBySession(sessionId, sort, pageable, userDetails.getUsername());
        return ResponseEntity.ok(response);
    }

}
