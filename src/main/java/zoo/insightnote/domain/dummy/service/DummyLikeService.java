package zoo.insightnote.domain.dummy.service;

import lombok.RequiredArgsConstructor;
import net.datafaker.Faker;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import zoo.insightnote.domain.InsightLike.entity.InsightLike;
import zoo.insightnote.domain.InsightLike.repository.InsightLikeRepository;
import zoo.insightnote.domain.insight.entity.Insight;
import zoo.insightnote.domain.insight.repository.InsightRepository;
import zoo.insightnote.domain.user.entity.User;
import zoo.insightnote.domain.user.repository.UserRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DummyLikeService {

    private final UserRepository userRepository;
    private final InsightRepository insightRepository;
    private final InsightLikeRepository insightLikeRepository;

    private final Faker faker = new Faker();

    @Transactional
    public void generateLikes(int count) {
        List<User> users = userRepository.findAll();
        List<Insight> insights = insightRepository.findAll();

        for (int i = 0; i < count; i++) {
            User user = users.get(faker.random().nextInt(users.size()));
            Insight insight = insights.get(faker.random().nextInt(insights.size()));

            // 유저 본인이 작성한 인사이트는 제외 (옵션)
            if (insight.getUser().getId().equals(user.getId())) continue;

            boolean alreadyLiked = insightLikeRepository.existsByUserAndInsight(user, insight);
            if (alreadyLiked) continue;

            InsightLike like = InsightLike.create(user, insight);
            insightLikeRepository.save(like);
        }
    }
}