package com.example.cpen321tutorial1;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

public class BlockedUsers extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blocked_users);

        RecyclerView recyclerView = findViewById(R.id.recyclerview);


        List<PersonItem> items = new ArrayList<PersonItem>();
        items.add(new PersonItem("John Doe", "BLOCKED", R.drawable.user));
        items.add(new PersonItem("Jane Doe", "BLOCKED", R.drawable.user));
        items.add(new PersonItem("Zheng Xu", "BLOCKED", R.drawable.user));
        items.add(new PersonItem("Joy Choi", "BLOCKED", R.drawable.user));
        items.add(new PersonItem("Savitoj Sachar", "BLOCKED", R.drawable.user));
        items.add(new PersonItem("Tyson Brown", "BLOCKED", R.drawable.user));


        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new PersonAdapter(getApplicationContext(), items));


        recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(BlockedUsers.this, recyclerView ,new RecyclerItemClickListener.OnItemClickListener() {
                    @Override public void onItemClick(View view, int position) {
//                        Intent FriendIntent = new Intent(Friends.this, PersonalProfileFriend.class);
//                        startActivity(FriendIntent);
                    }

                    @Override public void onLongItemClick(View view, int position) {
                        // do whatever
                    }
                }));




    }
}