package za.co.wethinkcode.gadgethomeserver.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.rabbitmq.client.*;
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

import java.io.IOException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeoutException;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service
public class ChatService {
    private final ChatRepository chatRepo;
    private final UserDetailsService userService;
    private final PostsService postsService;
    private final FirebaseMessaging firebaseMessaging;

    private final ChatMapper chatMapper;

    private final ObjectMapper objectMapper;

    private final ConnectionFactory factory;

    private final Log logger = LogFactory.getLog(this.getClass());


    public ChatService(ChatRepository chatRepository,
                       UserDetailsService userService,
                       PostsService postsService,
                       FirebaseMessaging firebaseMessaging,
                       ChatMapper chatMapper,
                       ObjectMapper objectMapper, ConnectionFactory factory) {
        this.chatRepo = chatRepository;
        this.userService = userService;
        this.postsService = postsService;
        this.firebaseMessaging = firebaseMessaging;
        this.chatMapper = chatMapper;
        this.objectMapper = objectMapper;
        this.factory = factory;
    }


    public ChatDto sendMessage(ChatDto chatDto) throws IOException {
        UserDto recipient = userService.getUserDto(chatDto.getRecipientUsername());

        Chat chatEntity = chatRepo.save(chatMapper.toEntity(chatDto));
        if(recipient.getDeviceId() == null || Objects.equals(recipient.getDeviceId(), "")) {
            Connection connection = null;
            try {
                connection = factory.newConnection();
                Channel channel = connection.createChannel();

                channel.exchangeDeclare("messages", "direct");

                channel.queueDeclare(recipient.getUserName(), false, false, false, null);
                channel.queueBind(recipient.getUserName(), "messages", recipient.getUserName());

                channel.basicPublish("messages", recipient.getUserName(), null,
                        objectMapper.writeValueAsBytes(chatDto));
            } catch (IOException | TimeoutException e) {
                logger.error(e.getMessage());
                throw new RuntimeException(e);
            } finally {
                assert connection != null;
                connection.close();
            }
        } else {
            Message msg = Message.builder()
                .setToken(recipient.getDeviceId())
                .putData("body", chatDto.getMessage())
                .putData("sender", chatDto.getSenderUsername())
                .putData("sent", LocalDate.now().toString())
                .build();

            String id = null;
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

    public UserDto updateDeviceId(UserDto user) throws Exception {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(Objects.equals(user.getUserName(), authentication.getName())){
            
            UserDto savedUser = userService.saveDeviceToContact(user);

            Connection connection = null;
            try {
                connection = factory.newConnection();
                Channel channel = connection.createChannel();
                channel.queueDeclare(user.getUserName(), false, false, false, null);

                long messageCount = channel.messageCount(user.getUserName());

                for(long i = 0; i < messageCount; i++) {
                    GetResponse response = channel.basicGet(user.getUserName(), true);
                    ChatDto chatDto = objectMapper.readValue(new String(response.getBody()), ChatDto.class);
                    sendMessage(chatDto);
                }
                channel.queueDelete(user.getUserName());
            } catch (IOException | TimeoutException e) {
                logger.error(e.getMessage());
                throw new RuntimeException(e);
            } finally {
                assert connection != null;
                connection.close();
            }
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
}
