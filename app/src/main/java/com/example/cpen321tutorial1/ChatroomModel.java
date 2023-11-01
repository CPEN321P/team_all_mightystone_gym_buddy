package com.example.cpen321tutorial1;

import java.util.ArrayList;
import java.util.List;

public class ChatroomModel {

    String chatId;
    List<String> userIds;
    //String lastMessageSenderId;


    public ChatroomModel() {
    }

    public ChatroomModel(String chatId, List<String> userIds) {
        this.chatId = chatId;
        this.userIds = userIds;
        //pull messages from backend given chatId
    }

    public String getChatId() {
        return chatId;
    }

    public void setChatId(String chatId) {
        this.chatId = chatId;
    }

    public List<String> getUserIds() {
        return userIds;
    }

    public void setUserIds(List<String> userIds) {
        this.userIds = userIds;
    }
}
