package za.co.wethinkcode.gadgethomeserver.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class PropertyProvider {
    @Value("${spring.rabbit.host}")
    private String rabbitMQHost;

    @Value("${spring.rabbit.port}")
    private int rabbitMQPort;

    @Value("${spring.rabbit.user}")
    private String rabbitMQUser;

    @Value("${spring.rabbit.password}")
    private String rabbitMQPassword;

    @Value("${spring.rabbitmq.virtual-host}")
    private String rabbitMQVirtualHost;

    public String getRabbitMQHost() {
        return rabbitMQHost;
    }

    public void setRabbitMQHost(String rabbitMQHost) {
        this.rabbitMQHost = rabbitMQHost;
    }

    public int getRabbitMQPort() {
        return rabbitMQPort;
    }

    public void setRabbitMQPort(int rabbitMQPort) {
        this.rabbitMQPort = rabbitMQPort;
    }

    public String getRabbitMQUser() {
        return rabbitMQUser;
    }

    public void setRabbitMQUser(String rabbitMQUser) {
        this.rabbitMQUser = rabbitMQUser;
    }

    public String getRabbitMQPassword() {
        return rabbitMQPassword;
    }

    public void setRabbitMQPassword(String rabbitMQPassword) {
        this.rabbitMQPassword = rabbitMQPassword;
    }

    public String getRabbitMQVirtualHost() {
        return rabbitMQVirtualHost;
    }

    public void setRabbitMQVirtualHost(String rabbitMQVirtualHost) {
        this.rabbitMQVirtualHost = rabbitMQVirtualHost;
    }
}
