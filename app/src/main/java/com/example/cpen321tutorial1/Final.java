package com.example.cpen321tutorial1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Final extends AppCompatActivity {

    private Button Video;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_final);

        Video = findViewById(R.id.Video);
        Video.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent SurpriseIntent = new Intent(Final.this, Video.class);
                startActivity(SurpriseIntent);
            }
        });
    }
}