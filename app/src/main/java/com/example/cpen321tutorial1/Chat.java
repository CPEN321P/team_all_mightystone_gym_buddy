package com.example.cpen321tutorial1;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

public class Chat extends AppCompatActivity {

    Account otherAccount;
    String chatroomId;
    ChatroomModel chatroomModel;

    EditText chat_text_input;
    ImageView userImage;
    TextView username;
    ImageButton message_send_button;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        //otherAccount = MainActivity.getAccountFromIntent(getIntent());


        chat_text_input = findViewById(R.id.chat_text_input);
        userImage = findViewById(R.id.userImage);
        username = findViewById(R.id.username);
        message_send_button = findViewById(R.id.message_send_button);

        //TODO: pull otherAccount from backend
        //username.setText(otherAccount.Username);

        //FOR NOW!!!!!!
        username.setText("Example");

        //get chatroomId from backend

        //getChatroom from chatroomId
        getOrCreateChatroomModel();

        message_send_button.setOnClickListener((view -> {
            String message = chat_text_input.getText().toString().trim();
            if(message.isEmpty()){
                return;
            }
            sendMessageToUser(message);
        }));

    }

    private void sendMessageToUser(String message) {


        //ChatMessageModel chatMessageModel = new ChatMessageModel(message, )


    }

    private void getOrCreateChatroomModel() {
        //get chatroom from backend

        //chatroomModel = ;

    }
}