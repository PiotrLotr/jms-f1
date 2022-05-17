package com.example.jmsf1.receiver;

import com.example.jmsf1.configuration.JmsConfig;
import com.example.jmsf1.messages.F1CarStateMessage;
import org.json.simple.JSONObject;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import javax.jms.Message;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

@Component
public class LoggerReceiver {

    private static FileWriter fileWriter;
    private static String logFilePath= "c:\\Users\\pwron\\f1Logs.json";


    @JmsListener(destination = JmsConfig.TOPIC_PIT_STOP, containerFactory = "topicConnectionFactory")
    public void receiveF1CarStateMessage(
            @Payload F1CarStateMessage convertedMessage,
            @Headers MessageHeaders messageHeaders,
            Message message) {
        System.out.println("LoggerReceiver.receiveF1CarStateMessage, message: "+convertedMessage);

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("F1CarState", "what the fuck?");

        try {
            File file = new File(logFilePath);
            file.setWritable(true);
            file.setReadable(true);

            if(!file.exists()){
                file.createNewFile();
            }
            fileWriter = new FileWriter(file);
            fileWriter.write(jsonObject.toJSONString());
            fileWriter.close();

            System.out.println("LoggerReceiver.receiveF1CarStateMessage: JSON Object Successfully written to the file");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
