package com.example.cpen321tutorial1;

import static com.example.cpen321tutorial1.GlobalClass.myAccount;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import java.util.List;

public class Messages
        extends AppCompatActivity {

    ConnectionToBackend c = new ConnectionToBackend();
    List<ChatModelFromBackend> items = c.getAllChatsFromUserId(myAccount.getUserId());


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messages);

        RecyclerView recyclerView = findViewById(R.id.recyclerview);


        recyclerView.setLayoutManager
                (new LinearLayoutManager(this));
        recyclerView.setAdapter
                (new ChatAdapter(getApplicationContext(), items));

        recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(Messages.this,
                        recyclerView ,
                        new RecyclerItemClickListener.OnItemClickListener() {

                    @Override public void onItemClick(View view, int position) {
                        Intent ChatIntent =
                                new Intent(Messages.this, Chat.class);

                        ChatModelFromBackend chatModelFromBackend = items.get(position);
                        Account otherAccount;
                        if(chatModelFromBackend.members.get(0).equals(myAccount.getUserId())) {
                            otherAccount = c.getAccountInformation(chatModelFromBackend.members.get(1));
                        } else{
                            otherAccount = c.getAccountInformation(chatModelFromBackend.members.get(0));

                        }

                        ChatIntent.putExtra("Username", otherAccount.getUsername());
                        ChatIntent.putExtra("FriendId", otherAccount.getUserId());

                        startActivity(ChatIntent);
                    }

                    @Override public void onLongItemClick
                            (View view, int position) {
                        // do whatever
                    }
                }));
    }
}