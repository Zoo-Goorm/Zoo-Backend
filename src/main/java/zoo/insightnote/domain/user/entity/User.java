package zoo.insightnote.domain.user.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
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
}
