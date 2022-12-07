package za.co.wethinkcode.gadgethomeserver.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;

import java.util.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.fasterxml.jackson.core.JsonProcessingException;

import za.co.wethinkcode.gadgethomeserver.mocks.MockPosts;
import za.co.wethinkcode.gadgethomeserver.models.database.Post;
import za.co.wethinkcode.gadgethomeserver.models.database.User;
import za.co.wethinkcode.gadgethomeserver.repository.UserRepository;
import za.co.wethinkcode.gadgethomeserver.service.PostsService;
import za.co.wethinkcode.gadgethomeserver.service.UserDetailsService;
import za.co.wethinkcode.gadgethomeserver.util.SessionToken;

@AutoConfigureMockMvc
@WebMvcTest(PostsController.class)
public class PostControllerTest extends AbstractControllerTest {
    @MockBean
    UserRepository userRepo;

    @MockBean
    PostsService postService;


    @MockBean
    UserDetailsService userDetailsService;

    @MockBean
    SessionToken token;

    User owner;
    Post post;
    Map<String, String> postDto;

    @Override
    @BeforeEach
    protected void setUp() throws Exception {
        super.setUp();

        MockPosts mockPosts = new MockPosts();

        owner = new User("user", "password");
        post = new Post("cellphone", "Galaxy S22", "Samsung", "used", owner, 21000.0);

        postDto = new HashMap<>();
        postDto.put("device", "cellphone");
        postDto.put("model", "Galaxy S22");
        postDto.put("brand", "Samsung");
        postDto.put("description", "used");
        postDto.put("amount", "21000.0");

        BDDMockito.given(userDetailsService.loadUserByUsername("user"))
            .willReturn(new org.springframework.security.core.userdetails.User("user", "password", new ArrayList<>()));

        BDDMockito.given(userRepo.findById("user")).willReturn(Optional.of(owner));

        BDDMockito.given(userRepo.findById("test_1")).willReturn(Optional.of(new User("test_1", "password")));

        BDDMockito.given(postService.getPost(1L)).willReturn(mockPosts.getById(1L));

        BDDMockito.given(postService.getPosts()).willReturn(mockPosts.findAll());

        BDDMockito.given(postService.getPostsByKeyword("cellphone"))
            .willReturn(mockPosts.findByModelOrBrandOrDevice("cellphone"));

        BDDMockito.given(postService.addPost(any(Post.class))).willReturn(mockPosts.save(post));
    
        BDDMockito.given(postService.updatePost(anyLong(), any(Post.class))).willReturn(post);
    }

    @Test
    void getPostsTest() throws Exception {
        MockHttpServletResponse response = mvc.perform(MockMvcRequestBuilders.get("/ads/posts")
                                                .accept(MediaType.APPLICATION_JSON))
                                                .andReturn().getResponse();

        assertEquals(HttpStatus.OK.value(), response.getStatus());

        List<Post> posts = mapFromJson(response.getContentAsString(), List.class);

        assertNotNull(posts);
        assertTrue(posts.size() > 0);
    }

    @Test
    void getPostByIdTest() throws Exception {
        MockHttpServletResponse response = mvc.perform(MockMvcRequestBuilders.get("/ads/posts/id/1")
                                                .accept(MediaType.APPLICATION_JSON))
                                                .andReturn().getResponse();

        assertEquals(HttpStatus.OK.value(), response.getStatus());

        Post retrievedPost = mapFromJson(response.getContentAsString(), Post.class);

        assertNotNull(retrievedPost);
        assertEquals(1L, retrievedPost.getId());
    }

    @Test
    void getPostsByKeywordTest() throws Exception {
        MockHttpServletResponse response = mvc.perform(MockMvcRequestBuilders.get("/ads/posts/key/cellphone")
                                                .accept(MediaType.APPLICATION_JSON))
                                                .andReturn().getResponse();

        assertEquals(HttpStatus.OK.value(), response.getStatus());

        List<Post> posts = mapFromJson(response.getContentAsString(), List.class);

        assertNotNull(posts);
        assertTrue(posts.size() > 0);
    }

    @WithMockUser(username = "user", password = "password")
    @Test
    void addPostTest() throws JsonProcessingException, Exception {
        MockHttpServletResponse response = mvc.perform(MockMvcRequestBuilders.post("/ads/post")
                                            .contentType(MediaType.APPLICATION_JSON)
                                            .content(mapToJson(postDto).getBytes())
                                            .accept(MediaType.APPLICATION_JSON))
                                            .andReturn().getResponse();

        assertEquals(HttpStatus.OK.value(), response.getStatus());

        Post savedPost = mapFromJson(response.getContentAsString(), Post.class);
        assertNotNull(savedPost);
        assertEquals("user", savedPost.getOwner().getUserName());
    }

    @WithMockUser(username = "test_1", password = "password")
    @Test
    void updatePostTest() throws JsonProcessingException, Exception {
        MockHttpServletResponse response = mvc.perform(MockMvcRequestBuilders.put("/ads/post/1")
                                            .contentType(MediaType.APPLICATION_JSON)
                                            .content(mapToJson(postDto).getBytes())
                                            .accept(MediaType.APPLICATION_JSON))
                                            .andReturn().getResponse();

        assertEquals(HttpStatus.OK.value(), response.getStatus());

        Post savedPost = mapFromJson(response.getContentAsString(), Post.class);
        assertNotNull(savedPost);
        assertEquals(Post.class, savedPost.getClass());
    }
}
