package ru.kikopark.backend.model;

public class AccountResponse {
    private String name;
    private String roleName;

    public AccountResponse(String name, String roleName) {
        this.name = name;
        this.roleName = roleName;
    }

    public AccountResponse() {
    }

    public String getName() {
        return name;
    }

    public String getRoleName() {
        return roleName;
    }
}
