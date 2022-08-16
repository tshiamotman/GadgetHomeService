package za.co.wethinkcode.gadgethomeserver.services;

import org.springframework.stereotype.Service;
import za.co.wethinkcode.gadgethomeserver.models.Post;
import za.co.wethinkcode.gadgethomeserver.repository.PostRepository;

import java.util.List;

@Service
public class PostsService {

    private final PostRepository postRepo;

    public PostsService(PostRepository postRepository) {
        this.postRepo = postRepository;
    }

    public List<Post> getPosts() {
        return postRepo.findAll();
    }

    public Post getPost(Long id) {
        return postRepo.getById(id);
    }

    public List<Post> getPostsByKeyword(String keyword) {
        return postRepo.findByModelOrBrandOrDevice(keyword, keyword, keyword);
    }

    public Post updatePost(Long id, Post post) {
        Post postDb = postRepo.getById(id);
        postRepo.delete(postDb);
        return postRepo.save(post);
    }

    public void deletePost(Post post) {
        postRepo.delete(post);
    }

    public Post addPost(Post post) {
        return postRepo.save(post);
    }
}
