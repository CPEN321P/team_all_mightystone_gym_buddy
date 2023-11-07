package com.example.cpen321tutorial1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class PersonalProfileManager
        extends AppCompatActivity {

    private Button MyGyms;

    private TextView Username;

    private TextView Email;

    private Button TheAnnouncement;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_profile_manager);
        initWidgets();


        MainActivity.TestComeFromOutsideOrNot = 0;
        Username.setText(GlobalClass.manager.getUsername());
        Email.setText(GlobalClass.manager.getEmail());

        //Profile.setOnClickListener(new View.OnClickListener() {
        //    @Override
        //    public void onClick(View view) {
        //        Intent PersonalProfileIntent =
        //              new Intent(PersonalProfileManager.this,
        //                  PersonalProfileEdit.class);
        //        startActivity(PersonalProfileIntent);
        //    }
        //});



        TheAnnouncement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Enter the page of announcement
            }
        });


        MyGyms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Scarch for the backend database to search for
                //      the gym base on the manager Email;
                //If search it, then jump to the GymProfileManager Activity
                //Intent MyGymsIntent = new Intent
                //      (PersonalProfileManager.this, GymsProfileManager.class);
                //startActivity(MyGymsIntent);
                //Otherwise, jump to the NewGyms Activity
                //TODO FIX THIS WTFS
                if (true) {
                    Intent NewGymsIntent =
                            new Intent(PersonalProfileManager.this,
                                    NewGyms.class);
                    startActivity(NewGymsIntent);
                } else {
                    Intent NewGymsIntent =
                            new Intent(PersonalProfileManager.this,
                                    GymsProfileManager.class);
                    startActivity(NewGymsIntent);
                }

            }
        });
    }

    private void initWidgets() {
        Username = findViewById(R.id.Username);
        Email = findViewById(R.id.Email);
        TheAnnouncement = findViewById(R.id.Announcement);
        MyGyms = findViewById(R.id.MyGyms);
    }
}