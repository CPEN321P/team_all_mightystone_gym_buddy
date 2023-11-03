package com.example.cpen321tutorial1;

import static com.example.cpen321tutorial1.GlobalClass.myAccount;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;

public class PossibleFriends extends AppCompatActivity {

    private Button MyFriends;
    private Button Messages;
    private Button Home;
    private Button Friends;
    private Button Schedule;
    private Button Gyms;
    private Button PersonalProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_possible_friends);



        RecyclerView recyclerView = findViewById(R.id.recyclerview);

        //get all possible friends from backend
        ConnectionToBackend c = new ConnectionToBackend();
        List<Account> items = c.getRecommendedUsers(myAccount.getUserId());

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new PersonAdapter(getApplicationContext(), items));

        recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(PossibleFriends.this, recyclerView ,new RecyclerItemClickListener.OnItemClickListener() {
                    @Override public void onItemClick(View view, int position) {
                        Intent FriendIntent = new Intent(PossibleFriends.this, PersonalProfileOthers.class);
                        startActivity(FriendIntent);
                    }

                    @Override public void onLongItemClick(View view, int position) {
                        // do whatever
                    }
                }));


        MyFriends = findViewById(R.id.my_friends);

        MyFriends.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent MyFriendsIntent = new Intent(PossibleFriends.this, Friends.class);
                startActivity(MyFriendsIntent);
            }
        });

        Messages = findViewById(R.id.top_bar_messages);

        Messages.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent MessageIntent = new Intent(PossibleFriends.this, Messages.class);
                startActivity(MessageIntent);
            }
        });

        Home = findViewById(R.id.navigation_home);

        Home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent HomeIntent = new Intent(PossibleFriends.this, Logo.class);
                startActivity(HomeIntent);
            }
        });

        Friends = findViewById(R.id.navigation_friends);

        Friends.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent FriendsIntent = new Intent(PossibleFriends.this, Friends.class);
                startActivity(FriendsIntent);
            }
        });

        Schedule = findViewById(R.id.navigation_schedule);

        Schedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent ScheduleIntent = new Intent(PossibleFriends.this, MonthlySchedule.class);
                startActivity(ScheduleIntent);
            }
        });

        Gyms = findViewById(R.id.navigation_gyms);

        Gyms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent GymIntent = new Intent(PossibleFriends.this, Gyms.class);
                startActivity(GymIntent);
            }
        });

        PersonalProfile = findViewById(R.id.navigation_profile);

        PersonalProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent ProfileIntent = new Intent(PossibleFriends.this, PersonalProfileUsers.class);
                startActivity(ProfileIntent);
            }
        });


    }
}