package zoo.insightnote.domain.userIntroductionLink.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import zoo.insightnote.domain.userIntroductionLink.entity.UserIntroductionLink;

@Repository
public interface IntroductionLinkRepository extends JpaRepository<UserIntroductionLink, Long> {

}