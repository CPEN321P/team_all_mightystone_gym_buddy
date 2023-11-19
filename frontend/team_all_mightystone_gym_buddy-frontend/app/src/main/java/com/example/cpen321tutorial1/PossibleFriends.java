package com.example.cpen321tutorial1;

import static com.example.cpen321tutorial1.GlobalClass.myAccount;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;

public class PossibleFriends
        extends AppCompatActivity {

    Button MyFriends;

    Button Messages;

    Button Home;

    Button Friends;

    Button Schedule;

    Button Gyms;

    Button PersonalProfile;

    final static String TAG = "PossibleFriends";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_possible_friends);



        RecyclerView recyclerView = findViewById(R.id.recyclerview);
        //RecyclerView recyclerView2 = findViewById(R.id.recyclerview2);


        //get all possible friends from backend
        ConnectionToBackend c = new ConnectionToBackend();
        List<Account> items;
        if (GlobalClass.TestTempPeopleList == 0){
            items = c.getAllInList(myAccount.getUserId(), 2);
        }
        else{
            items = GlobalClass.TempPeopleList;
        }

        ArrayList<Account> Frienditems = myAccount.getFriendsList();

        recyclerView.setLayoutManager
                (new LinearLayoutManager(this));
        recyclerView.setAdapter
                (new PersonAdapter(getApplicationContext(), items));

        recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(PossibleFriends.this,
                        recyclerView ,
                        new RecyclerItemClickListener.OnItemClickListener() {
                    @Override public void onItemClick(View view, int position) {

                        Intent FriendIntent = new Intent(PossibleFriends.this, PersonalProfileOthers.class);
                        if(!items.isEmpty()){
                            Account posFriend = items.get(position);
                            for (int i = 0; i < Frienditems.size(); i++){
                                Log.d(TAG, "Frienditems Id: " + Frienditems.get(i).getUserId());
                                Log.d(TAG, "items Id: " + posFriend.getUserId());
                                if (posFriend.getUserId().equals(Frienditems.get(i).getUserId())){
                                    FriendIntent = new Intent(PossibleFriends.this, PersonalProfileFriend.class);
                                }
                            }

                            Log.d(TAG, "Test111");
                            FriendIntent.putExtra("Name",
                                    posFriend.getUsername());
                            FriendIntent.putExtra("UserId",
                                    posFriend.getUserId());
                            FriendIntent.putExtra("Age",
                                    posFriend.getAge());
                            FriendIntent.putExtra("Weight",
                                    posFriend.getWeight());
                            FriendIntent.putExtra("Gender",
                                    posFriend.getGender());
                        }

                        startActivity(FriendIntent);
                    }

                    @Override public void onLongItemClick
                            (View view, int position) {
                        // do whatever
                    }
                }));


        MyFriends = findViewById(R.id.my_friends);

        MyFriends.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent MyFriendsIntent =
                        new Intent(PossibleFriends.this, Friends.class);
                startActivity(MyFriendsIntent);
            }
        });

        Messages = findViewById(R.id.top_bar_messages);

        Messages.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent MessageIntent =
                        new Intent(PossibleFriends.this, Messages.class);
                startActivity(MessageIntent);
            }
        });

        Home = findViewById(R.id.navigation_home);

        Home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent HomeIntent =
                        new Intent(PossibleFriends.this, Logo.class);
                startActivity(HomeIntent);
            }
        });

        Friends = findViewById(R.id.navigation_friends);

        Friends.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent FriendsIntent =
                        new Intent(PossibleFriends.this, Friends.class);
                startActivity(FriendsIntent);
            }
        });

        Schedule = findViewById(R.id.navigation_schedule);

        Schedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent ScheduleIntent =
                        new Intent(PossibleFriends.this, ScheduleMonthly.class);
                startActivity(ScheduleIntent);
            }
        });

        Gyms = findViewById(R.id.navigation_gyms);

        Gyms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent GymIntent =
                        new Intent(PossibleFriends.this, Gyms.class);
                startActivity(GymIntent);
            }
        });

        PersonalProfile = findViewById(R.id.navigation_profile);

        PersonalProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent ProfileIntent =
                        new Intent(PossibleFriends.this, PersonalProfileUsers.class);
                startActivity(ProfileIntent);
            }
        });


    }
}