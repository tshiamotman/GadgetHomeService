package za.co.wethinkcode.gadgethomeserver.models.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Blacklist {
    @Id
    @GeneratedValue()
    public String id;

    private String username;

    private String token;

    public Blacklist(String username, String token) {
        this.username = username;
        this.token = token;
    }

    public Blacklist() {

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
