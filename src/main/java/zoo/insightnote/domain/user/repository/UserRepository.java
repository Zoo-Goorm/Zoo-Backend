package zoo.insightnote.domain.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import zoo.insightnote.domain.user.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {
}
