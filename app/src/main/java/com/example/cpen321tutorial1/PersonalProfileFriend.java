package com.example.cpen321tutorial1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

public class PersonalProfileFriend extends AppCompatActivity {

    Button Schedule, Message;
    TextView Username, Email, Age, Weight, Gender;
    public static ArrayList<Event> FriendsEvent = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_profile_friend);
        initWidgets();

        //Get the account information of this user
        //Username.setText();
        //Email.setText();
        //Age.setText();
        //Weight.setText();
        //Gender.setText();

        FriendsEvent.clear();
        //Get the event list from database of this user, and upload it into FriendsEvent

        Schedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity.TestComeFromOutsideOrNot = 1;
                Intent ScheduleIntent = new Intent(PersonalProfileFriend.this, ScheduleFriendsMonthly.class);
                startActivity(ScheduleIntent);
            }
        });

        Message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Go to the page which send the message to friends
            }
        });
    }

    private void initWidgets() {
        Schedule = findViewById(R.id.Schedule);
        Message = findViewById(R.id.Message);
        Username = findViewById(R.id.Username);
        Email = findViewById(R.id.Email);
        Age = findViewById(R.id.Age);
        Weight = findViewById(R.id.Weight);
        Gender = findViewById(R.id.Gender);
    }
}