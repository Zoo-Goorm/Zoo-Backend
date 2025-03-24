package zoo.insightnote.domain.dummy.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import zoo.insightnote.domain.dummy.service.DummyInsightService;
import zoo.insightnote.domain.dummy.service.DummyUserService;

@RestController
@RequestMapping("/dummy")
@RequiredArgsConstructor
@Tag(name = "01-더미 데이터 생성", description = "유저 및 인사이트 더미 데이터를 생성하는 API")
public class DummyDataController {

    private final DummyUserService dummyUserService;
    private final DummyInsightService dummyInsightService;

    @PostMapping("/users")
    @Operation(summary = "더미 유저 생성", description = "입력한 수만큼 랜덤 유저 데이터를 생성합니다.")
    public ResponseEntity<String> generateDummyUsers(
            @Parameter(description = "생성할 유저 수", example = "100") @RequestParam int count)
    {
        dummyUserService.generateUsers(count);
        return ResponseEntity.ok(count + "명의 유저가 생성되었습니다.");
    }

    @PostMapping("/insights")
    @Operation(summary = "더미 인사이트 생성", description = "입력한 수만큼 랜덤 인사이트 데이터를 생성합니다.")
    public ResponseEntity<String> generateDummyInsights(
            @Parameter(description = "생성할 인사이트 수", example = "200") @RequestParam int count)
    {
        dummyInsightService.generateInsights(count);
        return ResponseEntity.ok(count + "개의 인사이트가 생성되었습니다.");
    }
}