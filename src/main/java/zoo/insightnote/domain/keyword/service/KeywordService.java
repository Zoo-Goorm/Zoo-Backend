package zoo.insightnote.domain.keyword.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import zoo.insightnote.domain.keyword.dto.KeywordResponseDto;
import zoo.insightnote.domain.keyword.entity.Keyword;
import zoo.insightnote.domain.keyword.mapper.KeywordMapper;
import zoo.insightnote.domain.keyword.repository.KeywordRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class KeywordService {

    private final KeywordRepository keywordRepository;

    @Transactional
    public Keyword findOrCreateByName(String keywordName) {
        return keywordRepository.findByName(keywordName)
                .orElseGet(() -> keywordRepository.save(Keyword.create(keywordName)));
    }

    public List<KeywordResponseDto> getAllKeywords () {
        return keywordRepository.findAll(Sort.by(Sort.Direction.ASC, "id")).stream()
                .map(KeywordMapper::toResponse)
                .collect((Collectors.toList()));
    }
}