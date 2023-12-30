package ru.kikopark.backend.persistence.order.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "times", schema = "public")
public class TimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "time_id")
    Integer timeId;
    Integer minutes;

    public Integer getTimeId() {
        return timeId;
    }

    public Integer getMinutes() {
        return minutes;
    }
}
