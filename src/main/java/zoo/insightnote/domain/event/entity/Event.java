package zoo.insightnote.domain.event.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.time.LocalDateTime;

import lombok.*;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String name;
    private String description;
    private String location;
    private LocalDateTime startTime;
    private LocalDateTime endTime;

    @Builder
    public Event(String name, String description, String location, LocalDateTime startTime, LocalDateTime endTime) {
        this.name = name;
        this.description = description;
        this.location = location;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public void update(String name, String description, String location, LocalDateTime startTime, LocalDateTime endTime) {
        if (isChanged(this.name, name)) this.name = name;
        if (isChanged(this.description, description)) this.description = description;
        if (isChanged(this.location, location)) this.location = location;
        if (isChanged(this.startTime, startTime)) this.startTime = startTime;
        if (isChanged(this.endTime, endTime)) this.endTime = endTime;
    }

    private boolean isChanged(Object currentValue, Object newValue) {
        return newValue != null && !newValue.equals(currentValue);
    }
}
