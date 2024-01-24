package ru.kikopark.backend.model.authentication;

import java.lang.reflect.Array;

public class AuthenticationResponse {
    private String accessToken;
    private String refreshToken;
    private String[] roles;

    public AuthenticationResponse() {
    }

    public AuthenticationResponse(String accessToken, String refreshToken, String[] roles) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.roles = roles;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public String[] getRoles() {
        return roles;
    }
}
