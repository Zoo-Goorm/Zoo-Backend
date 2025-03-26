package zoo.insightnote.domain.dummy.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import net.datafaker.Faker;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import zoo.insightnote.domain.comment.repository.CommentRepository;
import zoo.insightnote.domain.dummy.data.CommentDummyData;
import zoo.insightnote.domain.insight.entity.Insight;
import zoo.insightnote.domain.insight.repository.InsightRepository;
import zoo.insightnote.domain.user.entity.User;
import zoo.insightnote.domain.user.repository.UserRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class DummyCommentService {
    private final UserRepository userRepository;
    private final InsightRepository insightRepository;
    private final CommentRepository commentRepository;
    private final Faker faker = new Faker();
    private final Random random = new Random();

    @PersistenceContext
    private EntityManager em;

    @Transactional
    public void generateComments(int count) {
        List<User> users = userRepository.findAll();
        List<Insight> insights = insightRepository.findAll();
        List<String> sampleComments = CommentDummyData.SAMPLE_COMMENTS;

        for (int i = 0; i < count; i++) {
            User user = users.get(faker.random().nextInt(users.size()));
            Insight insight = insights.get(faker.random().nextInt(insights.size()));

            // 세션 ID 기준으로 날짜 결정
            Long sessionId = insight.getSession().getId();
            LocalDate baseDate = sessionId <= 15 ? LocalDate.of(2025, 4, 3) : LocalDate.of(2025, 4, 4);
            LocalTime randomTime = LocalTime.of(random.nextInt(24), random.nextInt(60));
            LocalDateTime createdAt = LocalDateTime.of(baseDate, randomTime);

            String commentContent = sampleComments.get(faker.random().nextInt(sampleComments.size()));

            // Native Query로 직접 INSERT
            em.createNativeQuery("INSERT INTO comment (user_id, insight_id, content, create_at, updated_at) " +
                            "VALUES (:userId, :insightId, :content, :createdAt, :updatedAt)")
                    .setParameter("userId", user.getId())
                    .setParameter("insightId", insight.getId())
                    .setParameter("content", commentContent)
                    .setParameter("createdAt", createdAt)
                    .setParameter("updatedAt", createdAt)
                    .executeUpdate();
        }
    }
}