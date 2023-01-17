package za.co.wethinkcode.gadgethomeserver.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.security.test.context.support.WithMockUser;
import za.co.wethinkcode.gadgethomeserver.mapper.PostMapper;
import za.co.wethinkcode.gadgethomeserver.mapper.UserMapper;
import za.co.wethinkcode.gadgethomeserver.mocks.MockPosts;
import za.co.wethinkcode.gadgethomeserver.models.database.Post;
import za.co.wethinkcode.gadgethomeserver.models.database.User;
import za.co.wethinkcode.gadgethomeserver.models.domain.PostDto;
import za.co.wethinkcode.gadgethomeserver.repository.PostRepository;

public class PostsServiceTest {

    PostsService service;

    PostMapper postMapper;
    PostRepository repo;

    UserDetailsService userService;

    UserMapper userMapper;

    User owner;
    Post post;

    PostDto postDto;

    @BeforeEach
    void setUp() {
        repo = mock(PostRepository.class);
        postMapper = mock(PostMapper.class);
        userService = mock(UserDetailsService.class);
        userMapper = mock(UserMapper.class);

        owner = new User("user", "password");
        post = new Post("cellphone", "Galaxy S22", "Samsung", "used", owner, 21000.0);
        postDto = new PostDto();
        postDto.setDevice("cellphone");
        postDto.setModel("Galaxy S22");
        postDto.setBrand("Samsung");
        postDto.setDescription("Used");
        postDto.setPrice(21000.0);

        service = new PostsService(repo, postMapper, userService, userMapper);
    }

    @Test
    @WithMockUser(username = "user")
    void testAddPost() throws Exception{
        when(repo.save(post)).thenReturn(new MockPosts().save(post));
        PostDto newPost = service.addPost(postDto);

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

}
