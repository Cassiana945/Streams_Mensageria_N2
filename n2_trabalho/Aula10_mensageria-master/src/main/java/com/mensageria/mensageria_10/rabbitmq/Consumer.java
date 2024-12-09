package com.mensageria.mensageria_10.rabbitmq;

import com.mensageria.mensageria_10.dao.MessageDAO;
import com.mensageria.mensageria_10.model.Message;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.TimeoutException;

public class Consumer {
    private static final String QUEUE_NAME = ConfigRabbitMQ.getProperty("amqp.queueName");
    private static final String URI = ConfigRabbitMQ.getProperty("amqp.uri");

    public static void main(String[] args) throws URISyntaxException, NoSuchAlgorithmException, KeyManagementException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setUri(URI);

        try (Connection connection = factory.newConnection();
             Channel channel = connection.createChannel()) {
            channel.queueDeclare(QUEUE_NAME, true, false, false, null);
            System.out.println("Esperando mensagem...");


            DeliverCallback deliverCallback = (consumerTag, delivery) -> {
                String message = new String(delivery.getBody(), StandardCharsets.UTF_8);
                System.out.println("Mensagem recebida: " + message);


                processMessage(message);
            };


            channel.basicConsume(QUEUE_NAME, true, deliverCallback, consumerTag -> {});

        } catch (IOException | TimeoutException e) {
            e.printStackTrace();
        }
    }

    private static void processMessage(String message) {

        String[] messageParts = message.split("\\|");

        // Condição para garantir que haja ao menos 2 partes na mensagem (ação e o primeiro parâmetro)
        if (messageParts.length < 2) {
            System.err.println("Mensagem mal formatada: " + message);
            return;
        }

        String action = messageParts[0];  // Ação: CREATE, UPDATE, DELETE

        // Agora, vamos verificar as diferentes ações:
        if ("CREATE".equals(action)) {
            if (messageParts.length < 3) {
                System.err.println("Mensagem CREATE mal formatada: " + message);
                return;
            }

            String cidade = messageParts[1];
            String alerta = messageParts[2];
            MessageDAO messageDAO = new MessageDAO();

            try {
                Message msg = new Message();
                msg.setCidade(cidade);
                msg.setAlerta(alerta);
                messageDAO.create(msg);
                System.out.println("Alerta inserido no banco de dados.");
            } catch (Exception e) {
                e.printStackTrace();
            }

        } else if ("UPDATE".equals(action)) {
            if (messageParts.length < 4) {
                System.err.println("Mensagem UPDATE mal formatada: " + message);
                return;
            }

            String cidade = messageParts[1];
            String alerta = messageParts[2];
            Long id = Long.parseLong(messageParts[3]);
            MessageDAO messageDAO = new MessageDAO();

            try {
                Message msg = new Message();
                msg.setId(id);
                msg.setCidade(cidade);
                msg.setAlerta(alerta);
                messageDAO.update(msg);
                System.out.println("Alerta atualizado no banco de dados.");
            } catch (Exception e) {
                e.printStackTrace();
            }

        } else if ("DELETE".equals(action)) {
            if (messageParts.length < 2) {
                System.err.println("Mensagem DELETE mal formatada: " + message);
                return;
            }

            Long id = Long.parseLong(messageParts[1]);
            MessageDAO messageDAO = new MessageDAO();

            try {
                messageDAO.delete(id);
                System.out.println("Alerta deletado do banco de dados.");
            } catch (Exception e) {
                e.printStackTrace();
            }

        } else {
            System.err.println("Ação desconhecida: " + action);
        }
    }
}
