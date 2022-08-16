package za.co.wethinkcode.gadgethomeserver.models;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;

class OrderTest {
    @Test
    void testConstructor() {
        Order actualOrder = new Order();
        actualOrder.setId(123L);
        actualOrder.setPaymentReleased(true);
        actualOrder.setTrackId("42");
        actualOrder.setTransit("Transit");
        assertNull(actualOrder.getBuyer());
        assertEquals(123L, actualOrder.getId().longValue());
        assertNull(actualOrder.getPost());
        assertEquals("42", actualOrder.getTrackId());
        assertEquals("Transit", actualOrder.getTransit());
        assertTrue(actualOrder.isPaymentReleased());
    }

    @Test
    void testConstructor2() {
        assertFalse((new Order()).isPaymentReleased());
    }

    @Test
    void testConstructor3() {
        User user = new User();
        user.setEmail("jane.doe@example.org");
        user.setFirstName("Jane");
        user.setLastName("Doe");
        user.setNumber("42");
        user.setPassword("iloveyou");
        user.setRole("Role");
        user.setUserName("janedoe");

        User user1 = new User();
        user1.setEmail("jane.doe@example.org");
        user1.setFirstName("Jane");
        user1.setLastName("Doe");
        user1.setNumber("42");
        user1.setPassword("iloveyou");
        user1.setRole("Role");
        user1.setUserName("janedoe");

        Post post = new Post();
        post.setAmount(10.0d);
        post.setAvailable(true);
        post.setBrand("Brand");
        post.setDatePosted(LocalDate.ofEpochDay(1L));
        post.setDescription("The characteristics of someone or something");
        post.setDevice("Device");
        post.setId(123L);
        post.setModel("Model");
        post.setOwner(user1);
        Order actualOrder = new Order(user, post);

        assertFalse(actualOrder.isPaymentReleased());
        assertEquals("Seller Hasn't Posted", actualOrder.getTransit());
        Post post1 = actualOrder.getPost();
        assertSame(post, post1);
        assertSame(user, actualOrder.getBuyer());
        assertFalse(post1.isAvailable());
    }
}

