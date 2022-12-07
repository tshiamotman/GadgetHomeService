package za.co.wethinkcode.gadgethomeserver.models.domain;

import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import za.co.wethinkcode.gadgethomeserver.models.database.User;

@Entity
public class RefreshToken {
    @Id
    @GeneratedValue()
    private String id;

    @Column(name = "token")
    private String token;

    @Column(name = "user_id")
    private String user;

    public RefreshToken(User user) {
        this.user = user.getUserName();
        this.token = UUID.randomUUID().toString();
    }

    public RefreshToken() {

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
