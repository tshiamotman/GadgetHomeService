package za.co.wethinkcode.gadgethomeserver.models.domain;

import javax.xml.bind.annotation.XmlElement;

import com.fasterxml.jackson.annotation.JsonProperty;

public class UserDto {
    @JsonProperty("user_name")
    private String userName;

    @XmlElement(nillable = true)
    @JsonProperty("first_name")
    private String firstName;

    @XmlElement(nillable = true)
    @JsonProperty("last_name")
    private String lastName;

    @XmlElement(nillable = true)
    @JsonProperty("password")
    private String password;

    @XmlElement(nillable = true)
    @JsonProperty("number")
    private String number;

    @XmlElement(nillable = true)
    @JsonProperty("email")
    private String email;

    @XmlElement(nillable = true)
    @JsonProperty("role")
    private String role;

    public UserDto() {
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

}
