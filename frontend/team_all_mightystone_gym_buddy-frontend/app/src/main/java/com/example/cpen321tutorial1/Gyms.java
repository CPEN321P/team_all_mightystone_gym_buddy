package com.example.cpen321tutorial1;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class Gyms extends AppCompatActivity {

    private Button Home;
    private Button Friends;
    private Button Schedule;
    private Button Gyms;
    private Button PersonalProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gyms);

        RecyclerView recyclerView = findViewById(R.id.recyclerview);

        //get all gyms from backend
        ConnectionToBackend c = new ConnectionToBackend();
        List<Gym> items = c.getAllGyms();


        recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(Gyms.this, recyclerView,
                        new RecyclerItemClickListener.OnItemClickListener() {
                    @Override public void onItemClick(View view, int position) {
                        Intent GymIntent = new Intent(Gyms.this, GymProfile.class);
                        if(!items.isEmpty()){
                            Gym Gym = items.get(position);
                            GymIntent.putExtra("GymAddress", Gym.getAddress());
                            GymIntent.putExtra("GymName", Gym.getName());
                        }

                        startActivity(GymIntent);
                    }

                    @Override public void onLongItemClick(View view, int position) {
                        // do whatever
                    }
                }));

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new GymAdapter(getApplicationContext(), items));

        Home = findViewById(R.id.navigation_home);

        Home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent HomeIntent = new Intent(Gyms.this, Logo.class);
                startActivity(HomeIntent);
            }
        });

        Friends = findViewById(R.id.navigation_friends);

        Friends.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent FriendsIntent =
                        new Intent(Gyms.this, Friends.class);
                startActivity(FriendsIntent);
            }
        });

        Schedule = findViewById(R.id.navigation_schedule);

        Schedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent ScheduleIntent =
                        new Intent(Gyms.this, MonthlySchedule.class);
                startActivity(ScheduleIntent);
            }
        });

        Gyms = findViewById(R.id.navigation_gyms);

        Gyms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent GymIntent =
                        new Intent(Gyms.this, Gyms.class);
                startActivity(GymIntent);
            }
        });

        PersonalProfile = findViewById(R.id.navigation_profile);

        PersonalProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent ProfileIntent =
                        new Intent(Gyms.this, PersonalProfileUsers.class);
                startActivity(ProfileIntent);
            }
        });


    }
}