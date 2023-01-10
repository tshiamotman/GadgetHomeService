package za.co.wethinkcode.gadgethomeserver.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;
import za.co.wethinkcode.gadgethomeserver.models.domain.ChatDto;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeoutException;

@Service
public class RabbitMqService {

    private final Connection connection;

    private final ObjectMapper objectMapper;
   
    private final Log logger = LogFactory.getLog(this.getClass());

    public RabbitMqService(ConnectionFactory factory, ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;

        try {
            this.connection = factory.newConnection();
        } catch (IOException | TimeoutException e) {
            logger.error(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public Boolean publishMessageToQueue(ChatDto chat, String queue) {
        Channel channel;
        try {
            channel = connection.createChannel();
            channel.exchangeDeclare("messages", "direct");

            channel.queueDeclare(queue, false, false, false, null);
            channel.queueBind(queue, "messages", queue);

            channel.basicPublish("messages", queue, null,
                    objectMapper.writeValueAsString(chat).getBytes());

            return true;
        } catch (IOException e) {
            logger.error(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public List<ChatDto> getQueueMessages(String queue) {
        List<ChatDto> queueMessages = new ArrayList<>();
        Channel channel;
        try {
            channel = connection.createChannel();
            channel.queueDeclare(queue, false, false, false, null);

            long messageCount = channel.messageCount(queue);

            for(long i = 0; i < messageCount; i++) {
                GetResponse response = channel.basicGet(queue, true);
                ChatDto chatDto = objectMapper.readValue(new String(response.getBody()), ChatDto.class);
                queueMessages.add(chatDto);
            }
            channel.queueDelete(queue);
        } catch (IOException e) {
            logger.error(e.getMessage());
            throw new RuntimeException(e);
        }

        return queueMessages;
    }
}
