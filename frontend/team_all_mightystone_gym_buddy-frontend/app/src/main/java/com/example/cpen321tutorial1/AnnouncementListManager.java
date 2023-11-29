package com.example.cpen321tutorial1;

import static com.example.cpen321tutorial1.GlobalClass.SelectedAnnouncement;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;

public class AnnouncementListManager extends AppCompatActivity {

    private ArrayList<String> TheList = new ArrayList<String>();

    private ListView listView;

    private Button Cancel;

    private Button Add;

    final static String TAG = "AnnouncementListManager";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_announcement_list_manager);
        initWidgets();
        //SelectedAnnouncement = new Announcement();

        for (int i = 0; i < GlobalClass.AnnouncementList.size(); i++){
            TheList.add(GlobalClass.AnnouncementList.get(i).getHeader());
        }

        listView = (ListView) findViewById(R.id.MessageList);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>
                (this, R.layout.activity_announcement_list_view, R.id.textView, TheList);

        listView.setAdapter(arrayAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Log.d(TAG, TheList.get(i).toString());
                SelectedAnnouncement = GlobalClass.AnnouncementList.get(i);

                Intent Intent =
                        new Intent(AnnouncementListManager.this,
                                AnnouncementDetail.class);
                startActivity(Intent);
            }
        });

        Cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "Trying get Back");
                Intent Intent =
                        new Intent(AnnouncementListManager.this,
                                PersonalProfileManager.class);
                startActivity(Intent);
            }
        });

        Add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "Add Announcement");
                Intent Intent =
                        new Intent(AnnouncementListManager.this,
                                AnnouncementAdd.class);
                startActivity(Intent);
            }

        });
    }

    private void initWidgets(){
        Cancel = findViewById(R.id.Cancel);
        Add = findViewById(R.id.Add);
    }
}