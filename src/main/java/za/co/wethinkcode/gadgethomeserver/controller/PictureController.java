package za.co.wethinkcode.gadgethomeserver.controller;

import org.springframework.http.CacheControl;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import za.co.wethinkcode.gadgethomeserver.models.Picture;
import za.co.wethinkcode.gadgethomeserver.models.Post;
import za.co.wethinkcode.gadgethomeserver.services.PictureService;
import za.co.wethinkcode.gadgethomeserver.services.PostsService;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/images")
public class PictureController {

    private final PictureService service;
    private final PostsService postsService;

    public PictureController(PostsService postsService, PictureService pictureService) {
        this.service = pictureService;
        this.postsService = postsService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<byte[]> getImage(@PathVariable String id) {
        Post post = postsService.getPost(Long.valueOf(id));

        HttpHeaders headers = new HttpHeaders();
        headers.setCacheControl(CacheControl.noCache().getHeaderValue());
        headers.setContentType(MediaType.IMAGE_JPEG);

        Picture image = service.getImages(post).get(0);

        return new ResponseEntity<>(image.getImage(), headers, HttpStatus.OK);
    }

    @GetMapping("/images/{id}")
    public List<ResponseEntity<byte[]>> getImages(@PathVariable String id) {
        Post post = postsService.getPost(Long.valueOf(id));
        HttpHeaders headers = new HttpHeaders();
        headers.setCacheControl(CacheControl.noCache().getHeaderValue());

        List<ResponseEntity<byte[]>> responses = new ArrayList<>();

        for(Picture image: service.getImages(post)){
            responses.add(new ResponseEntity<>(image.getImage(), headers, HttpStatus.OK));
        }
        return responses;
    }

    @PostMapping("/{id}")
    public ResponseEntity<Map<String, String>>
    uploadPicture(@PathVariable String id, @RequestPart("MultipartFile") MultipartFile image) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        Post post = postsService.getPost(Long.valueOf(id));

        if(!authentication.isAuthenticated() ||
                !post.getOwner().getUserName().equals(authentication.getName())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User not authorized");
        }

        service.addImage(image, post);

        return new ResponseEntity<>(
                Map.of("message", "successful"),
                new HttpHeaders(),
                HttpStatus.OK
        );
    }
}
