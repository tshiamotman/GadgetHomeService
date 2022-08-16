package za.co.wethinkcode.gadgethomeserver.controller;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestClientException;
import org.springframework.web.server.ResponseStatusException;
import za.co.wethinkcode.gadgethomeserver.models.Post;
import za.co.wethinkcode.gadgethomeserver.models.User;
import za.co.wethinkcode.gadgethomeserver.repository.UserRepository;
import za.co.wethinkcode.gadgethomeserver.services.PostsService;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/ads")
public class PostsController {

    private final UserRepository userRepo;
    private final PostsService postsService;

    public PostsController(UserRepository userRepository, PostsService postsService) {
        this.userRepo = userRepository;
        this.postsService = postsService;
    }

    @GetMapping("/posts")
    public List<Post> getPosts() {
        return postsService.getPosts();
    }

    @GetMapping("/posts/id/{id}")
    public Post getPost(@PathVariable Long id) {
        try {
            return postsService.getPost(id);
        } catch (RestClientException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Post not found", e);
        }
    }

    @GetMapping("/posts/key/{keyword}")
    public List<Post> getPostByKeyword(@PathVariable String keyword) {
        try {
            return postsService.getPostsByKeyword(keyword);
        } catch (RestClientException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Posts not found", e);
        }
    }

    @PostMapping("/post")
    public Post addPost(@RequestBody Map<String, String> map) {
        Authentication authentication = SecurityContextHolder
                .getContext().getAuthentication();

        if (!authentication.isAuthenticated()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User unauthorised");
        }

        User user = userRepo.findUserByUserName(authentication.getName());

        Post post = new Post(
                map.get("device"),
                map.get("model"),
                map.get("brand"),
                map.get("description"),
                user,
                Double.parseDouble(map.get("amount")));

        return postsService.addPost(post);
    }

    @PutMapping("/post/{id}")
    public Post updatePost(@PathVariable String id, @RequestBody Post post) {
        Authentication authentication = SecurityContextHolder
                .getContext().getAuthentication();

        Post postDb = postsService.getPost(Long.valueOf(id));

        if (!authentication.isAuthenticated() ||
                userRepo.findUserByUserName(authentication.getName()).equals(postDb.getOwner())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User does not exist");
        }

        return postsService.updatePost(Long.valueOf(id), post);
    }

}
