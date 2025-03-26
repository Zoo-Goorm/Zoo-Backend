package zoo.insightnote.domain.user.service;

import static zoo.insightnote.domain.user.entity.Role.*;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import zoo.insightnote.domain.payment.dto.etc.UserInfoDto;
import zoo.insightnote.domain.user.dto.request.JoinDto;
import zoo.insightnote.domain.user.dto.PaymentUserInfoResponseDto;
import zoo.insightnote.domain.user.entity.User;
import zoo.insightnote.domain.user.repository.UserRepository;
import zoo.insightnote.global.exception.CustomException;
import zoo.insightnote.global.exception.ErrorCode;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public void joinProcess(JoinDto joinDto) {

        String name = joinDto.getName();
        String email = joinDto.getEmail();

        existUser(email);

        User user = User.builder()
                .name(name)
                .email(email)
                .role(GUEST)
                .username(email)
                .build();
        userRepository.save(user);
    }

    public void existUser(String email) {
        boolean isExist = userRepository.existsByUsername(email);
        if (!isExist) {
            throw new CustomException(ErrorCode.ALREADY_EXIST_USER);
        }
    }

    public User findByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
    }

    public PaymentUserInfoResponseDto getPaymentUserInfo(String username) {
        User user = findByUsername(username);

        PaymentUserInfoResponseDto response = PaymentUserInfoResponseDto.builder()
                .userId(user.getId())
                .email(user.getEmail())
                .name(user.getName())
                .build();

        return response;
    }

    public void updateUserInfo(UserInfoDto userInfo, User user) {
        user.update(
                userInfo.getName(),
                userInfo.getPhoneNumber(),
                userInfo.getJob(),              // 직업
                userInfo.getOccupation(),       // 직군
                userInfo.getInterestCategory()
        );
        userRepository.save(user);
    }
}
