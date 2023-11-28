package com.example.cpen321tutorial1;

public class ChatMessage {

    //private String _id;
    Long schedule;
    //senderId

    String sender;

    String body;

    public ChatMessage(Long schedule, String sender, String body) {
        this.schedule = schedule;
        this.sender = sender;
        this.body = body;
    }

    public Long getSchedule() {
        return schedule;
    }

    public void setSchedule(Long schedule) {
        this.schedule = schedule;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String toString() {
        return String.format("schedule:%s,sender:%s,body:%s",
                schedule, sender, body);
    }
}
