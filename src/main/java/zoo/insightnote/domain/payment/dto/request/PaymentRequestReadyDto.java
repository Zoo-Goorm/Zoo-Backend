package zoo.insightnote.domain.payment.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class PaymentRequestReadyDto {
    @NotNull
    private Long userId;

    @NotBlank
    private String itemName;

    @Positive()
    private int totalAmount;

    @Positive()
    private int quantity;

    @NotEmpty
    private List<Long> sessionIds;
}
