package com.example.cpen321tutorial1;

import static com.example.cpen321tutorial1.GlobalClass.client;
import static com.example.cpen321tutorial1.GlobalClass.manager;
import static com.example.cpen321tutorial1.JsonFunctions.NewCallPost;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import okhttp3.FormBody;
import okhttp3.Request;
import okhttp3.RequestBody;

public class AnnouncementAdd extends AppCompatActivity {

    private Button Cancel;

    private Button Add;

    private TextView Head;

    private TextView Description;

    final static String TAG = "AnnouncementAdd";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_announcement_add);
        initWidgets();

        Add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String Title = Head.getText().toString();
                String Body = Description.getText().toString();

                Announcement NewAccouncement = new Announcement(Title, Body);
                GlobalClass.AnnouncementList.add(NewAccouncement);

                //Put something into the backend

                RequestBody formBody = new FormBody.Builder()
                        .add("header", Title)
                        .add("body", Body)
                        .build();

                Request requestName = new Request.Builder()
                        .url("https://20.172.9.70/gymsUsers/makeAnnouncement/" +
                                manager.get_id())
                        .put(formBody)
                        .build();

                NewCallPost(client, requestName);

                Toast.makeText(AnnouncementAdd.this,
                        "Add Announcement Success!!!",
                        Toast.LENGTH_SHORT).show();

                Intent Intent =
                        new Intent(AnnouncementAdd.this,
                                AnnouncementListManager.class);
                startActivity(Intent);
            }
        });

        Cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                //Put something into the backend
            }
        });
    }

    private void initWidgets(){
        Cancel = findViewById(R.id.Cancel);
        Add = findViewById(R.id.Add);
        Head = findViewById(R.id.Head);
        Description = findViewById(R.id.Description);
    }
}