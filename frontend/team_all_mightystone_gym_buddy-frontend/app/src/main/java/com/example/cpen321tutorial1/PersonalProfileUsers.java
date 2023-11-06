package com.example.cpen321tutorial1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class PersonalProfileUsers extends AppCompatActivity {

    Button FriendsList;

    Button GymList;

    Button BlockList;

    Button Profile;

    Button Schedule;

    TextView Username;

    TextView Email;

    TextView Age;

    TextView Weight;

    TextView Gender;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_profile);
        initWidgets();

        MainActivity.TestComeFromOutsideOrNot = 0;
        Username.setText(GlobalClass.myAccount.getUsername());
        Email.setText(GlobalClass.myAccount.getEmailAddress());
        Age.setText(Integer.toString(GlobalClass.myAccount.getAge()) + " years old");
        Weight.setText(Integer.toString(GlobalClass.myAccount.getWeight()) + " kg");
        Gender.setText(GlobalClass.myAccount.getGender());

        FriendsList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent FriendsIntent = new Intent(PersonalProfileUsers.this, Friends.class);
                startActivity(FriendsIntent);
            }
        });

        GymList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent GymsIntent = new Intent(PersonalProfileUsers.this, Gyms.class);
                startActivity(GymsIntent);
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
                Intent BlockIntent = new Intent(PersonalProfileUsers.this, BlockedUsers.class);
                startActivity(BlockIntent);
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