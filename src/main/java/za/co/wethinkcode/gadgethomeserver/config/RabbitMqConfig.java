package za.co.wethinkcode.gadgethomeserver.config;

import com.rabbitmq.client.ConnectionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import za.co.wethinkcode.gadgethomeserver.util.PropertyProvider;

@Component
public class RabbitMqConfig {

    private final PropertyProvider propertyProvider;

    public RabbitMqConfig(PropertyProvider propertyProvider) {
        this.propertyProvider = propertyProvider;
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
