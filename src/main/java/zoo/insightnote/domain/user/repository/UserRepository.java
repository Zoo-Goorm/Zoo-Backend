package zoo.insightnote.domain.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import zoo.insightnote.domain.user.entity.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    User findByUsername(String username);
    Optional<User> findByEmail(String email);
}
