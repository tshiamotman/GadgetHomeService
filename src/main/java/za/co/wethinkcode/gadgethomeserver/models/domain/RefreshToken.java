package za.co.wethinkcode.gadgethomeserver.models.domain;

import java.util.UUID;

import javax.persistence.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

import za.co.wethinkcode.gadgethomeserver.models.database.User;

@RedisHash("refreshtoken")
public class RefreshToken {
    @Id String id;

    @Indexed
    private String token;

    @Indexed
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
    
}
