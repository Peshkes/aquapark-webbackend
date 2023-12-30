package ru.kikopark.backend.model.authentication;

public class AccountRequest {
    private String name;
    private String email;
    private String password;
    private int roleId;

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public int getRoleId() {
        return roleId;
    }
}
