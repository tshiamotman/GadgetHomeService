package za.co.wethinkcode.gadgethomeserver.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import za.co.wethinkcode.gadgethomeserver.mapper.UserMapper;
import za.co.wethinkcode.gadgethomeserver.models.database.Picture;
import za.co.wethinkcode.gadgethomeserver.models.database.Post;
import za.co.wethinkcode.gadgethomeserver.models.domain.UserDto;
import za.co.wethinkcode.gadgethomeserver.repository.PictureRepository;

import java.io.IOException;
import java.util.List;

@Service
public class PictureService {
    private final PictureRepository pictureRepo;

    private final UserMapper userMapper;

    public PictureService(PictureRepository pictureRepo, UserMapper userMapper) {
        this.pictureRepo = pictureRepo;
        this.userMapper = userMapper;
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

    public Picture addUserImage(MultipartFile image, UserDto user) throws IOException {
        Picture picture = new Picture();
        picture.setUser(userMapper.toEntity(user));
        picture.setImage(image.getBytes());

        return pictureRepo.save(picture);
    }

    public Picture getUserImage(UserDto user) {
        return pictureRepo.findByUserUserName(user.getUserName()).orElseThrow();
    }
}
