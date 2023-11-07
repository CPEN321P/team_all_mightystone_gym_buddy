package com.example.cpen321tutorial1;

import static com.example.cpen321tutorial1.GlobalClass.myAccount;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.util.List;

public class Friends
        extends AppCompatActivity {

    Button FindNewFriends;

    Button Messages;

    Button Home;

    Button Friends;

    Button Schedule;

    Button Gyms;

    Button PersonalProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends);

        RecyclerView recyclerView = findViewById(R.id.recyclerview);

        //GET ALL FRIEND FROM BACKEND
        ConnectionToBackend c = new ConnectionToBackend();
        List<Account> items = c.getAllInList(myAccount.getUserId(), 0);

        recyclerView.setLayoutManager
                (new LinearLayoutManager(this));
        recyclerView.setAdapter
                (new PersonAdapter(getApplicationContext(), items));

        recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(Friends.this,
                        recyclerView,
                        new RecyclerItemClickListener.OnItemClickListener() {
                    @Override public void onItemClick(View view, int position) {

                        Intent FriendIntent =
                                new Intent(Friends.this,
                                        PersonalProfileFriend.class);

                        if(!items.isEmpty()){
                            Account Friend = items.get(position);
                            FriendIntent.putExtra
                                    ("FriendName", Friend.getUsername());
                            FriendIntent.putExtra
                                    ("FriendUserId", Friend.getUserId());
                            FriendIntent.putExtra
                                    ("FriendAge", Friend.getAge());
                            FriendIntent.putExtra
                                    ("FriendWeight", Friend.getWeight());
                            FriendIntent.putExtra
                                    ("FriendGender", Friend.getGender());
                        }


                        startActivity(FriendIntent);
                    }

                    @Override public void onLongItemClick
                            (View view, int position) {
                        // do whatever
                    }
                }));

        FindNewFriends = findViewById(R.id.find_new_friends);

        FindNewFriends.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent FindFriendsIntent =
                        new Intent(Friends.this, PossibleFriends.class);
                startActivity(FindFriendsIntent);
            }
        });

        Messages = findViewById(R.id.top_bar_messages);

        Messages.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent MessageIntent =
                        new Intent(Friends.this, Messages.class);
                startActivity(MessageIntent);
            }
        });

        Home = findViewById(R.id.navigation_home);

        Home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent HomeIntent =
                        new Intent(Friends.this, Logo.class);
                startActivity(HomeIntent);
            }
        });

        Friends = findViewById(R.id.navigation_friends);

        Friends.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent FriendsIntent =
                        new Intent(Friends.this, Friends.class);
                startActivity(FriendsIntent);
            }
        });

        Schedule = findViewById(R.id.navigation_schedule);

        Schedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent ScheduleIntent =
                        new Intent(Friends.this, MonthlySchedule.class);
                startActivity(ScheduleIntent);
            }
        });

        Gyms = findViewById(R.id.navigation_gyms);

        Gyms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent GymIntent =
                        new Intent(Friends.this, Gyms.class);
                startActivity(GymIntent);
            }
        });

        PersonalProfile = findViewById(R.id.navigation_profile);

        PersonalProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent ProfileIntent =
                        new Intent(Friends.this, PersonalProfileUsers.class);
                startActivity(ProfileIntent);
            }
        });


    }
}