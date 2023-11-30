package com.example.cpen321tutorial1;

import java.util.List;

public class ChatModelFromBackend {

    String _id;

    List<String> members;

    List<ChatMessage> messages;

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public List<String> getMembers() {
        return members;
    }

    public void setMembers(List<String> members) {
        this.members = members;
    }

    public List<ChatMessage> getMessages() {
        return messages;
    }

    public void setMessages(List<ChatMessage> messages) {
        this.messages = messages;
    }

    public String toString() {
        return String.format("_id:%s,members:%s,chatMessages:%s",
                _id, members, messages);
    }
}
