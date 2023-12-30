package ru.kikopark.backend.persistence.order.entities;

import jakarta.persistence.*;

@Entity
@Table(schema = "public", name = "types")
public class TypeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "type_id")
    Integer typeId;
    String name;

    public Integer getTypeId() {
        return typeId;
    }

    public String getName() {
        return name;
    }
}
