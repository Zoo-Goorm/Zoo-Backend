package zoo.insightnote.domain.user.mapper;

import org.springframework.stereotype.Service;
import zoo.insightnote.domain.user.dto.response.UserInfoResponse;
import zoo.insightnote.domain.user.entity.User;

@Service
public class UserMapper {

    public UserInfoResponse toResponse(User user) {
        return UserInfoResponse.builder()
                .name(user.getName())
                .email(user.getEmail())
                .phoneNumber(user.getPhoneNumber())
                .job(user.getJob())
                .occupation(user.getOccupation())
                .interestCategory(user.getInterestCategory())
                .build();
    }
}
