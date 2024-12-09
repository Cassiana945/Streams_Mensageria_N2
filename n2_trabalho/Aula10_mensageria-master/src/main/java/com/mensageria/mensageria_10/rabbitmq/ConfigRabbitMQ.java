package com.mensageria.mensageria_10.rabbitmq;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConfigRabbitMQ {

    private static Properties properties = new Properties();

    static {
        try (InputStream in = ConfigRabbitMQ.class.getClassLoader()
                .getResourceAsStream("application.properties")) {
                properties.load(in);

    }catch(IOException e) {
        e.printStackTrace();
    }
   }

   public static String getProperty(String key) { return properties.getProperty(key);}
}
