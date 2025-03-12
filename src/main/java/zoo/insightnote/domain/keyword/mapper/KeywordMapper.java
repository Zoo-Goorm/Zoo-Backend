package zoo.insightnote.domain.keyword.mapper;

import zoo.insightnote.domain.keyword.dto.KeywordResponseDto;
import zoo.insightnote.domain.keyword.entity.Keyword;

public class KeywordMapper {

    public static KeywordResponseDto toResponse(Keyword keyword) {
        return new KeywordResponseDto(keyword.getId(), keyword.getName());
    }
}