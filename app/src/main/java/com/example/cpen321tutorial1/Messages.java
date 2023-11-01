package com.example.cpen321tutorial1;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

public class Messages extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messages);

        RecyclerView recyclerView = findViewById(R.id.recyclerview);

        List<PersonItem> items = new ArrayList<PersonItem>();
        items.add(new PersonItem("John Doe", "", R.drawable.user));
        items.add(new PersonItem("Jane Doe", "", R.drawable.user));
        items.add(new PersonItem("Zheng Xu", "", R.drawable.user));
        items.add(new PersonItem("Joy Choi", "", R.drawable.user));
        items.add(new PersonItem("Savitoj Sachar", "", R.drawable.user));
        items.add(new PersonItem("Tyson Brown", "", R.drawable.user));


        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new PersonAdapter(getApplicationContext(), items));

        recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(Messages.this, recyclerView ,new RecyclerItemClickListener.OnItemClickListener() {
                    @Override public void onItemClick(View view, int position) {
                        Intent ChatIntent = new Intent(Messages.this, Chat.class);

                        //import recipient's account information from backend

                        //MainActivity.passAccountAsIntent(ChatIntent, RecipientAccount);

                        startActivity(ChatIntent);
                    }

                    @Override public void onLongItemClick(View view, int position) {
                        // do whatever
                    }
                }));
    }
}