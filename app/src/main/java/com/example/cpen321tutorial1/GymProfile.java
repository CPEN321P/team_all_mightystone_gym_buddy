package com.example.cpen321tutorial1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class GymProfile extends AppCompatActivity {

    TextView Name, Location, AccessTime, Website;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_profile);
        initWidgets();

        //Get the relative information from the database
        //Location.setText();
        //AccessTime.setText();
        //Website.setText();
    }

    private void initWidgets() {
        Location = findViewById(R.id.Location);
        AccessTime = findViewById(R.id.AccessTime);
        Website = findViewById(R.id.Website);
        Name = findViewById(R.id.GymName);
    }
}