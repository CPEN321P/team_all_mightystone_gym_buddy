package com.example.cpen321tutorial1;

import static com.example.cpen321tutorial1.GlobalClass.SelectedAnnouncement;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class AnnouncementDetail extends AppCompatActivity {

    private Button Back;

    private TextView Header;

    private TextView Body;

    final static String TAG = "AnnouncementDetail";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_announcement_detail);
        initWidgets();

        Header.setText(SelectedAnnouncement.getHeader());
        Body.setText(SelectedAnnouncement.getBody());

        Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void initWidgets(){
        Header = findViewById(R.id.Header);
        Body = findViewById(R.id.Description);
        Back = findViewById(R.id.Back);
    }
}