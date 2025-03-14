package zoo.insightnote.domain.keyword.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import zoo.insightnote.domain.keyword.dto.KeywordResponseDto;
import zoo.insightnote.domain.keyword.service.KeywordService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/keywords")
public class KeywordController {

    private final KeywordService keywordService;

    @GetMapping
    public ResponseEntity<List<KeywordResponseDto>> getAllkeywords() {
        List<KeywordResponseDto> keywords = keywordService.getAllKeywords();
        return ResponseEntity.ok(keywords);
    }
}