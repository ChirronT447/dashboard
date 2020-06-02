package com.gateway.dashboard.websocket;

import java.time.LocalDateTime;

public class Message {

    private String name;
    private LocalDateTime messageUpdated;

    public Message() {}

    public Message(String name) {
        this.name = name;
        setMessageUpdated();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        setMessageUpdated();
    }

    public void setMessageUpdated() {
        this.messageUpdated = LocalDateTime.now();
    }

    public String getMessageUpdated() {
        return messageUpdated.toString();
    }
}
