package zoo.insightnote.domain.payment.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import zoo.insightnote.domain.event.entity.Event;
import zoo.insightnote.domain.session.entity.Session;
import zoo.insightnote.domain.user.entity.User;
import zoo.insightnote.global.entity.BaseTimeEntity;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Payment extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "event_id", nullable = false)
    private Event event;

    private String tid;
    private int amount;
    private Boolean checkedEvent;
    private Boolean isOnline;

    @Enumerated(EnumType.STRING)
    private PaymentStatus paymentStatus;

    public static Payment create(User user, Session session, String tid, int amount, Boolean isOnline) {
        return Payment.builder()
                .user(user)
                .event(session.getEvent())
                .tid(tid)
                .amount(amount)
                .isOnline(isOnline)
                .checkedEvent(Boolean.FALSE)
                .paymentStatus(PaymentStatus.COMPLETED)
                .build();
    }

    public void update() {
        if (isChanged(this.checkedEvent, Boolean.TRUE)) this.checkedEvent = true;
    }

    private boolean isChanged(Object currentValue, Object newValue) {
        return newValue != null && !newValue.equals(currentValue);
    }
}
