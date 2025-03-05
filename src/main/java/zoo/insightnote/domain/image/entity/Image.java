package zoo.insightnote.domain.image.entity;

import jakarta.persistence.*;
import zoo.insightnote.global.entity.BaseTimeEntity;

@Entity

public class Image extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String fileName;

    private String fileUrl;

    private Long entityId;

    @Enumerated(EnumType.STRING)
    private EntityType entityType; // 이미지가 속한 도메인 (EVENT, SESSION, SPEAKER, INSIGHT)


    public static Image of(String fileName, String fileUrl, Long entityId, EntityType entityType) {
        Image image = new Image();
        image.fileName = fileName;
        image.fileUrl = fileUrl;
        image.entityId = entityId;
        image.entityType = entityType;
        return image;
    }
}