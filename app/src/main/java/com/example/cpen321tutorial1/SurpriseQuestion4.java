package com.example.cpen321tutorial1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class SurpriseQuestion4 extends AppCompatActivity {

    private Button Q4_1;
    private Button Q4_2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_surprise_question4);

        Q4_1 = findViewById(R.id.A1);
        Q4_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent SurpriseIntent = new Intent(SurpriseQuestion4.this, SurpriseQuestion5.class);
                startActivity(SurpriseIntent);
            }
        });

        Q4_2 = findViewById(R.id.A2);
        Q4_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent SurpriseIntent = new Intent(SurpriseQuestion4.this, SurpriseQuestion5.class);
                startActivity(SurpriseIntent);
            }
        });
    }


}