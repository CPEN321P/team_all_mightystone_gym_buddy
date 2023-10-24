package com.example.cpen321tutorial1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class SurpriseQuestion5 extends AppCompatActivity {

    private Button Q5_1;
    private Button Q5_2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_surprise_question5);

        Q5_1 = findViewById(R.id.A1);
        Q5_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent SurpriseIntent = new Intent(SurpriseQuestion5.this, Answer1.class);
                startActivity(SurpriseIntent);
            }
        });

        Q5_2 = findViewById(R.id.A2);
        Q5_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent SurpriseIntent = new Intent(SurpriseQuestion5.this, Answer2.class);
                startActivity(SurpriseIntent);
            }
        });
    }
}