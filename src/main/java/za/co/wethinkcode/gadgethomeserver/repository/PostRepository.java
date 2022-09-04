package za.co.wethinkcode.gadgethomeserver.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import za.co.wethinkcode.gadgethomeserver.models.database.Post;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

    List<Post> findByModelOrBrandOrDevice(String model, String brand, String device);

    List<Post> findPostByOwner(String owner);
}
