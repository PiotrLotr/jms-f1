package com.example.jmsf1.sender;

import com.example.jmsf1.messages.F1CarStateMessage;
import com.example.jmsf1.configuration.JmsConfig;
import com.example.jmsf1.model.F1Car;
import lombok.RequiredArgsConstructor;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;


import java.time.LocalDateTime;
import java.util.ArrayList;


@Component
@RequiredArgsConstructor
public class F1CarTopicProducer {

    private final JmsTemplate jmsTemplate;


    @Scheduled(fixedRate = 15000)
    public void sendF1CarStateMessage() {
        ArrayList<Double> carParameters = F1Car.getF1CarParameters();

        F1CarStateMessage message = F1CarStateMessage.builder()
                .id(F1CarStateMessage.nextId())
                .createdAt(LocalDateTime.now())
                .message("\n"+
                        "engineTemp: " + carParameters.get(0)+"C" + " STATUS: " + translateInput(carParameters.get(1)) + "\n"
                        +"tyresPress: " + carParameters.get(2)+"Bar" + " STATUS: " + translateInput(carParameters.get(3)) + "\n"
                        +"oilPress: " + carParameters.get(4)+"KPa" + " STATUS: " + translateInput(carParameters.get(5)) + "\n"
                )
                .build();
        jmsTemplate.convertAndSend(JmsConfig.TOPIC_PIT_STOP, message);
        System.out.println("F1CarTopicProducer.sendF1CarStateMessage - sent message: " + "\n" + message);
    }

    private static String translateInput(double value){
        if(value == 1){
            return "Exceeded!";
        } else {
            return "not exceeded";
        }
    }




}
