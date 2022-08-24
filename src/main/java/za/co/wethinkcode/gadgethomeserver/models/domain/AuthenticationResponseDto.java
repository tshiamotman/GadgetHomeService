package za.co.wethinkcode.gadgethomeserver.models.domain;

import java.util.Map;

public class AuthenticationResponseDto {
    
    private Boolean error;

    private String message;

    private String token;

    private String refreshToken;

    private String user;

    public AuthenticationResponseDto(Boolean error, String message) {
        this.error = error;
        this.message = message;
    }

    public Boolean getError() {
        return error;
    }

    public void setError(Boolean error) {
        this.error = error;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getToken() {
        return token;
    }

    public AuthenticationResponseDto setToken(String token) {
        this.token = token;
        return this;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public AuthenticationResponseDto setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
        return this;
    }

    public String getUser() {
        return user;
    }

    public AuthenticationResponseDto setUser(String user) {
        this.user = user;
        return this;
    }

    public Map<String, Object> getResponseBody() {
        if(this.token != null && this.refreshToken != null && this.user != null) {
            return Map.of(
                "error", error,
                "message", message,
                "token", this.token,
                "refresh", this.refreshToken,
                "user", user
            );
        }
        return Map.of("error", error, "message", message);
    }
}
