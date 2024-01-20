package ru.kikopark.backend.model.authentication;

public class AuthenticationResponse {
    private String token;

    public AuthenticationResponse(String token) {
        this.token = token;
    }

    public AuthenticationResponse(){}

    public String getToken() {
        return token;
    }
}
