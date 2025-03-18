package zoo.insightnote.domain.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import zoo.insightnote.domain.user.entity.User;
import zoo.insightnote.domain.user.repository.UserRepository;
import zoo.insightnote.global.exception.CustomException;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public User findByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new CustomException(null, "사용자를 찾을 수 없습니다."));
    }

    public User findUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException(null, "사용자를 찾을 수 없습니다."));
    }
}
