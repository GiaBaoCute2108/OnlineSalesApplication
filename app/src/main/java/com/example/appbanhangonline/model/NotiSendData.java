package com.example.appbanhangonline.model;

import java.util.HashMap;
import java.util.Map;

public class NotiSendData {
    private String token;
    Map<String, String> notification;
    private Map<String, Object> message;

    public NotiSendData(String token, Map<String, String> notification) {
        Map<String, Object> message = new HashMap<>();
        message.put("token", token);
        message.put("notification", notification);
        this.message = message;
    }


    public String getTo() {
        return token;
    }

    public void setTo(String token) {
        this.token = token;
    }

    public Map<String, String> getNotification() {
        return notification;
    }

    public void setNotification(Map<String, String> notification) {
        this.notification = notification;
    }

    public Map<String, Object> getMessage() {
        return message;
    }

    public void setMessage(Map<String, Object> message) {
        this.message = message;
    }
}
