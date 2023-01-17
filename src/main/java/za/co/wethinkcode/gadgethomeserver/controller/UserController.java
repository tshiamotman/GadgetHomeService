package za.co.wethinkcode.gadgethomeserver.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import za.co.wethinkcode.gadgethomeserver.models.domain.UserDto;
import za.co.wethinkcode.gadgethomeserver.service.PictureService;
import za.co.wethinkcode.gadgethomeserver.service.UserDetailsService;

import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("/user")
public class UserController {

    private final UserDetailsService userService;

    private final PictureService pictureService;

    public UserController(UserDetailsService userService, PictureService pictureService) {
        this.userService = userService;
        this.pictureService = pictureService;
    }

    @GetMapping("/getUser/{user}")
    public ResponseEntity<?> getUser(@PathVariable("user") String username) {
        try {
            return ResponseEntity.ok(userService.getUserDto(username));
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NO_CONTENT, "Post not found", e);
        }
    }

    @GetMapping("/getPicture/{user}")
    public ResponseEntity<?> getPicture(@PathVariable("user") String username) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.IMAGE_JPEG);

            UserDto userDto = userService.getUserDto(username);

            return new ResponseEntity<>(pictureService.getUserImage(userDto).getImage(), headers, HttpStatus.OK);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NO_CONTENT, "Picture not found", e);
        }
    }

    @PostMapping("/addPicture")
    public ResponseEntity<?> addUserPicture(@RequestPart("MultipartFile") MultipartFile file) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

            UserDto userDto = userService.getUserDto(authentication.getName());

            pictureService.addUserImage(file, userDto);

            return ResponseEntity.ok(Map.of("message", "Success"));
        } catch (IOException e) {
            throw new ResponseStatusException(HttpStatus.NO_CONTENT, "Post not found", e);
        }
    }
}
