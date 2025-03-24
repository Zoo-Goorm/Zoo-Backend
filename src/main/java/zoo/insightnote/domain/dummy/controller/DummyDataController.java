package zoo.insightnote.domain.dummy.controller;

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
public class DummyDataController {

    private final DummyUserService dummyUserService;
    private final DummyInsightService dummyInsightService;

    @PostMapping("/users")
    public ResponseEntity<String> generateDummyUsers(@RequestParam int count) {
        dummyUserService.generateUsers(count);
        return ResponseEntity.ok(count + "명의 유저가 생성되었습니다.");
    }

    @PostMapping("/insights")
    public ResponseEntity<String> generateDummyInsights(@RequestParam int count) {
        dummyInsightService.generateInsights(count);
        return ResponseEntity.ok(count + "개의 인사이트가 생성되었습니다.");
    }
}