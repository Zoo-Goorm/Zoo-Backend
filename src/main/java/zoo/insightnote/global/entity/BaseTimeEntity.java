package zoo.insightnote.global.entity;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.EntityListeners;

import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Getter
@Setter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public class BaseTimeEntity {
    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createAt;

    @LastModifiedDate
    @Column(nullable = false)
    private LocalDateTime updatedAt;

    private LocalDateTime deletedAt;
}
