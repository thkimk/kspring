package com.kkk.sbgtest.kafka;

import org.omg.CORBA.SystemException;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;


@Component
public class KafkaSubscriberService {

//    @KafkaListener(topics = "hello", containerFactory = "pingKafkaListenerContainerFactory")
    public void pingListener(Ping ping, Acknowledgment ack) {
        try {
            System.out.println("Recieved ping message: " + ping);
            ack.acknowledge();
        } catch (Exception e) {
            String msg = "시스템에 예상치 못한 문제가 발생했습니다";
            System.out.println("Recieved ping message: " + msg + e);
        }
    }
}
