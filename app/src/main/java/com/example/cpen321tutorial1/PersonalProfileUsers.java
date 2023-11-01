package com.example.cpen321tutorial1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class PersonalProfileUsers extends AppCompatActivity {

    Button FriendsList, GymList, BlockList;
    TextView Username, Email, Age, Weight, Gender;
    Button Profile, Schedule;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_profile);
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

        GymList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Enter the list of subscribed gyms
            }
        });

        Profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent PersonalProfileIntent = new Intent(PersonalProfileUsers.this, PersonalProfileEdit.class);
                startActivity(PersonalProfileIntent);
            }
        });

        Schedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity.TestComeFromOutsideOrNot = 1;
                Intent ScheduleIntent = new Intent(PersonalProfileUsers.this, MonthlySchedule.class);
                startActivity(ScheduleIntent);
            }
        });

        BlockList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Intent ScheduleIntent = new Intent(PersonalProfileUsers.this, MonthlySchedule.class);
                //startActivity(ScheduleIntent);
            }
        });
    }

    private void initWidgets() {
        FriendsList = findViewById(R.id.Friends);
        GymList = findViewById(R.id.Gyms);
        Username = findViewById(R.id.Username);
        Email = findViewById(R.id.Email);
        Age = findViewById(R.id.Age);
        Weight = findViewById(R.id.Weight);
        Gender = findViewById(R.id.Gender);
        Profile = findViewById(R.id.EditPersonalProfile);
        Schedule = findViewById(R.id.EditSchedule);
        BlockList = findViewById(R.id.BlockList);
    }
}