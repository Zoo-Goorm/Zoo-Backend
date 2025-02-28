package zoo.insightnote.domain.user.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String email;

    private String nickname;

    private String phoneNumber;

    private String job;

    private String occupation;

    private String interestCategory;

    @Enumerated(EnumType.STRING)
    private Role role;

}
