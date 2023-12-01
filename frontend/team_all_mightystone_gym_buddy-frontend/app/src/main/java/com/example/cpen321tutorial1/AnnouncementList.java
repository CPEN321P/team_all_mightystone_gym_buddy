package com.example.cpen321tutorial1;

import static com.example.cpen321tutorial1.GlobalClass.SelectedAnnouncement;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;

public class AnnouncementList extends AppCompatActivity {

    private ArrayList<String> TheList = new ArrayList<String>();

    ListView listView;

    private Button Cancel;

    final static String TAG = "AnnouncementList";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_announcement_list);
        initWidgets();

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
                SelectedAnnouncement = GlobalClass.AnnouncementList.get(i);

                Intent Intent =
                        new Intent(AnnouncementList.this,
                                AnnouncementDetail.class);
                startActivity(Intent);
            }
        });

        Cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void initWidgets(){
        Cancel = findViewById(R.id.Cancel);
    }
}