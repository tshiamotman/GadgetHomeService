package za.co.wethinkcode.gadgethomeserver.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import za.co.wethinkcode.gadgethomeserver.models.database.Post;
import za.co.wethinkcode.gadgethomeserver.models.domain.PostDto;
import za.co.wethinkcode.gadgethomeserver.service.PostsService;

import java.util.List;

@RestController
@RequestMapping("/ads")
public class PostsController {
    private final PostsService postsService;

    public PostsController(PostsService postsService) {
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
        try {
            return ResponseEntity.ok(postsService.addPost(postDto));
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Failed to save Post", e);
        }
    }

    @PutMapping("/post/{id}")
    public ResponseEntity<?> updatePost(@PathVariable String id, @RequestBody PostDto postDto) {
        try {
            return ResponseEntity.ok(postsService.updatePost(Long.valueOf(id), postDto));
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Failed to update Post", e);
        }
    }

    @DeleteMapping("/post/{id}")
    public ResponseEntity<?> deletePost(@PathVariable String id) {
        try {
            return ResponseEntity.ok(postsService.deletePost(Long.valueOf(id)));
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Failed to delete Post", e);
        }
    }
}
