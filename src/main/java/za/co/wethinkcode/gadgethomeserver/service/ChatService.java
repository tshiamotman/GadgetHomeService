package za.co.wethinkcode.gadgethomeserver.service;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import za.co.wethinkcode.gadgethomeserver.mapper.ChatMapper;
import za.co.wethinkcode.gadgethomeserver.models.database.Chat;
import za.co.wethinkcode.gadgethomeserver.models.domain.ChatDto;
import za.co.wethinkcode.gadgethomeserver.models.domain.UserDto;
import za.co.wethinkcode.gadgethomeserver.repository.ChatRepository;

import java.time.LocalDate;
import java.util.*;

@Service
public class ChatService {
    private final ChatRepository chatRepo;
    private final UserDetailsService userService;
    private final PostsService postsService;
    private final FirebaseMessaging firebaseMessaging;

    private final ChatMapper chatMapper;

    private final RabbitMqService rabbitMqService;

    private final Log logger = LogFactory.getLog(this.getClass());


    public ChatService(ChatRepository chatRepository,
                       UserDetailsService userService,
                       PostsService postsService,
                       FirebaseMessaging firebaseMessaging,
                       ChatMapper chatMapper, RabbitMqService rabbitMqService) {
        this.chatRepo = chatRepository;
        this.userService = userService;
        this.postsService = postsService;
        this.firebaseMessaging = firebaseMessaging;
        this.chatMapper = chatMapper;
        this.rabbitMqService = rabbitMqService;
    }


    public ChatDto sendMessage(ChatDto chatDto) {
        UserDto recipient = userService.getUserDto(chatDto.getRecipientUsername());

        Chat chatEntity = chatRepo.save(chatMapper.toEntity(chatDto));
        if(recipient.getDeviceId() == null || Objects.equals(recipient.getDeviceId(), "")) {
            Boolean sentToQueue = rabbitMqService.publishMessageToQueue(chatDto, recipient.getUserName());
        } else {
            Message msg = Message.builder()
                .setToken(recipient.getDeviceId())
                .putData("body", chatDto.getMessage())
                .putData("sender", chatDto.getSenderUsername())
                .putData("sent", LocalDate.now().toString())
                .build();

            String id;
            try {
                id = firebaseMessaging.send(msg);
            } catch (FirebaseMessagingException e) {
                logger.error(e.getMessage());
                throw new RuntimeException(e);
            }
            chatEntity.setMessageId(id);
            chatEntity.setMessageDelivered(true);
        }

        chatEntity.setRecipient(userService.getUser(recipient.getUserName()));
        chatEntity.setSender(userService.getUser(chatDto.getSenderUsername()));

        if(chatDto.getPost() != null) chatEntity.setPost(postsService.getPost(chatDto.getPost().getId()));

        Chat savedChat = chatRepo.save(chatEntity);
        
        return chatMapper.toDto(savedChat);
    }

    public UserDto updateDeviceId(UserDto user) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(Objects.equals(user.getUserName(), authentication.getName())){
            
            UserDto savedUser = userService.saveDeviceToContact(user);

            List<ChatDto> queueMessages = rabbitMqService.getQueueMessages(user.getUserName());

            queueMessages.forEach(this::sendMessage);

            return savedUser;
        } else {
            throw new RuntimeException("User does not match");
        }
    }

    public ChatDto setMessageToRead(String messageId) {
        Chat chatEntity = chatRepo.findByMessageId(messageId).orElseThrow();
        chatEntity.setMessageRead(true);
        return chatMapper.toDto(chatRepo.save(chatEntity));
    }

    public Map<String, List<ChatDto>> getAllConversations() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        List<Chat> allChats = chatRepo
                .findAllBySenderUserNameOrRecipientUserName(username, username);

        Set<String> usernames = new HashSet<>();

        allChats.forEach(chat -> {
            usernames.add(chat.getRecipient().getUserName());
            usernames.add(chat.getSender().getUserName());
        });

        usernames.remove(username);

        Map<String, List<ChatDto>> conversations = new HashMap<>();

        usernames.forEach(user -> {
            List<ChatDto> conversation = new ArrayList<>();
            for(Chat chat: allChats) {
                if(Objects.equals(chat.getSender().getUserName(), user) ||
                        Objects.equals(chat.getRecipient().getUserName(), user)) {
                    conversation.add(chatMapper.toDto(chat));
                }
            }
            conversations.put(user, conversation);
        });
        return conversations;
    }
}
