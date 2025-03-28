package zoo.insightnote.domain.user.entity;

import static zoo.insightnote.domain.user.entity.Role.*;

import jakarta.persistence.Column;
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
@Builder
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String username;

    private String name;

    private String email;

    private String nickname;

    private String phoneNumber;

    private String job;

    private String occupation;

    private String interestCategory;

    @Enumerated(EnumType.STRING)
    private Role role;

    private String snsUrl;

    public User(String username, String name, String email, Role role) {
        this.username = username;
        this.name = name;
        this.email = email;
        this.role = role;
    }

    public void updateUsername(String username) {
        this.username = username;
        this.role = USER;
    }

    public void update(String email, String name) {
        this.email = email;
        this.name = name;
    }

    public void update(String name, String phoneNumber, String job, String occupation, String interestCategory) {
        if (isChanged(this.name, name)) this.name = name;
        if (isChanged(this.phoneNumber, phoneNumber)) this.phoneNumber = phoneNumber;
        if (isChanged(this.job, job)) this.job = job;
        if (isChanged(this.occupation, occupation)) this.occupation = occupation;
        if (isChanged(this.interestCategory, interestCategory)) this.interestCategory = interestCategory;
    }

    public void update(String name, String nickname, String phoneNumber, String occupation, String job, String interestCategory, String snsUrl) {
        if (isChanged(this.name, name)) this.name = name;
        if (isChanged(this.nickname, nickname)) this.nickname = nickname;
        if (isChanged(this.phoneNumber, phoneNumber)) this.phoneNumber = phoneNumber;
        if (isChanged(this.occupation, occupation)) this.occupation = occupation;
        if (isChanged(this.job, job)) this.job = job;
        if (isChanged(this.interestCategory, interestCategory)) this.interestCategory = interestCategory;
        if (isChanged(this.snsUrl, snsUrl)) this.snsUrl = snsUrl;
    }

    public void anonymizeUserData() {
        this.username = null;
        this.name = "알 수 없음";
        this.nickname = "알 수 없음";
    }

    private boolean isChanged(Object currentValue, Object newValue) {
        return newValue != null && !newValue.equals(currentValue);
    }
}
