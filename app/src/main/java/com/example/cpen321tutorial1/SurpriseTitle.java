package com.example.cpen321tutorial1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class SurpriseTitle extends AppCompatActivity {

    private Button LetRock;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_surprise_title);

        LetRock = findViewById(R.id.button);
        LetRock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent SurpriseIntent = new Intent(SurpriseTitle.this, SurpriseQuestion1.class);
                startActivity(SurpriseIntent);
            }
        });
    }
}