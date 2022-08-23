package za.co.wethinkcode.gadgethomeserver.models.domain;

import java.util.Map;

public class AuthenticationDto {
    
    private Boolean error;

    private String message;

    private String token;

    private String refreshToken;

    private String user;

    public AuthenticationDto(Boolean error, String message) {
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

    public AuthenticationDto setToken(String token) {
        this.token = token;
        return this;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public AuthenticationDto setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
        return this;
    }

    public String getUser() {
        return user;
    }

    public AuthenticationDto setUser(String user) {
        this.user = user;
        return this;
    }

    public Map<String, Object> build() {
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
