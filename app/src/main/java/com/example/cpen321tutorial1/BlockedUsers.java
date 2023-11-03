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

        //TODO GET ALL BLOCKED USERS FROM BACKEND
        List<Account> items = new ArrayList<Account>();



        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new PersonAdapter(getApplicationContext(), items));


        recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(BlockedUsers.this, recyclerView ,new RecyclerItemClickListener.OnItemClickListener() {
                    @Override public void onItemClick(View view, int position) {
                        Intent UnblockIntent = new Intent(BlockedUsers.this, Unblock.class);
                        startActivity(UnblockIntent);
                    }

                    @Override public void onLongItemClick(View view, int position) {
                        // do whatever
                    }
                }));




    }
}