package com.example.cpen321tutorial1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class GymProfile extends AppCompatActivity {

    private TextView Name, Location, AccessTime, Website, Tips;
    private Button Subscript, CancelSubscript;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_profile);
        initWidgets();

        //Get the relative information from the database
        //Name.setText();
        //Location.setText();
        //AccessTime.setText();
        //Website.setText();
        //Tips.setText();

        Subscript.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ////////////////////////////////////////////////////////////////////////////////////////
                //Get the Gym class file from the backend database base on the information of the Gym///
                //Add the current user into the SubscriptedUser arraylist of this gym///////////////////
                ////////////////////////////////////////////////////////////////////////////////////////
            }
        });

        CancelSubscript.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ////////////////////////////////////////////////////////////////////////////////////////
                //Get the Gym class file from the backend database base on the information of the Gym///
                //Remove the current user into the SubscriptedUser arraylist of this gym///////////////////
                ////////////////////////////////////////////////////////////////////////////////////////
            }
        });
    }

    private void initWidgets() {
        Location = findViewById(R.id.Location);
        AccessTime = findViewById(R.id.AccessTime);
        Website = findViewById(R.id.Website);
        Name = findViewById(R.id.GymName);
        Tips = findViewById(R.id.Tips);
        Subscript = findViewById(R.id.Subscript);
        CancelSubscript = findViewById(R.id.CancelSubscription);
    }
}