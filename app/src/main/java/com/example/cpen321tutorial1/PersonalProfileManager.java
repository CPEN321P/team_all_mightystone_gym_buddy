package com.example.cpen321tutorial1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class PersonalProfileManager extends AppCompatActivity {

    private Button MyGyms;
    private TextView Username, Email, Age, Weight, Gender;
    private Button Profile, Schedule, TheAnnouncement;
    final static String TAG = "PersonalProfileManager";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_profile_manager);
        initWidgets();


        MainActivity.TestComeFromOutsideOrNot = 0;
        Username.setText(GlobalClass.myAccount.getUsername());
        Email.setText(GlobalClass.myAccount.getEmailAddress());
        Age.setText(Integer.toString(GlobalClass.myAccount.getAge()) + " years old");
        Weight.setText(Integer.toString(GlobalClass.myAccount.getWeight()) + " kg");
        Gender.setText(GlobalClass.myAccount.getGender());

        Profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent PersonalProfileIntent = new Intent(PersonalProfileManager.this, PersonalProfileEdit.class);
                startActivity(PersonalProfileIntent);
            }
        });

        Schedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity.TestComeFromOutsideOrNot = 1;
                Intent ScheduleIntent = new Intent(PersonalProfileManager.this, MonthlySchedule.class);
                startActivity(ScheduleIntent);
            }
        });

        TheAnnouncement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Enter the page of announcement
            }
        });


        MyGyms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String ManagerEmail;
                //Scarch for the backend database to search for the gym base on the manager Email;
                //If search it, then jump to the GymProfileManager Activity
                //Intent MyGymsIntent = new Intent(PersonalProfileManager.this, GymsProfileManager.class);
                //startActivity(MyGymsIntent);
                //Otherwise, jump to the NewGyms Activity
                if (Gym.CurrentGym.isEmpty()) {
                    Intent NewGymsIntent = new Intent(PersonalProfileManager.this, NewGyms.class);
                    startActivity(NewGymsIntent);
                } else {
                    Intent NewGymsIntent = new Intent(PersonalProfileManager.this, GymsProfileManager.class);
                    startActivity(NewGymsIntent);
                }

            }
        });
    }

    private void initWidgets() {
        Username = findViewById(R.id.Username);
        Email = findViewById(R.id.Email);
        Age = findViewById(R.id.Age);
        Weight = findViewById(R.id.Weight);
        Gender = findViewById(R.id.Gender);
        Profile = findViewById(R.id.EditPersonalProfile);
        Schedule = findViewById(R.id.EditSchedule);
        TheAnnouncement = findViewById(R.id.Announcement);
        MyGyms = findViewById(R.id.MyGyms);
    }
}