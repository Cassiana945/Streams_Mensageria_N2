package com.mensageria.mensageria_10.rabbitmq;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.net.URISyntaxException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.TimeoutException;

public class Producer {

    private static final String QUEUE_NAME = ConfigRabbitMQ.getProperty("amqp.queueName");
    private static final String URI = ConfigRabbitMQ.getProperty("amqp.uri");


    public static void sendMessage(String message) {
        try {
            ConnectionFactory factory = new ConnectionFactory();
            factory.setUri(URI);

            try (Connection connection = factory.newConnection(); Channel channel = connection.createChannel()) {
                channel.queueDeclare(QUEUE_NAME, true, false, false, null);
                channel.basicPublish("", QUEUE_NAME, null, message.getBytes());
                System.out.println("Mensagem enviada: " + message);
            }
        } catch (IOException | TimeoutException | URISyntaxException | KeyManagementException | NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }
}
