package za.co.wethinkcode.gadgethomeserver.models.domain;

import javax.persistence.Id;

import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

@RedisHash("blacklist")
public class Blacklist {
    @Id String id;

    @Indexed
    private String username;

    @Indexed
    private String token;

    public Blacklist(String username, String token) {
        this.username = username;
        this.token = token;
    }

    public String getUsername() {
        return username;
    }

    public String getToken() {
        return token;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setToken(String token) {
        this.token = token;
    }

}
