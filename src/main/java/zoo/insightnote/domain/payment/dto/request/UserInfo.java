package zoo.insightnote.domain.payment.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;

public record UserInfo(
        @NotBlank String name,
        @NotBlank String email,
        @NotBlank String phoneNumber,
        @NotBlank String job,              // 직업
        @NotBlank String occupation,       // 직군
        @NotBlank String interestCategory,
        @JsonProperty("online") boolean isOnline
) {}

