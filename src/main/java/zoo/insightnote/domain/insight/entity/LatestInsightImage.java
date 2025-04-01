package zoo.insightnote.domain.insight.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

import org.hibernate.annotations.Immutable;
import org.hibernate.annotations.Subselect;

@Entity
@Subselect("select * from latest_insight_image")
@Immutable
public class LatestInsightImage {
    @Id
    private Long entityId;
    private String fileUrl;
}