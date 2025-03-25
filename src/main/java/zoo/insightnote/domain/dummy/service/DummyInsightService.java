package zoo.insightnote.domain.dummy.service;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import net.datafaker.Faker;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import zoo.insightnote.domain.insight.entity.Insight;
import zoo.insightnote.domain.insight.repository.InsightRepository;
import zoo.insightnote.domain.session.entity.Session;
import zoo.insightnote.domain.session.repository.SessionRepository;
import zoo.insightnote.domain.user.entity.User;
import zoo.insightnote.domain.user.repository.UserRepository;
import static zoo.insightnote.domain.dummy.data.InsightDummyData.INSIGHT_PHRASES;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class DummyInsightService {

    private final UserRepository userRepository;
    private final SessionRepository sessionRepository;
    private final Faker faker = new Faker();
    private final Random random = new Random();

    @PersistenceContext
    private EntityManager em;
    @Transactional
    public void generateInsights(int count) {
        List<User> users = userRepository.findAll();
        List<Session> sessions = sessionRepository.findAll();

        for (int i = 0; i < count; i++) {
            User user = users.get(faker.random().nextInt(users.size()));
            Session session = sessions.get(faker.random().nextInt(sessions.size()));

            LocalDate baseDate = session.getId() <= 15 ? LocalDate.of(2025, 4, 3) : LocalDate.of(2025, 4, 4);
            LocalTime randomTime = LocalTime.of(random.nextInt(24), random.nextInt(60));
            LocalDateTime randomDateTime = LocalDateTime.of(baseDate, randomTime);

            boolean isPublic = faker.bool().bool();
            boolean isAnonymous = faker.bool().bool();
            boolean isDraft = faker.bool().bool();
            String memo = generateInsightMemo();

            em.createNativeQuery("INSERT INTO insight (session_id, user_id, memo, is_public, is_anonymous, is_draft, create_at, updated_at) " +
                            "VALUES (:sessionId, :userId, :memo, :isPublic, :isAnonymous, :isDraft, :createdAt, :updatedAt)")
                    .setParameter("sessionId", session.getId())
                    .setParameter("userId", user.getId())
                    .setParameter("memo", memo)
                    .setParameter("isPublic", isPublic)
                    .setParameter("isAnonymous", isAnonymous)
                    .setParameter("isDraft", isDraft)
                    .setParameter("createdAt", randomDateTime)
                    .setParameter("updatedAt", randomDateTime)
                    .executeUpdate();
        }
    }

    private String generateInsightMemo() {
        return INSIGHT_PHRASES.get(faker.random().nextInt(INSIGHT_PHRASES.size()))
                + " " + faker.lorem().sentence();
    }
}