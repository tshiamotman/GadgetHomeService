package za.co.wethinkcode.gadgethomeserver.models.domain;

import javax.persistence.Id;

import org.springframework.data.redis.core.RedisHash;

@RedisHash("blacklist")
public class Blacklist {
    @Id
    private String username;

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
