package com.example.cpen321tutorial1;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URISyntaxException;
import java.security.KeyManagementException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.net.ssl.SSLContext;

import io.socket.client.Ack;
import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

public class Chat extends AppCompatActivity {

    Account otherAccount;

    String chatId;

    List<ChatMessage> messages = new ArrayList<ChatMessage>();

    //ChatModelFromBackend chatModelFromBackend;

    EditText chat_text_input;

    ImageView userImage;

    TextView username;

    ImageButton message_send_button;

    Socket socket;

    RecyclerView recyclerView;
    String friendId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        setContentView(R.layout.activity_chat);

        recyclerView = findViewById(R.id.recyclerview);

        recyclerView.setLayoutManager
                (new LinearLayoutManager(this));

        recyclerView.setAdapter(new ChatMessageAdapter(this, messages));

        Intent i = getIntent();
        String name = i.getStringExtra("Username");
        friendId = i.getStringExtra("FriendId");

        chat_text_input = findViewById(R.id.chat_text_input);
        userImage = findViewById(R.id.userImage);
        username = findViewById(R.id.username);
        message_send_button = findViewById(R.id.message_send_button);

        username.setText(name);
        //ConnectionToBackend c = new ConnectionToBackend();

        //check if this chat already exists on the backend
        if(!checkIfChatExists(friendId)){
            //create new chat


        } else{

        }


//        Map<String, String> userIdMap = new HashMap<>();
//        userIdMap.put("userId", "1234");
//        IO.Options socketOptions = IO.Options.builder().setAuth(userIdMap).build();

        try {

            IO.Options options = new IO.Options();
            options.forceNew = true;
            socket = IO.socket("https://tams.westus3.cloudapp.azure.com/", options);
            socket.connect();
            Log.d("SOCKETTTT", "CONNECTED BROOOOO");
        } catch (URISyntaxException e) {
            Log.d("SOCKET ISSUES!", Log.getStackTraceString(e));
        }

        Log.d("SOCKETTTT", "connected");

        //join room in socket
        JSONObject jsonJoinRoom = new JSONObject();
        try {
            jsonJoinRoom.put("myID", GlobalClass.myAccount.getUserId());
            jsonJoinRoom.put("theirID", friendId);

        } catch (JSONException e) {
            Log.d("JSON ISSUES", Log.getStackTraceString(e));
        }

        socket.emit("join_chat", jsonJoinRoom, (Ack) args -> {
            JSONObject response = (JSONObject) args[0];
            try {
                Log.d("SOCKET STUFF", "Emit event response code is " + response.getString("status"));
            }
            catch (JSONException e) {
                Log.d("SOCKET ISSUES!!!", Log.getStackTraceString(e));
            }
        });

        Log.d("SOCKETTTTT", "joined room?");

        message_send_button.setOnClickListener((view -> {
            String message = chat_text_input.getText().toString().trim();
            if(message.isEmpty()){
                Toast.makeText(this, "You have to input something in your text!", Toast.LENGTH_SHORT).show();;
            }
            else {
                sendMessage(message);
                updateUI();
            }
        }));

        //listen for new messages
        socket.on("send_message", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                JSONObject messageObjReceived = (JSONObject) args[0];
                Chat.this.runOnUiThread(() -> {
                    try {
                        String myId = messageObjReceived.getString("myId");
                        String theirId = messageObjReceived.getString("theirId");
                        String message = messageObjReceived.getString("message");

                         ChatMessage thisMessage = new ChatMessage(new Long(0), theirId, message);
                         messages.add(thisMessage);
                         updateUI();

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                });
            }
        });



    }

    private void LoadPreviousMessages(Chat thisChat) {



    }

    private void sendMessage(String message) {

        ChatMessage chatMessage = new ChatMessage(new Long(0), GlobalClass.myAccount.getUserId(), message);
        messages.add(chatMessage);

        //emit socket
        JSONObject jsonMessage = new JSONObject();
        try {
            jsonMessage.put("myID", GlobalClass.myAccount.getUserId());
            jsonMessage.put("theirID", friendId);
            jsonMessage.put("message", message);

        } catch (JSONException e) {
            Log.d("JSON ISSUES", Log.getStackTraceString(e));
        }
        socket.emit("send_message", jsonMessage, (Ack) args -> {
            JSONObject response = (JSONObject) args[0];
            try {
                Log.d("SOCKET STUFF", "Emit event response code is " + response.getString("status"));
            }
            catch (JSONException e) {
                Log.d("SOCKET ISSUES!!!", Log.getStackTraceString(e));
            }
        });

        chat_text_input.setText("");
    }


    private void updateUI(){

        recyclerView.setAdapter(new ChatMessageAdapter(this, messages));
        recyclerView.smoothScrollToPosition(messages.size() - 1);

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
        ChatModelFromBackend thisChat = c.getChatFromFriendId(friendId);

        if(thisChat== null){
            Log.d("THISSSSSSS", "chat is null :c");
            return false;
        }

        return true;

    }
}