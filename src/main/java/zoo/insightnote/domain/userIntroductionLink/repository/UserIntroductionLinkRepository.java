package zoo.insightnote.domain.userIntroductionLink.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import zoo.insightnote.domain.user.entity.User;
import zoo.insightnote.domain.userIntroductionLink.entity.UserIntroductionLink;

import java.util.List;

@Repository
public interface UserIntroductionLinkRepository extends JpaRepository<UserIntroductionLink, Long> {
    List<UserIntroductionLink> findByUser(User user);
}