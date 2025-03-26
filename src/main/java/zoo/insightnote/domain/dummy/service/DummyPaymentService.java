package zoo.insightnote.domain.dummy.service;

import lombok.RequiredArgsConstructor;
import net.datafaker.Faker;
import org.springframework.stereotype.Service;
import zoo.insightnote.domain.event.entity.Event;
import zoo.insightnote.domain.payment.entity.Payment;
import zoo.insightnote.domain.payment.entity.PaymentStatus;
import zoo.insightnote.domain.payment.repository.PaymentRepository;
import zoo.insightnote.domain.reservation.entity.Reservation;
import zoo.insightnote.domain.reservation.repository.ReservationRepository;
import zoo.insightnote.domain.session.entity.Session;
import zoo.insightnote.domain.session.repository.SessionRepository;
import zoo.insightnote.domain.session.service.SessionService;
import zoo.insightnote.domain.user.entity.User;
import zoo.insightnote.domain.user.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;

import static zoo.insightnote.domain.dummy.data.PaymentDummyData.FristDay;
import static zoo.insightnote.domain.dummy.data.PaymentDummyData.SecoundDay;

@Service
@RequiredArgsConstructor
public class DummyPaymentService {
    private final UserRepository userRepository;
    private final SessionRepository sessionRepository;

    private final Faker faker = new Faker();
    private final PaymentRepository paymentRepository;
    private final ReservationRepository reservationRepository;
    private final SessionService sessionService;

    public void generatePayments(int count) {
        List<User> users = userRepository.findAll();

        for (int i = 0; i < count; i++) {
            User user = users.get(faker.random().nextInt(users.size()));

            List<Long> sessionList = new ArrayList<>();
            FristDay.forEach(group -> sessionList.add(group.get(faker.random().nextInt(group.size()))));
            SecoundDay.forEach(group -> sessionList.add(group.get(faker.random().nextInt(group.size()))));


            Session mainSession = sessionService.findSessionBySessionId(sessionList.get(1));
            Event event = mainSession.getEvent();

            Payment payment = Payment.builder()
                    .event(event)
                    .user(user)
                    .paymentStatus(PaymentStatus.COMPLETED)
                    .amount(50000)
                    .checkedEvent(Boolean.FALSE)
                    .isOnline(faker.bool().bool())
                    .build();

            paymentRepository.save(payment);

            for (Long sessionId : sessionList) {
                Session session = sessionRepository.findById(sessionId).orElseThrow();

                Reservation reservation = Reservation.builder()
                        .user(user)
                        .session(session)
                        .checked(Boolean.FALSE)
                        .build();

                reservationRepository.save(reservation);
            }
        }
    }
}
