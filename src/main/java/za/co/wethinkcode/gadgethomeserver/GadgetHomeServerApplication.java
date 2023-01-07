package za.co.wethinkcode.gadgethomeserver;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import za.co.wethinkcode.gadgethomeserver.util.PropertyProvider;

import java.net.URISyntaxException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

@SpringBootApplication
public class GadgetHomeServerApplication {

    @Autowired
    PropertyProvider propertyProvider;

    public static void main(String[] args) {
        SpringApplication.run(GadgetHomeServerApplication.class, args);
    }

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }

    @Bean
    public ConnectionFactory connectionFactory(){
        ConnectionFactory factory = new ConnectionFactory();
        factory.setUsername(propertyProvider.getRabbitMQUser());
        factory.setPassword(propertyProvider.getRabbitMQPassword());
        factory.setVirtualHost(propertyProvider.getRabbitMQVirtualHost());
        factory.setHost(propertyProvider.getRabbitMQHost());
        factory.setPort(propertyProvider.getRabbitMQPort());
        factory.setAutomaticRecoveryEnabled(true);
        return factory;
    }
}
