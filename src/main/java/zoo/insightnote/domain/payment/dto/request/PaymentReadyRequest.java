package zoo.insightnote.domain.payment.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.util.List;

public record PaymentReadyRequest(
        @NotBlank String itemName,
        @Positive int totalAmount,
        @Positive int quantity,
        @NotEmpty List<@NotNull Long> sessionIds,
        @NotNull @Valid UserInfo userInfo
) {}