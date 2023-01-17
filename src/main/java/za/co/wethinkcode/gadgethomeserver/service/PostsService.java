package za.co.wethinkcode.gadgethomeserver.service;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import org.springframework.web.server.ResponseStatusException;
import za.co.wethinkcode.gadgethomeserver.mapper.PostMapper;
import za.co.wethinkcode.gadgethomeserver.mapper.UserMapper;
import za.co.wethinkcode.gadgethomeserver.models.database.Post;
import za.co.wethinkcode.gadgethomeserver.models.database.User;
import za.co.wethinkcode.gadgethomeserver.models.domain.PostDto;
import za.co.wethinkcode.gadgethomeserver.models.domain.UserDto;
import za.co.wethinkcode.gadgethomeserver.repository.PostRepository;

import java.util.List;

@Service
public class PostsService {

    private final PostRepository postRepo;

    private final PostMapper postMapper;

    private final UserDetailsService userService;

    private final UserMapper userMapper;

    public PostsService(PostRepository postRepository, PostMapper postMapper, UserDetailsService userService, UserMapper userMapper) {
        this.postRepo = postRepository;
        this.postMapper = postMapper;
        this.userService = userService;
        this.userMapper = userMapper;
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

    public PostDto updatePost(Long id, PostDto post) {
        Authentication authentication = SecurityContextHolder
                .getContext().getAuthentication();

        Post postDb = postRepo.getById(id);
        UserDto user = userService.getUserDto(authentication.getName());

        if (!authentication.isAuthenticated() ||
                !user.getUserName().equals(postDb.getOwner().getUserName())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User not authenticated to edit Post");
        }
        postDb = postMapper.toEntity(post);
        postDb.setId(id);
        postDb.setOwner(userMapper.toEntity(user));
        return postMapper.toDto(postRepo.save(postDb));
    }

    public PostDto deletePost(Long id) {
        Authentication authentication = SecurityContextHolder
            .getContext().getAuthentication();

        Post postDb = postRepo.getById(id);

        if (!authentication.isAuthenticated() ||
                !authentication.getName().equals(postDb.getOwner().getUserName())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User not authenticated to delete Post");
        }

        PostDto post = postMapper.toDto(postDb);

        postRepo.delete(postDb);
        return post;
    }

    public PostDto addPost(PostDto postDto) throws Exception {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        UserDto userDto = userService.getUserDto(username);

        Post post = postMapper.toEntity(postDto);
        post.setOwner(userMapper.toEntity(userDto));

        return postMapper.toDto(postRepo.save(post));
    }
}
