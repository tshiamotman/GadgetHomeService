package za.co.wethinkcode.gadgethomeserver.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import za.co.wethinkcode.gadgethomeserver.mocks.MockPosts;
import za.co.wethinkcode.gadgethomeserver.models.database.Post;
import za.co.wethinkcode.gadgethomeserver.models.database.User;
import za.co.wethinkcode.gadgethomeserver.repository.PostRepository;

public class PostsServiceTest {

    PostsService service;
    PostRepository repo;

    User owner;
    Post post;

    @BeforeEach
    void setUp() {
        repo = mock(PostRepository.class);
        owner = new User("user", "password");
        post = new Post("cellphone", "Galaxy S22", "Samsung", "used", owner, 21000.0);

        service = new PostsService(repo);
    }

    @Test
    void testAddPost() {
        when(repo.save(post)).thenReturn(new MockPosts().save(post));
        Post newPost = service.addPost(post); 

        assertEquals("cellphone", newPost.getDevice());
        assertEquals("Samsung", newPost.getBrand());
        assertEquals("user", newPost.getOwner().getUserName());
        assertNotNull(newPost.getId());
    }

    @Test
    void testGetPost() {
        when(repo.getById(1L)).thenReturn(new MockPosts().getById(1L));

        Post postDb = service.getPost(1L);

        assertEquals(1L, postDb.getId());
        assertEquals("test_1", postDb.getOwner().getUserName());
    }

    @Test
    void testGetPosts() {
        when(repo.findAll()).thenReturn(new MockPosts().findAll());

        List<Post> posts = service.getPosts();

        assertTrue(posts.size() > 0);
        assertEquals(Post.class, posts.get(0).getClass());
    }

    @Test
    void testGetPostsByKeyword() {
        String keyword = "cellphone";
        when(repo.findByModelOrBrandOrDevice(keyword, keyword, keyword))
            .thenReturn(new MockPosts().findByModelOrBrandOrDevice(keyword));
        
        List<Post> posts = service.getPostsByKeyword(keyword);

        assertTrue(posts.size() > 0);
        assertEquals(Post.class, posts.get(0).getClass());
    }

    @Test
    void testUpdatePost() {
        when(repo.getById(1L)).thenReturn(new MockPosts().getById(1L));
        when(repo.save(post)).thenReturn(new MockPosts().save(post));

        Post postDb = service.getPost(1L);

        assertEquals(1L, postDb.getId());
        assertEquals("test_1", postDb.getOwner().getUserName());

        Post updatedPost = service.updatePost(1L, post);

        assertEquals(1L, updatedPost.getId());
        assertEquals("user", updatedPost.getOwner().getUserName());
    }
}
