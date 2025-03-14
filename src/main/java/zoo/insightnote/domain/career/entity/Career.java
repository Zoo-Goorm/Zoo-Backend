package zoo.insightnote.domain.career.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import zoo.insightnote.domain.speaker.entity.Speaker;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Career {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "speaker_id") // 외래키 설정
    private Speaker speaker;

    @Builder
    public Career(String description, Speaker speaker) {
        this.description = description;
        this.speaker = speaker;
    }
}