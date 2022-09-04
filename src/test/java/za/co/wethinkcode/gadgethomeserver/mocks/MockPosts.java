package za.co.wethinkcode.gadgethomeserver.mocks;

import java.util.ArrayList;
import java.util.List;

import za.co.wethinkcode.gadgethomeserver.models.database.Post;
import za.co.wethinkcode.gadgethomeserver.models.database.User;

public class MockPosts {
    List<Post> posts = new ArrayList<>();

    public MockPosts() {
        String username = "test_1";
        String model = "Galaxy S";
        Double amount = 5000.0;
        for(int i = 1; i < 11; i++){
            User owner = new User(username, "password");
            Post post = new Post("cellphone", model + i, 
                "Samsung", "new", owner, amount + 1000.0 * i);
            post.setId(Long.valueOf(i));
            posts.add(post);
        }
    }

    public List<Post> findAll() {
        return posts;
    }

    public Post save(Post post) {
        if(post.getId() == null) post.setId(Long.valueOf(posts.size() + 1));
        posts.add(post);
        return post;
    }

    public Post getById(Long id) {
        for(Post post: posts) {
            if(post.getId().equals(id)) return post;
        }
        return null;
    }

    public List<Post> findByModelOrBrandOrDevice(String keyword) {
        List<Post> searchPosts = new ArrayList<>();

        for(Post post:posts) {
            if(post.getModel().equals(keyword) || post.getBrand().equals(keyword) || post.getDevice().equals(keyword)) {
                searchPosts.add(post);
            }
        }
        return searchPosts;
    }

    public void delete(Post post) {
        posts.remove(post);
    }
}