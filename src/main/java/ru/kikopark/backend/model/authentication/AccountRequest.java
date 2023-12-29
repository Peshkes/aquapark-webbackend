package ru.kikopark.backend.model.authentication;

public class AccountRequest {
    private String name;
    private String email;
    private String password;
    private int roleId;

    public AccountRequest(String name, String email, String password, int roleId) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.roleId = roleId;
    }

    public AccountRequest() {
    }

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
