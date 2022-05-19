package com.example.jmsf1.receiver;

import com.example.jmsf1.configuration.JmsConfig;
import com.example.jmsf1.messages.F1CarStateMessage;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import javax.jms.Message;

@Component
public class PitStopMechanicsReceiver {

    @JmsListener(destination = JmsConfig.TOPIC_PREPARE_TO_PIT_STOP, containerFactory = "topicConnectionFactory")
    public void receivePitStopRequest(
            @Payload F1CarStateMessage convertedMessage,
            @Headers MessageHeaders messageHeaders,
            Message message) {
        System.out.println("PitStopMechanicsReceiver.receivePitStopRequest, message: "+convertedMessage);

    }



}
