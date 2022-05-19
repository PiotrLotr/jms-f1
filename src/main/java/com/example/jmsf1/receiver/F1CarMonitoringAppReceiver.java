package com.example.jmsf1.receiver;

import com.example.jmsf1.configuration.JmsConfig;
import com.example.jmsf1.messages.F1CarStateMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import javax.jms.Message;
import java.time.LocalDateTime;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
@RequiredArgsConstructor
public class F1CarMonitoringAppReceiver {

    private final JmsTemplate jmsTemplate;

    @JmsListener(destination = JmsConfig.TOPIC_PIT_STOP, containerFactory = "topicConnectionFactory")
    public void receiveF1CarMonitorInput(
            @Payload F1CarStateMessage convertedMessage,
            @Headers MessageHeaders messageHeaders,
            Message message) {
        System.out.println("F1CarMonitoringAppReceiver.receiveF1CarMonitorInput, message: " + convertedMessage);

        String msg = convertedMessage.toString();

        if(checkState("CRITICAL", msg)) {
            buildMessage(
                    jmsTemplate,
                    " ! CRITICAL STATE. PIT STOP PROCEDURE ! ",
                    JmsConfig.TOPIC_PREPARE_TO_PIT_STOP);
        }
        if(checkState("EXCEEDED", msg)) {
            buildMessage(
                    jmsTemplate,
                    " ! PARAMETERS EXCEEDED ! ",
                    JmsConfig.QUEUE_CAR_DRIVER);
        }
    }

    private static void buildMessage(JmsTemplate jmsTemplate, String message, String DIRECTION){
        F1CarStateMessage respondMessage = F1CarStateMessage.builder()
                .id(F1CarStateMessage.nextId())
                .createdAt(LocalDateTime.now())
                .message(message)
                .build();
        jmsTemplate.convertAndSend(DIRECTION, respondMessage);
        System.out.println("F1CarMonitoringAppReceiver.receiveF1CarMonitorInput, message: "+message);
    }

    private static Boolean checkState(String state, String msg){
        Pattern pattern = Pattern.compile(state, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(msg);
        return matcher.find();
    }


}
