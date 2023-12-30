package ru.kikopark.backend.persistence.order.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "statuses", schema = "public")
public class StatusEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "status_id")
    private Integer statusId;
    private String name;

    public Integer getStatusId() {
        return statusId;
    }

    public String getName() {
        return name;
    }
}
