package zoo.insightnote.domain.keyword.dto;

import lombok.Getter;

@Getter
public class KeywordResponseDto {
    private Long id;
    private String name;

    public KeywordResponseDto(Long id, String name) {
        this.id = id;
        this.name = name;
    }
}