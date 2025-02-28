package zoo.insightnote.global.entity;

import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.EntityListeners;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Getter
@Setter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public class BaseTimeEntity {
    private LocalDateTime createAt;
    private LocalDateTime updatedAt;
    private LocalDateTime deletedAt;
}
