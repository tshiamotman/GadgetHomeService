package za.co.wethinkcode.gadgethomeserver.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import za.co.wethinkcode.gadgethomeserver.models.database.Picture;
import za.co.wethinkcode.gadgethomeserver.models.database.Post;

import java.util.List;

public interface PictureRepository extends JpaRepository<Picture, Long> {
    List<Picture> getByPost(Post post);
}
