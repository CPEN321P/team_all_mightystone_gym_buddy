package com.example.cpen321tutorial1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class SurpriseQuestion2 extends AppCompatActivity {

    private Button Q2_1;
    private Button Q2_2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_surprise_question2);

        Q2_1 = findViewById(R.id.A1);
        Q2_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent SurpriseIntent = new Intent(SurpriseQuestion2.this, SurpriseQuestion3.class);
                startActivity(SurpriseIntent);
            }
        });

        Q2_2 = findViewById(R.id.A2);
        Q2_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent SurpriseIntent = new Intent(SurpriseQuestion2.this, SurpriseQuestion3.class);
                startActivity(SurpriseIntent);
            }
        });
    }
}