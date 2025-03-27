package zoo.insightnote.domain.user.service;

import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import zoo.insightnote.domain.email.service.EmailVerificationService;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import zoo.insightnote.domain.payment.dto.etc.UserInfoDto;
import zoo.insightnote.domain.user.dto.request.UserInfoRequest;
import zoo.insightnote.domain.user.dto.response.PaymentUserInfoResponseDto;
import zoo.insightnote.domain.user.dto.response.UserInfoResponse;
import zoo.insightnote.domain.user.entity.Role;
import zoo.insightnote.domain.user.entity.User;
import zoo.insightnote.domain.user.mapper.UserMapper;
import zoo.insightnote.domain.user.repository.UserRepository;
import zoo.insightnote.global.exception.CustomException;
import zoo.insightnote.global.exception.ErrorCode;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final EmailVerificationService emailVerificationService;
    private final UserMapper userMapper;

    public void autoRegisterAndLogin(String name, String email, String code) {
        verifyCode(email, code);

        Optional<User> optionalUser = userRepository.findByEmail(email);
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

    public User findUserByEmail(String email) {
        return userRepository.findByEmail(email)
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

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void updateUserInfo(UserInfoDto userInfo) {
        User user = findUserByEmail(userInfo.getEmail());
        user.update(
                userInfo.getName(),
                userInfo.getPhoneNumber(),
                userInfo.getJob(),              // 직업
                userInfo.getOccupation(),       // 직군
                userInfo.getInterestCategory()
        );
    }

    @Transactional(readOnly = true)
    public UserInfoResponse getUserInfo(String username) {
        User user = findByUsername(username);
        return userMapper.toResponse(user);
    }

    @Transactional
    public UserInfoResponse updateUserInfo(UserInfoRequest userInfoRequest, String username) {
        User user = findByUsername(username);
        user.update(
                userInfoRequest.getName(),
                userInfoRequest.getNickname(),
                userInfoRequest.getPhoneNumber(),
                userInfoRequest.getOccupation(),
                userInfoRequest.getJob(),
                userInfoRequest.getInterestCategory(),
                userInfoRequest.getSnsUrl()
        );
        return userMapper.toResponse(user);
    }
}
