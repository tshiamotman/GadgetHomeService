package za.co.wethinkcode.gadgethomeserver.service;

import org.springframework.stereotype.Service;

import za.co.wethinkcode.gadgethomeserver.models.database.Chat;
import za.co.wethinkcode.gadgethomeserver.models.database.Post;
import za.co.wethinkcode.gadgethomeserver.models.database.User;
import za.co.wethinkcode.gadgethomeserver.repository.ChatRepository;
import za.co.wethinkcode.gadgethomeserver.repository.UserRepository;

@Service
public class ChatService {
    private ChatRepository chatRepo;
    private UserRepository userRepo;


    public ChatService(ChatRepository chatRepository, UserRepository userRepository) {
        this.chatRepo = chatRepository;
        this.userRepo = userRepository;
    }


    public Chat createChat(User buyer, Post post) throws Exception {
        if(chatRepo.existsByBuyerAndPost(buyer, post)) throw new Exception("Chat Queue already exists.");

        Chat chat = new Chat(post, buyer);

        Chat savedChat = chatRepo.save(chat);
        
        return savedChat;
    }
    

    public Chat closeChat(User buyer, Post post) {
        
        return null;
    }
}
