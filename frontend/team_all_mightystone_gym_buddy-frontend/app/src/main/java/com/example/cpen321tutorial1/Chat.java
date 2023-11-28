package com.example.cpen321tutorial1;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class Chat extends AppCompatActivity {

    Account otherAccount;

    String chatId;

    List<ChatMessage> messages = new ArrayList<ChatMessage>();

    //ChatModelFromBackend chatModelFromBackend;

    EditText chat_text_input;

    ImageView userImage;

    TextView username;

    ImageButton message_send_button;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

//        RecyclerView recyclerView = findViewById(R.id.recyclerview);
//
//        recyclerView.setLayoutManager
//                (new LinearLayoutManager(this));
//        recyclerView.setAdapter
//                (new MessageAdapter(getApplicationContext(), items));


        Intent i = getIntent();
        String name = i.getStringExtra("Username");
        String friendId = i.getStringExtra("FriendId");


        chat_text_input = findViewById(R.id.chat_text_input);
        userImage = findViewById(R.id.userImage);
        username = findViewById(R.id.username);
        message_send_button = findViewById(R.id.message_send_button);

        //FOR NOW!!!!!!
        username.setText(name);
        ConnectionToBackend c = new ConnectionToBackend();

        //check if this chat already exists on the backend
        if(checkIfChatExists(friendId)){
            //get chat information and set messages
            Log.d("thisss", "reached here");
            Chat thisChat = c.getChatFromFriendId(friendId);
            LoadPreviousMessages(thisChat);
        }





        message_send_button.setOnClickListener((view -> {
            String message = chat_text_input.getText().toString().trim();
            if(message.isEmpty()){
                return;
            }
            sendMessage(message);
        }));

    }

    private void LoadPreviousMessages(Chat thisChat) {



    }

    private void sendMessage(String message) {
        //ChatMessageModel chatMessageModel = new ChatMessageModel(message, )
        chat_text_input.setText("");
        //socket stuff send to database and then
    }

    private void updateUI(){
        //socket stuff to make messages appear i guess?

    }
    public Account getOtherAccount() {
        return otherAccount;
    }

    public void setOtherAccount(Account otherAccount) {
        this.otherAccount = otherAccount;
    }

    public String getChatId() {
        return chatId;
    }

    public void setChatId(String chatId) {
        this.chatId = chatId;
    }

    public List<ChatMessage> getMessages() {
        return messages;
    }

    public void setMessages(List<ChatMessage> messages) {
        this.messages = messages;
    }

    private boolean checkIfChatExists(String friendId) {
        ConnectionToBackend c = new ConnectionToBackend();
        Chat thisChat = c.getChatFromFriendId(friendId);

        //Log.d("THISSSSSSS", email + " in login page");

        if(thisChat== null){
            Log.d("THISSSSSSS", "chat is null :c");
            return false;
        }

        return true;

    }
}