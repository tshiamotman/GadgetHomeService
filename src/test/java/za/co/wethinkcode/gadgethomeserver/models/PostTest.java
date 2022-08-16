package za.co.wethinkcode.gadgethomeserver.models;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;

class PostTest {

    User user = new User();

    @Test
    void testConstructor() {
        Post actualPost = new Post();
        actualPost.setAmount(10.0d);
        actualPost.setAvailable(true);
        actualPost.setBrand("Brand");
        LocalDate ofEpochDayResult = LocalDate.ofEpochDay(1L);
        actualPost.setDatePosted(ofEpochDayResult);
        actualPost.setDescription("The characteristics of someone or something");
        actualPost.setDevice("Device");
        actualPost.setId(123L);
        actualPost.setModel("Model");
        actualPost.setOwner(user);
        
        assertEquals(10.0d, actualPost.getAmount().doubleValue());
        assertEquals("Brand", actualPost.getBrand());
        assertSame(ofEpochDayResult, actualPost.getDatePosted());
        assertEquals("The characteristics of someone or something", actualPost.getDescription());
        assertEquals("Device", actualPost.getDevice());
        assertEquals(123L, actualPost.getId().longValue());
        assertEquals("Model", actualPost.getModel());
        assertSame(user, actualPost.getOwner());
        assertTrue(actualPost.isAvailable());
    }

    @Test
    void testConstructor2() {

        Post actualPost = new Post("Device", "Model", "Brand", "The characteristics of someone or something", user, 10.0d);

        assertEquals(10.0d, actualPost.getAmount().doubleValue());
        assertTrue(actualPost.isAvailable());
        assertSame(user, actualPost.getOwner());
        assertEquals("Model", actualPost.getModel());
        assertEquals("Brand", actualPost.getBrand());
        assertEquals("The characteristics of someone or something", actualPost.getDescription());
        assertEquals("Device", actualPost.getDevice());
    }

    private void setUser() {
        user.setEmail("jane.doe@example.org");
        user.setFirstName("Jane");
        user.setLastName("Doe");
        user.setNumber("42");
        user.setPassword("iloveyou");
        user.setRole("Role");
        user.setUserName("janedoe");
    }
}

