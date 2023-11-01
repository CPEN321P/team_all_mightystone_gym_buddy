package com.example.cpen321tutorial1;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

public class Chat extends AppCompatActivity {

    MainActivity.AccountInfo otherAccountInfo;

    EditText chat_text_input;
    ImageView userImage;
    TextView username;
    ImageButton message_send_button;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        //otherAccountInfo = MainActivity.getAccountInfoFromIntent(getIntent());


        chat_text_input = findViewById(R.id.chat_text_input);
        userImage = findViewById(R.id.userImage);
        username = findViewById(R.id.username);
        message_send_button = findViewById(R.id.message_send_button);

        //TODO: pull otherAccountInfo from backend
        //username.setText(otherAccountInfo.Username);

        //FOR NOW!!!!!!
        username.setText("Example");

        //chat should load previous messages by pulling from chat database



    }
}