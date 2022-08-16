package za.co.wethinkcode.gadgethomeserver.services;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import za.co.wethinkcode.gadgethomeserver.models.Picture;
import za.co.wethinkcode.gadgethomeserver.models.Post;
import za.co.wethinkcode.gadgethomeserver.repository.PictureRepository;

import java.io.IOException;
import java.util.List;

@Service
public class PictureService {
    private final PictureRepository pictureRepo;

    public PictureService(PictureRepository pictureRepo) {
        this.pictureRepo = pictureRepo;
    }

    public void addImage(MultipartFile image, Post post) {
        try {
            pictureRepo.save(new Picture(image, post));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<Picture> getImages(Post post) {
        return pictureRepo.getByPost(post);
    }
//
//    public Picture getImage(Long id){
//        return pictureRepo.getById(id);
//    }
}
