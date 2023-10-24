package com.example.cpen321tutorial1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class SurpriseQuestion1 extends AppCompatActivity {

    private Button Q1_1;
    private Button Q1_2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_surprise_question1);

        Q1_1 = findViewById(R.id.A1);
        Q1_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent SurpriseIntent = new Intent(SurpriseQuestion1.this, SurpriseQuestion2.class);
                startActivity(SurpriseIntent);
            }
        });

        Q1_2 = findViewById(R.id.A2);
        Q1_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent SurpriseIntent = new Intent(SurpriseQuestion1.this, SurpriseQuestion2.class);
                startActivity(SurpriseIntent);
            }
        });
    }
}