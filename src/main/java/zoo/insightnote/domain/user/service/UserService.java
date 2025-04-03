package zoo.insightnote.domain.user.service;

import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import zoo.insightnote.domain.email.service.EmailVerificationService;
import org.springframework.transaction.annotation.Transactional;
import zoo.insightnote.domain.payment.dto.request.UserInfo;
import zoo.insightnote.domain.user.dto.request.UserInfoRequest;
import zoo.insightnote.domain.user.dto.response.PaymentUserInfoResponse;
import zoo.insightnote.domain.user.dto.response.UserInfoResponse;
import zoo.insightnote.domain.user.entity.Role;
import zoo.insightnote.domain.user.entity.User;
import zoo.insightnote.domain.user.repository.UserRepository;
import zoo.insightnote.global.exception.CustomException;
import zoo.insightnote.global.exception.ErrorCode;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final EmailVerificationService emailVerificationService;

    public void autoRegisterAndLogin(String name, String email, String code) {
        verifyCode(email, code);

        Optional<User> optionalUser = userRepository.findByUsername(email);
        if (!optionalUser.isPresent()) {
            User user = User.builder()
                    .name(name)
                    .email(email)
                    .username(email)
                    .role(Role.GUEST)
                    .build();
            userRepository.save(user);
        }
    }

    private void verifyCode(String email, String code) {
        boolean isVerified = emailVerificationService.verifyCode(email, code);
        if (!isVerified) {
            throw new CustomException(ErrorCode.INVALID_VERIFICATION_CODE);
        }
    }

    private void existUser(String email) {
        boolean isExist = userRepository.existsByUsername(email);
        if (isExist) {
            throw new CustomException(ErrorCode.ALREADY_EXIST_USER);
        }
    }

    public User findByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
    }

    public PaymentUserInfoResponse getPaymentUserInfo(String username) {
        User user = findByUsername(username);

        PaymentUserInfoResponse response = PaymentUserInfoResponse.builder()
                .userId(user.getId())
                .email(user.getEmail())
                .name(user.getName())
                .build();

        return response;
    }

    public void updateUserInfo(UserInfo userInfo, User user) {
        user.update(
                userInfo.name(),
                userInfo.phoneNumber(),
                userInfo.job(),              // 직업
                userInfo.occupation(),       // 직군
                userInfo.interestCategory()
        );
        userRepository.save(user);
    }

    @Transactional(readOnly = true)
    public UserInfoResponse getUserInfo(String username) {
        User user = findByUsername(username);
        return UserInfoResponse.from(user);
    }

    @Transactional
    public UserInfoResponse updateUserInfo(UserInfoRequest userInfoRequest, String username) {
        User user = findByUsername(username);
        user.update(
                userInfoRequest.name(),
                userInfoRequest.nickname(),
                userInfoRequest.phoneNumber(),
                userInfoRequest.occupation(),
                userInfoRequest.job(),
                userInfoRequest.interestCategory(),
                userInfoRequest.snsUrl()
        );
        return UserInfoResponse.from(user);
    }

    @Transactional
    public UserInfoResponse anonymizeUserInfo(String username) {
        User user = findByUsername(username);
        user.anonymizeUserData();
        return UserInfoResponse.from(user);
    }
}
