package za.co.wethinkcode.gadgethomeserver.models.domain;

import java.util.UUID;

import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import za.co.wethinkcode.gadgethomeserver.models.database.User;

@RedisHash("refreshtoken")
public class RefreshToken {
    @Id
    private String token;

    private String user;

    public RefreshToken(User user) {
        this.user = user.getUserName();
        this.token = UUID.randomUUID().toString();
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }
    
}
