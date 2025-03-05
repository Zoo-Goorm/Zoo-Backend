package zoo.insightnote.domain.payment.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class PaymentRequestDto {
    @NotBlank
    private Long orderId;

    @NotBlank
    private Long userId;

    @NotBlank
    private String itemName;

    @NotBlank
    private int totalAmount;

    @NotBlank
    private int quantity;
}
