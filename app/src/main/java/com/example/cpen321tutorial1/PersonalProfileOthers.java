package com.example.cpen321tutorial1;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class PersonalProfileOthers extends AppCompatActivity {

    Button AddFriend;
    TextView Username, Email, Age, Weight, Gender;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_profile_others);
        initWidgets();

        //Get the account information of this user
        //Username.setText();
        //Email.setText();
        //Age.setText();
        //Weight.setText();
        //Gender.setText();

        AddFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Send the friend request
            }
        });
    }

    private void initWidgets() {
        Username = findViewById(R.id.Username);
        Email = findViewById(R.id.Email);
        Age = findViewById(R.id.Age);
        Weight = findViewById(R.id.Weight);
        Gender = findViewById(R.id.Gender);
        AddFriend = findViewById(R.id.AddFriend);
    }
}