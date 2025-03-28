package zoo.insightnote.domain.dummy.service;

import lombok.RequiredArgsConstructor;
import net.datafaker.Faker;
import org.springframework.stereotype.Service;
import zoo.insightnote.domain.user.entity.Role;
import zoo.insightnote.domain.user.entity.User;
import zoo.insightnote.domain.user.repository.UserRepository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import static zoo.insightnote.domain.dummy.data.UserDummyData.*;

@Service
@RequiredArgsConstructor
public class DummyUserService {

    private final UserRepository userRepository;
    private final Faker faker = new Faker(new Locale("ko"));

    public void generateUsers(int count) {
        for (int i = 1; i <= count; i++) {
            String index = String.valueOf(System.currentTimeMillis() + i);

            List<String> shuffled = new ArrayList<>(INTEREST_CATEGORIES);
            Collections.shuffle(shuffled);
            String interests = String.join(", ", shuffled.subList(0, 3));
            String nickname = NICKNAMES.get(faker.random().nextInt(NICKNAMES.size()));

            User user = User.builder()
                    .username("google " + index)
                    .name(faker.name().lastName() + faker.name().firstName())
                    .email("user" + index + "@example.com")
                    .nickname(nickname)
                    .phoneNumber("010-" + faker.number().digits(4) + "-" + faker.number().digits(4))
                    .job(JOBS.get(faker.random().nextInt(JOBS.size())))
                    .occupation(OCCUPATIONS.get(faker.random().nextInt(OCCUPATIONS.size())))
                    .interestCategory(interests)
                    .role(faker.bool().bool() ? Role.USER : Role.GUEST)
                    .build();

            userRepository.save(user);
        }
    }
}