package zoo.insightnote.domain.user.service;

import static zoo.insightnote.domain.user.entity.Role.*;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import zoo.insightnote.domain.user.dto.request.JoinDto;
import zoo.insightnote.domain.user.entity.User;
import zoo.insightnote.domain.user.repository.UserRepository;
import zoo.insightnote.global.exception.CustomException;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public void joinProcess(JoinDto joinDto) {

        String name = joinDto.getName();
        String email = joinDto.getEmail();

        boolean isExist = userRepository.existsByNameAndEmail(name, email);
        if (isExist) {
            throw new CustomException(null, "이미 가입된 사용자입니다.");
        }

        User user = User.builder()
                .name(name)
                .email(email)
                .role(GUEST)
                .build();
        userRepository.save(user);
    }

    public User findByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new CustomException(null, "사용자를 찾을 수 없습니다."));
    }

    public User findUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException(null, "사용자를 찾을 수 없습니다."));
    }
}
