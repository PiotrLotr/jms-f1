package com.example.jmsf1.sender;

import com.example.jmsf1.configuration.JmsConfig;
import com.example.jmsf1.messages.F1CarStateMessage;
import com.example.jmsf1.messages.PitStopRequestMessage;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;
import javax.jms.TextMessage;
import java.time.LocalDateTime;


@Component
@RequiredArgsConstructor
public class SendAndReceiveProducer {

    private final JmsTemplate jmsTemplate;
    private final ObjectMapper objectMapper;

    @Scheduled(fixedRate = 30000)
    public void sendAndReceive() throws JMSException, JsonProcessingException {
        PitStopRequestMessage message = PitStopRequestMessage.builder()
                .id(PitStopRequestMessage.nextId())
                .createdAt(LocalDateTime.now())
                .message(generateDriverBehaviour())
                .build();
        TextMessage responseMessage = (TextMessage) jmsTemplate.sendAndReceive(
                JmsConfig.QUEUE_SEND_AND_RECEIVE, new MessageCreator() {
                    @Override
                    public Message createMessage(Session session) throws JMSException {
                        TextMessage plainMessage = session.createTextMessage();
                        try {
                            plainMessage.setText(objectMapper.writeValueAsString(message));
                            plainMessage.setStringProperty("_type",
                                    PitStopRequestMessage.class.getName());
                            return plainMessage;
                        } catch (JsonProcessingException e) {
                            throw new JMSException("conversion to json failed: " +
                                    e.getMessage());
                        }
                    }
                });
        String responseText = responseMessage.getText();
        PitStopRequestMessage responseConverted = objectMapper.readValue(responseText, PitStopRequestMessage.class);
        System.out.println("F1CarDriverCommunicator.sendAndReceive got response: " +responseText+"\n\tconvertedMessage: "+responseConverted);
    }

    @JmsListener(destination = JmsConfig.TOPIC_PREPARE_TO_PIT_STOP, containerFactory = "topicConnectionFactory")
    public void receivePitStopRequest(
            @Payload F1CarStateMessage convertedMessage,
            @Headers MessageHeaders messageHeaders,
            Message message) {
        System.out.println("F1CarDriverCommunicator.sendAndReceive got response: "+convertedMessage);

    }

    @JmsListener(destination = JmsConfig.QUEUE_CAR_DRIVER, containerFactory = "queueConnectionFactory")
    public void receiveMonitoringInfo(
            @Payload F1CarStateMessage convertedMessage,
            @Headers MessageHeaders messageHeaders,
            Message message) {
        System.out.println("F1CarDriverCommunicator.sendAndReceive got response: "+convertedMessage);

    }

    private static String generateDriverBehaviour(){
        if(Math.random() < 0.5) {
            return "F1CarDriver: ! REQUEST TO VISIT PIT STOP !";
        } else {
            return "";
        }
    }

}
