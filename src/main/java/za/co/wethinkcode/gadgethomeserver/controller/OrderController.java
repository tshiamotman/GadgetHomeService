package za.co.wethinkcode.gadgethomeserver.controller;

import java.util.Map;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import za.co.wethinkcode.gadgethomeserver.models.database.Order;
import za.co.wethinkcode.gadgethomeserver.models.database.Post;
import za.co.wethinkcode.gadgethomeserver.models.database.User;
import za.co.wethinkcode.gadgethomeserver.repository.UserRepository;
import za.co.wethinkcode.gadgethomeserver.service.OrderService;
import za.co.wethinkcode.gadgethomeserver.service.PostsService;

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

        Optional<User> user = userRepo.findById(authentication.getName());

        Post post = postsService.getPost(Long.valueOf(map.get("post_id")));

        return new Order(user.get(), post);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteOrder(@PathVariable String id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        Order order = orderService.getOrder(Long.valueOf(id));

        if(!authentication.isAuthenticated() || !authentication.getName().equals(order.getBuyer().getUserName())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User UNAUTHORIZED");
        }

        orderService.deleteOrder(order);
        
        return ResponseEntity.ok().build();
    }
}
