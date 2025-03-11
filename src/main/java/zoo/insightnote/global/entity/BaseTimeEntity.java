package zoo.insightnote.global.entity;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.EntityListeners;

import java.time.LocalDate;
import java.time.LocalDate;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Getter
@Setter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public class BaseTimeEntity {
    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDate createAt;

    @UpdateTimestamp
    @Column(nullable = false)
    private LocalDate updatedAt;

    private LocalDate deletedAt;
}
