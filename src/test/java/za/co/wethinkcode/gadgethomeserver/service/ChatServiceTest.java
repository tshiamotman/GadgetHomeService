package za.co.wethinkcode.gadgethomeserver.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.firebase.messaging.FirebaseMessaging;
import com.rabbitmq.client.ConnectionFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import za.co.wethinkcode.gadgethomeserver.mapper.ChatMapper;
import za.co.wethinkcode.gadgethomeserver.models.database.Chat;
import za.co.wethinkcode.gadgethomeserver.models.database.Post;
import za.co.wethinkcode.gadgethomeserver.models.database.User;
import za.co.wethinkcode.gadgethomeserver.repository.ChatRepository;
import za.co.wethinkcode.gadgethomeserver.util.PropertyProvider;

public class ChatServiceTest {
    ChatService service;
    ChatRepository repo;

    PostsService postsService;

    UserDetailsService userService;

    FirebaseMessaging fcm;

    ChatMapper chatMapper;

    RabbitMqService rabbitMqService;

    User seller;
    User buyer;
    Post post;

    Chat chat;

    PropertyProvider propertyProvider;

    @BeforeEach
    void setup() {
        repo = mock(ChatRepository.class);
        postsService = mock(PostsService.class);
        userService = mock(UserDetailsService.class);
        fcm = mock(FirebaseMessaging.class);
        chatMapper = mock(ChatMapper.class);
        propertyProvider = mock(PropertyProvider.class);
        rabbitMqService = mock(RabbitMqService.class);

        seller = new User("seller", "password");
        buyer = new User("buyer", "password");
        post = new Post("cellphone", "Galaxy S22", "Samsung", "used", seller, 21000.0);
        post.setId(1L);

        chat = new Chat();
        chat.setId(1L);

        service = new ChatService(repo, userService, postsService, fcm, chatMapper, rabbitMqService);
    }

    @Test
    void createQueueTest() throws Exception {

    }
}
