package com.example.cpen321tutorial1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class PersonalProfileManager extends AppCompatActivity {

    Button FriendsList;
    TextView Username, Email, Age, Weight, Gender;
    Button Profile, Schedule, TheAnnouncement;
    final static String TAG = "PersonalProfileManager";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_profile_manager);
        initWidgets();

        MainActivity.TestComeFromOutsideOrNot = 0;
        Username.setText(Account.CurrentAccount.get(0).getUsername());
        Email.setText(Account.CurrentAccount.get(0).getEmailAddress());
        Age.setText(Integer.toString(Account.CurrentAccount.get(0).getAge()) + " years old");
        Weight.setText(Integer.toString(Account.CurrentAccount.get(0).getWeight()) + " kg");
        Gender.setText(Account.CurrentAccount.get(0).getGender());

        FriendsList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Enter the list of friends
            }
        });
        Log.d(TAG, "Test1");

        Profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent PersonalProfileIntent = new Intent(PersonalProfileManager.this, PersonalProfileEdit.class);
                startActivity(PersonalProfileIntent);
            }
        });
        Log.d(TAG, "Test3");

        Schedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity.TestComeFromOutsideOrNot = 1;
                Intent ScheduleIntent = new Intent(PersonalProfileManager.this, MonthlySchedule.class);
                startActivity(ScheduleIntent);
            }
        });
        Log.d(TAG, "Test4");

        TheAnnouncement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Enter the page of announcement
            }
        });
        Log.d(TAG, "Test2");

    }

    private void initWidgets() {
        FriendsList = findViewById(R.id.Friends);
        Username = findViewById(R.id.Username);
        Email = findViewById(R.id.Email);
        Age = findViewById(R.id.Age);
        Weight = findViewById(R.id.Weight);
        Gender = findViewById(R.id.Gender);
        Profile = findViewById(R.id.EditPersonalProfile);
        Schedule = findViewById(R.id.EditSchedule);
        TheAnnouncement = findViewById(R.id.Announcement);
    }
}