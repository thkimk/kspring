package com.kkk.sbgtest.mq;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

@Service
public class EmailProducer {

    @Autowired
    private JmsTemplate jmsTemplate;

    public void sendMsg(String email, String msg) {
        // jmsTemplate.setPubSubDomain(true); // pubsub 설정시 ...
        jmsTemplate.convertAndSend("mailbox", new Email(email, msg));
        System.out.println("email : "+email);
        System.out.println("message : "+msg);
        System.out.println("Sending an email message.");
    }
}