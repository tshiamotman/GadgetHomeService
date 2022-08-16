package za.co.wethinkcode.gadgethomeserver.models;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;

import java.io.IOException;
import java.time.LocalDate;

import org.junit.jupiter.api.Disabled;

import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockMultipartFile;

class PictureTest {
    @Test
    void testConstructor() throws IOException {
        MockMultipartFile file = new MockMultipartFile("Name", "AAAAAAAA".getBytes("UTF-8"));

        User user = new User();
        user.setEmail("jane.doe@example.org");
        user.setFirstName("Jane");
        user.setLastName("Doe");
        user.setNumber("42");
        user.setPassword("iloveyou");
        user.setRole("Role");
        user.setUserName("janedoe");

        Post post = new Post();
        post.setAmount(10.0d);
        post.setAvailable(true);
        post.setBrand("Brand");
        post.setDatePosted(LocalDate.ofEpochDay(1L));
        post.setDescription("The characteristics of someone or something");
        post.setDevice("Device");
        post.setId(123L);
        post.setModel("Model");
        post.setOwner(user);
        Picture actualPicture = new Picture(file, post);

        assertEquals("", actualPicture.getFileName());
        assertEquals(8L, actualPicture.getSize());
        assertSame(post, actualPicture.getPost());
        assertEquals(8, actualPicture.getImage().length);
        assertNull(actualPicture.getFileType());
    }
}

