package za.co.wethinkcode.gadgethomeserver.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import za.co.wethinkcode.gadgethomeserver.models.database.Chat;
import za.co.wethinkcode.gadgethomeserver.models.database.Post;
import za.co.wethinkcode.gadgethomeserver.models.database.User;
import za.co.wethinkcode.gadgethomeserver.repository.ChatRepository;
import za.co.wethinkcode.gadgethomeserver.repository.UserRepository;

public class ChatServiceTest {
    ChatService service;
    ChatRepository repo;
    UserRepository userRepo;

    User seller;
    User buyer;
    Post post;

    Chat chat;

    @BeforeEach
    void setup() {
        repo = mock(ChatRepository.class);
        userRepo = mock(UserRepository.class);

        seller = new User("seller", "password");
        buyer = new User("buyer", "password");
        post = new Post("cellphone", "Galaxy S22", "Samsung", "used", seller, 21000.0);
        post.setId(1L);

        chat = new Chat(post, buyer);
        chat.setId(1L);

        service = new ChatService(repo, userRepo);
    }

    @Test
    void createQueueTest() throws Exception {
        when(repo.existsByBuyerAndPost(buyer, post)).thenReturn(false);
        when(repo.save(any(Chat.class))).thenReturn(chat);

        Chat chat1 = service.createChat(buyer, post);

        assertEquals("buyer_seller_1", chat1.getQueueName());
    }
}
