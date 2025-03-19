package zoo.insightnote.domain.user.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;

    private String name;

    private String email;

    private String nickname;

    private String phoneNumber;

    private String job;

    private String occupation;

    private String interestCategory;

    private Boolean isOnline;

    @Enumerated(EnumType.STRING)
    private Role role;

    @Enumerated(EnumType.STRING)
    private AuthProvider provider;

    @Builder
    public User(String username, String name, String email, Role role) {
        this.username = username;
        this.name = name;
        this.email = email;
        this.role = role;
    }

    public void update(String email, String name) {
        this.email = email;
        this.name = name;
    }

    public void update(String name, String phoneNumber, String job, String occupation, String interestCategory, Boolean isOnline) {
        if (isChanged(this.name, name)) this.name = name;
        if (isChanged(this.phoneNumber, phoneNumber)) this.phoneNumber = phoneNumber;
        if (isChanged(this.job, job)) this.job = job;
        if (isChanged(this.occupation, occupation)) this.occupation = occupation;
        if (isChanged(this.interestCategory, interestCategory)) this.interestCategory = interestCategory;
        if (isChanged(this.isOnline, isOnline)) this.isOnline = isOnline;

    }

    private boolean isChanged(Object currentValue, Object newValue) {
        return newValue != null && !newValue.equals(currentValue);
    }
}
