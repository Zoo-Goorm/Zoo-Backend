package zoo.insightnote.domain.insight.dto.response;

import lombok.Getter;

@Getter
public class InsightIdResponse {
    private Long id;

    public InsightIdResponse(Long id) {
        this.id =id;
    }
}