package zoo.insightnote.domain.keyword.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import zoo.insightnote.domain.keyword.entity.Keyword;
import zoo.insightnote.domain.keyword.repository.KeywordRepository;

@Service
@RequiredArgsConstructor
public class KeywordService {

    private final KeywordRepository keywordRepository;

    @Transactional
    public Keyword findOrCreateByName(String keywordName) {
        return keywordRepository.findByName(keywordName)
                .orElseGet(() -> keywordRepository.save(Keyword.create(keywordName)));
    }

}