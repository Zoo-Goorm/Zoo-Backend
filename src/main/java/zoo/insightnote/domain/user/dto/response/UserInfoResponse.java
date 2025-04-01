package zoo.insightnote.domain.user.dto.response;

import lombok.Builder;
import zoo.insightnote.domain.user.entity.User;

@Builder
public record UserInfoResponse(
        String name,
        String nickname,
        String email,
        String phoneNumber,
        String occupation,
        String job,
        String interestCategory,
        String snsUrl,
        String username
) {
    public static UserInfoResponse from(User user) {
        return UserInfoResponse.builder()
                .name(user.getName())
                .nickname(user.getNickname())
                .email(user.getEmail())
                .phoneNumber(user.getPhoneNumber())
                .occupation(user.getOccupation())
                .job(user.getJob())
                .interestCategory(user.getInterestCategory())
                .username(user.getUsername())
                .snsUrl(user.getSnsUrl())
                .build();
    }
}