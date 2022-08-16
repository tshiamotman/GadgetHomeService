package za.co.wethinkcode.gadgethomeserver.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import za.co.wethinkcode.gadgethomeserver.models.Post;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {

    List<Post> findByModelOrBrandOrDevice(String model, String brand, String device);
}
