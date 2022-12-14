package za.co.wethinkcode.gadgethomeserver.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import za.co.wethinkcode.gadgethomeserver.models.database.Post;
import za.co.wethinkcode.gadgethomeserver.models.database.User;
import za.co.wethinkcode.gadgethomeserver.models.domain.PostDto;
import za.co.wethinkcode.gadgethomeserver.repository.UserRepository;
import za.co.wethinkcode.gadgethomeserver.service.PostsService;

import java.util.List;

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
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Post not found", e);
        }
    }

    @GetMapping("/posts/key/{keyword}")
    public List<Post> getPostByKeyword(@PathVariable String keyword) {
        try {
            return postsService.getPostsByKeyword(keyword);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Posts not found", e);
        }
    }

    @PostMapping("/post")
    public ResponseEntity<?> addPost(@RequestBody PostDto postDto) {
        Authentication authentication = SecurityContextHolder
                .getContext().getAuthentication();

        if (!authentication.isAuthenticated()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User unauthorised");
        }

        User user = userRepo.findById(authentication.getName()).get();

        Post post = postsService.addPost(new Post(
            postDto.getDevice(),
            postDto.getModel(),
            postDto.getBrand(),
            postDto.getDescription(),
            user,
            postDto.getAmount()));

        return ResponseEntity.ok().body(post);
    }

    @PutMapping("/post/{id}")
    public Post updatePost(@PathVariable String id, @RequestBody PostDto postDto) {
        Authentication authentication = SecurityContextHolder
                .getContext().getAuthentication();

        Post postDb = postsService.getPost(Long.valueOf(id));
        User user = userRepo.findById(authentication.getName()).get();

        if (!authentication.isAuthenticated() ||
                !user.getUserName().equals(postDb.getOwner().getUserName())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User not authenticated to edit Post");
        }

        Post post = new Post(
                postDto.getDevice(),
                postDto.getModel(),
                postDto.getBrand(),
                postDto.getDescription(),
                user,
                postDto.getAmount());

        return postsService.updatePost(Long.valueOf(id), post);
    }

    @DeleteMapping("/post/{id}")
    public ResponseEntity<?> deletePost(@PathVariable String id) {
        Authentication authentication = SecurityContextHolder
                .getContext().getAuthentication();

        Post postDb = postsService.getPost(Long.valueOf(id));

        if (!authentication.isAuthenticated() ||
                !userRepo.findById(authentication.getName()).equals(postDb.getOwner())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User not authenticated to delete Post");
        }
        postsService.deletePost(postDb);
        return ResponseEntity.ok().build();
    }
}
