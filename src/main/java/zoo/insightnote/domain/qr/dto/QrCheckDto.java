package zoo.insightnote.domain.qr.dto;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class QrCheckDto {
    private String name;
    private String InfoName;
}
