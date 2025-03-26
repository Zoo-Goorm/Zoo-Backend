package zoo.insightnote.domain.user.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PaymentUserInfoResponseDto {
    private Long userId;
    private String name;
    private String email;
}
