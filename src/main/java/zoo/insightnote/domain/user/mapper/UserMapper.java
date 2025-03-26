package zoo.insightnote.domain.user.mapper;

import org.springframework.stereotype.Service;
import zoo.insightnote.domain.user.dto.response.UserInfoResponse;
import zoo.insightnote.domain.user.entity.User;

@Service
public class UserMapper {

    public UserInfoResponse toResponse(User user) {
        return UserInfoResponse.builder()
                .name(user.getName())
                .nickname(user.getNickname())
                .email(user.getEmail())
                .phoneNumber(user.getPhoneNumber())
                .occupation(user.getOccupation())
                .job(user.getJob())
                .interestCategory(user.getInterestCategory())
                .build();
    }
}
