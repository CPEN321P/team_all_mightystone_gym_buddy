package com.example.cpen321tutorial1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class SurpriseQuestion3 extends AppCompatActivity {

    private Button Q3_1;
    private Button Q3_2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_surprise_question3);

        Q3_1 = findViewById(R.id.A1);
        Q3_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent SurpriseIntent = new Intent(SurpriseQuestion3.this, SurpriseQuestion4.class);
                startActivity(SurpriseIntent);
            }
        });

        Q3_2 = findViewById(R.id.A2);
        Q3_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent SurpriseIntent = new Intent(SurpriseQuestion3.this, SurpriseQuestion4.class);
                startActivity(SurpriseIntent);
            }
        });
    }
}