package com.example.jmsf1.receiver;

import com.example.jmsf1.configuration.JmsConfig;
import com.example.jmsf1.messages.PitStopRequestMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.Payload;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class SendAndReceiveReceiver {

    private final JmsTemplate jmsTemplate;
    @JmsListener(destination = JmsConfig.QUEUE_SEND_AND_RECEIVE)
    public void receiveAndRespond(@Payload PitStopRequestMessage convertedMessage,
                                  @Headers MessageHeaders headers,
                                  Message message) throws JMSException {
        System.out.println("F1CarDriver.receiveAndRespond message: "+ convertedMessage);

                Destination replyTo = message.getJMSReplyTo();
        PitStopRequestMessage msg = PitStopRequestMessage.builder()
                .id(PitStopRequestMessage.nextId())
                .createdAt(LocalDateTime.now())
                .message(generateChiefBehaviour(convertedMessage))
                .build();

        System.out.println("F1TeamChief.receiveAndRespond message: " + msg);
        jmsTemplate.convertAndSend(replyTo, msg);
    }

        private static String generateChiefBehaviour(PitStopRequestMessage convertedMessage) {
        if(convertedMessage.getMessage().length() > 0){
            if(Math.random() < 0.5) {
                return " ! ACCEPTED !";
            } else {
                return " ! DENIED ! ";
            }
        } else {
            return "";
        }
    }

    private static Boolean compareStrings(String str1, String str2){
        int l1 = str1.length();
        int l2 = str2.length();
        if (l1 - l2 == 0){
            return true;
        }
        return false;
    }

}
