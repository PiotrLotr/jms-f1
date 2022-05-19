package com.example.jmsf1.receiver;

import com.example.jmsf1.configuration.JmsConfig;
import com.example.jmsf1.messages.F1CarStateMessage;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.context.annotation.Bean;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.support.destination.DynamicDestinationResolver;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

@Component
public class LoggerReceiver {

    private static FileWriter fileWriter;
    private static String logFilePath= "c:\\Users\\pwron\\f1Logs.json";
    private static File file = new File(logFilePath);
    private static int logCounter = 0;
    private static JSONArray listOfStates = new JSONArray();


    @JmsListener(destination = JmsConfig.TOPIC_PIT_STOP, containerFactory = "topicConnectionFactory")
    public void receiveF1CarStateMessage(
            @Payload F1CarStateMessage convertedMessage,
            @Headers MessageHeaders messageHeaders,
            Message message) {
        System.out.println("LoggerReceiver.receiveF1CarStateMessage, message: "+convertedMessage);

        JSONObject jsonObject = new JSONObject();
        jsonObject.put(logCounter, convertedMessage);
        listOfStates.add(jsonObject);
        logCounter++;

        try {
            file.setWritable(true);
            file.setReadable(true);
            if(!file.exists()){
                file.createNewFile();
            }
            fileWriter = new FileWriter(file);
            fileWriter.write(String.valueOf(listOfStates));
            fileWriter.close();

            System.out.println("LoggerReceiver.receiveF1CarStateMessage: JSON Object Successfully written to the file");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
