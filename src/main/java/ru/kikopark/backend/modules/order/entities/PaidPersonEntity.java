package ru.kikopark.backend.modules.order.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name = "paid_person", schema = "public")
public class PaidPersonEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "person_id")
    private UUID personId;
    @Column(name = "full_name")
    private String name;
    @Column(name = "email")
    private String email;

    public PaidPersonEntity(String name, String email) {
        this.name = name;
        this.email = email;
    }
}


