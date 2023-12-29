package ru.kikopark.backend.persistence.authentication.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "roles", schema = "public")
public class RoleEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "role_id")
    private Integer roleId;
    @Column(name = "role_name")
    private String roleName;

    public String getRoleName() {
        return roleName;
    }
}
