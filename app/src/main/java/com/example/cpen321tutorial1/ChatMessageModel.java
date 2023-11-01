package com.example.cpen321tutorial1;

import java.sql.Timestamp;

public class ChatMessageModel {
    String message;
    String senderId;
    //Timestamp timestamp;


    public ChatMessageModel() {
    }

    public ChatMessageModel(String message, String senderId) {
        this.message = message;
        this.senderId = senderId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }
}
