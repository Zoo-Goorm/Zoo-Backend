package zoo.insightnote.domain.user.dto.response;

import lombok.Builder;

@Builder
public record PaymentUserInfoResponse(
        Long userId,
        String name,
        String email
) { }