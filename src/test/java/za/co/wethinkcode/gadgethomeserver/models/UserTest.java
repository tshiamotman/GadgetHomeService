package za.co.wethinkcode.gadgethomeserver.models;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class UserTest {
    @Test
    void testConstructor() {
        User actualUser = new User();
        actualUser.setEmail("jane.doe@example.org");
        actualUser.setFirstName("Jane");
        actualUser.setLastName("Doe");
        actualUser.setNumber("42");
        actualUser.setPassword("iloveyou");
        actualUser.setRole("Role");
        actualUser.setUserName("janedoe");
        assertEquals("jane.doe@example.org", actualUser.getEmail());
        assertEquals("Jane", actualUser.getFirstName());
        assertEquals("Doe", actualUser.getLastName());
        assertEquals("42", actualUser.getNumber());
        assertEquals("iloveyou", actualUser.getPassword());
        assertEquals("Role", actualUser.getRole());
        assertEquals("janedoe", actualUser.getUserName());
    }

    @Test
    void testConstructor2() {
        User actualUser = new User("janedoe", "iloveyou");
        actualUser.setEmail("jane.doe@example.org");
        actualUser.setFirstName("Jane");
        actualUser.setLastName("Doe");
        actualUser.setNumber("42");
        actualUser.setPassword("iloveyou");
        actualUser.setRole("Role");
        actualUser.setUserName("janedoe");
        assertEquals("jane.doe@example.org", actualUser.getEmail());
        assertEquals("Jane", actualUser.getFirstName());
        assertEquals("Doe", actualUser.getLastName());
        assertEquals("42", actualUser.getNumber());
        assertEquals("iloveyou", actualUser.getPassword());
        assertEquals("Role", actualUser.getRole());
        assertEquals("janedoe", actualUser.getUserName());
    }
}

