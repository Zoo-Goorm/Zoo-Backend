package zoo.insightnote.domain.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import zoo.insightnote.domain.user.entity.User;
import zoo.insightnote.domain.user.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public User findByUsername(String username) {
        return userRepository.findByUsername(username).orElse(null);
    }
}
