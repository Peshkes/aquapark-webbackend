package ru.kikopark.backend.persistence.authentication.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "accounts", schema = "public")
public class AccountEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Integer userId;
    @Column(name = "role_id")
    private Integer roleId;
    private String name;
    private String password;
    private String email;

    public AccountEntity(){};

    public AccountEntity(Integer roleId, String name, String password, String email) {
        this.roleId = roleId;
        this.name = name;
        this.password = password;
        this.email = email;
    }

    public Integer getUserId() {
        return userId;
    }

    public Integer getRoleId() {
        return roleId;
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }
}
