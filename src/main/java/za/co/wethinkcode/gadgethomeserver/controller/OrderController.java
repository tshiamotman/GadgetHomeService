package za.co.wethinkcode.gadgethomeserver.controller;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import za.co.wethinkcode.gadgethomeserver.models.Order;
import za.co.wethinkcode.gadgethomeserver.models.Post;
import za.co.wethinkcode.gadgethomeserver.models.User;
import za.co.wethinkcode.gadgethomeserver.repository.UserRepository;
import za.co.wethinkcode.gadgethomeserver.services.OrderService;
import za.co.wethinkcode.gadgethomeserver.services.PostsService;

import java.util.Map;

@RestController
@RequestMapping("/order")
public class OrderController {

    private final OrderService orderService;
    private final PostsService postsService;
    private final UserRepository userRepo;

    public OrderController(OrderService orderService, PostsService postsService, UserRepository userRepository) {
        this.orderService = orderService;
        this.postsService = postsService;
        this.userRepo = userRepository;
    }

    @GetMapping("/{id}")
    public String trackOrder(@PathVariable String id) {
        return orderService.trackOrder(id);
    }

    @PostMapping("/add")
    public Order addOrder(@RequestBody Map<String, String> map){
        Authentication authentication = SecurityContextHolder
                .getContext().getAuthentication();

        if (!authentication.isAuthenticated()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User UNAUTHORIZED");
        }

        User user = userRepo.findUserByUserName(authentication.getName());

        Post post = postsService.getPost(Long.valueOf(map.get("post_id")));

        return new Order(user, post);
    }
}
