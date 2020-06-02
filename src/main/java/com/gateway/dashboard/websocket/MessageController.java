package com.gateway.dashboard.websocket;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;

@Controller
@EnableScheduling
public class MessageController {

    @Autowired
    private SimpMessagingTemplate template;

    //@Autowired
    //private final MessagesSupplier messagesSupplier;

    // @MessageMapping("/stream")
    // @SendTo("/topic/update")
    public Message greeting(Message message) throws Exception {
        System.out.println("Message received: " + message.getName());
        Thread.sleep(1000); // simulated delay
        message.setMessageUpdated();
        return message;
    }

    //@Scheduled(fixedDelay = 5000)
    //@MessageMapping("/stream")
    //@SendTo("/topic/update")
    public Message update() throws Exception {
        Message message = new Message("New message :) ");
        System.out.println("Message sending: " + message.getName());
        Thread.sleep(1000); // simulated delay
        return message;
    }

    @Scheduled(fixedDelay=10000)
    public void publishUpdates(){
        Message message = new Message("New message :) ");
        System.out.println("Message sending: " + message.getName());
        template.convertAndSend("/topic/update", message);
    }

}
