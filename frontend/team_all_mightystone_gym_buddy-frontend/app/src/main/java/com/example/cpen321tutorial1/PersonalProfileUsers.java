package com.example.cpen321tutorial1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class PersonalProfileUsers
        extends AppCompatActivity {

    Button BlockList;

    Button Profile;
    Button LogOut;

    TextView Username;

    TextView Email;

    TextView Age;

    TextView Weight;

    TextView Gender;

    TextView Gym;

    //dashboard buttons
    Button Home;

    Button Friends;

    Button Schedule;

    Button Gyms;

    Button PersonalProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_profile);
        initWidgets();

        MainActivity.TestComeFromOutsideOrNot = 0;
        Username.setText(GlobalClass.myAccount.getUsername());
        Email.setText(GlobalClass.myAccount.getEmailAddress());
        Age.setText(Integer.toString(GlobalClass.myAccount.getAge()) +
                " years old");
        Weight.setText(Integer.toString(GlobalClass.myAccount.getWeight()) +
                " kg");
        Gender.setText(GlobalClass.myAccount.getGender());

        Home = findViewById(R.id.navigation_home);

        Home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent HomeIntent =
                        new Intent(PersonalProfileUsers.this, Logo.class);
                startActivity(HomeIntent);
            }
        });

        Friends = findViewById(R.id.navigation_friends);

        Friends.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent FriendsIntent =
                        new Intent(PersonalProfileUsers.this, Friends.class);
                startActivity(FriendsIntent);
            }
        });

        Schedule = findViewById(R.id.navigation_schedule);

        Schedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent ScheduleIntent =
                        new Intent(PersonalProfileUsers.this, ScheduleMonthly.class);
                startActivity(ScheduleIntent);
            }
        });

        Gyms = findViewById(R.id.navigation_gyms);

        Gyms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent GymIntent =
                        new Intent(PersonalProfileUsers.this, Gyms.class);
                startActivity(GymIntent);
            }
        });

        PersonalProfile = findViewById(R.id.navigation_profile);

        PersonalProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent ProfileIntent =
                        new Intent(PersonalProfileUsers.this, PersonalProfileUsers.class);
                startActivity(ProfileIntent);
            }
        });

        if(Gym != null){
            Gym.setText(GlobalClass.myAccount.getMyGym().getName());

        } else {
            Gym.setText("NO GYM YET");
        }



        Profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent PersonalProfileIntent =
                        new Intent(PersonalProfileUsers.this, PersonalProfileEdit.class);
                startActivity(PersonalProfileIntent);
            }
        });

        LogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent LogOutIntent =
                        new Intent(PersonalProfileUsers.this, MainActivity.class);
                startActivity(LogOutIntent);
            }
        });


        BlockList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent BlockIntent =
                        new Intent(PersonalProfileUsers.this, BlockedUsers.class);

                startActivity(BlockIntent);
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
        Gym = findViewById(R.id.Gym);
        LogOut = findViewById(R.id.LogOut);
        BlockList = findViewById(R.id.BlockList);
    }
}