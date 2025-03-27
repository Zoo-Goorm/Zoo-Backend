package zoo.insightnote.domain.insight.controller;

import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import zoo.insightnote.domain.insight.dto.InsightRequestDto;
import zoo.insightnote.domain.insight.dto.InsightResponseDto;
import zoo.insightnote.domain.insight.dto.request.InsightCreateRequest;
import zoo.insightnote.domain.insight.dto.request.InsightUpdateRequest;
import zoo.insightnote.domain.insight.dto.response.InsightIdResponse;
import zoo.insightnote.domain.insight.dto.response.InsightLikeToggleResponse;
import zoo.insightnote.domain.insight.service.InsightService;
import zoo.insightnote.domain.user.entity.User;
import zoo.insightnote.domain.user.service.UserService;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class InsightControllerImpl implements InsightController{

    private final InsightService insightService;
    private final UserService userService;

    @Override
    @PostMapping("/insights")
    public ResponseEntity<InsightIdResponse> createInsight(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestBody InsightCreateRequest request
    ) {
        User user = userService.findByUsername(userDetails.getUsername());
        InsightIdResponse response =  insightService.createInsight(request, user);
        return ResponseEntity.ok(response);
    }

    @Override
    @PutMapping("/insights/{insightId}")
    public ResponseEntity<InsightIdResponse> updateInsight(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable Long insightId,
            @RequestBody InsightUpdateRequest request
    ) {
        User user = userService.findByUsername(userDetails.getUsername());
        InsightIdResponse response = insightService.updateInsight(insightId, request , user);
        return ResponseEntity.ok(response);
    }


    @Override
    @DeleteMapping("/insights/{insightId}")
    public ResponseEntity<Void> deleteInsight(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable Long insightId
    ) {
        User user = userService.findByUsername(userDetails.getUsername());
        insightService.deleteInsight(insightId, user);
        return ResponseEntity.noContent().build();
    }

//    @Override
//    @GetMapping("/{insightId}")
//    public ResponseEntity<InsightResponseDto.InsightRes> getInsightById(@PathVariable Long insightId) {
//        InsightResponseDto.InsightRes insight = insightService.getInsightById(insightId);
//        return ResponseEntity.ok(insight);
//    }

    //  좋아요 등록/취소 API
//    @PostMapping("/insights/{insightId}/like")
//    public ResponseEntity<String> toggleLike(
//            @PathVariable Long insightId,
//            @AuthenticationPrincipal UserDetails userDetails
//    ) {
//        int result = insightService.toggleLike(userDetails.getUsername(), insightId);
//        String message = result == 1 ? "좋아요가 등록되었습니다." : "좋아요가 취소되었습니다.";
//        return ResponseEntity.ok(message);
//    }

    @PostMapping("/insights/{insightId}/like")
    public ResponseEntity<String> toggleLike(
            @PathVariable Long insightId,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        User user = userService.findByUsername(userDetails.getUsername());
        int result = insightService.toggleLike(user, insightId);
        String message = result == 1 ? "좋아요 등록" : "좋아요 취소";
        return ResponseEntity.ok(message);
    }

    // 인기순위 상위 3개 가져오기
    @GetMapping("/insights/top")
    public ResponseEntity<List<InsightResponseDto.InsightTopRes>> getTop3PopularInsights(
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        List<InsightResponseDto.InsightTopRes> topInsights = insightService.getTopPopularInsights(userDetails.getUsername());
        return ResponseEntity.ok(topInsights);
    }

    // 인사이트 목록 조회
    @Override
    @GetMapping("/insights/list")
    public ResponseEntity<InsightResponseDto.InsightListPageRes> getInsights(
            @Parameter(description = "세션 날짜 (선택)", required = false)
            @RequestParam(value ="eventDay",  required = false) LocalDate eventDay,
            @RequestParam(value = "sessionId", required = false) Long sessionId,
            @RequestParam(value = "sort", defaultValue = "latest") String sort,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        InsightResponseDto.InsightListPageRes insights = insightService.getInsightsByEventDay(eventDay, sessionId, sort, page ,userDetails.getUsername());
        return ResponseEntity.ok(insights);
    }

    // 인사이트 상세 페이지
    @Override
    @GetMapping("/insights/{insightId}")
    public ResponseEntity<InsightResponseDto.InsightDetailPageRes> getInsightDetail(
            @PathVariable Long insightId,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        InsightResponseDto.InsightDetailPageRes insightDetail = insightService.getInsightDetail(insightId,userDetails.getUsername());
        return ResponseEntity.ok(insightDetail);
    }

    // 특정 세션의 인사이트 목록 조회
    @Override
    @GetMapping("sessions/{sessionId}/insight-notes")
    public ResponseEntity<InsightResponseDto.SessionInsightListPageRes> getInsightsBySession(
            @PathVariable Long sessionId,
            @RequestParam(defaultValue = "latest") String sort,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        Pageable pageable = PageRequest.of(page, size);
        InsightResponseDto.SessionInsightListPageRes response = insightService.getInsightsBySession(sessionId, sort, pageable, userDetails.getUsername());
        return ResponseEntity.ok(response);
    }

    @Override
    @GetMapping("/my/insights")
    public ResponseEntity<InsightResponseDto.MyInsightListPageRes> getMyInsights(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestParam(required = false) LocalDate eventDay,
            @RequestParam(required = false) Long sessionId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        InsightResponseDto.MyInsightListPageRes response = insightService.getMyInsights(userDetails.getUsername(), eventDay, sessionId, pageable);
        return ResponseEntity.ok(response);
    }
}
