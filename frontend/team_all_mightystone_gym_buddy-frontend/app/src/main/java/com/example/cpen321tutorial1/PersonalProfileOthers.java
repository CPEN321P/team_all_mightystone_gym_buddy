package com.example.cpen321tutorial1;

import static com.example.cpen321tutorial1.Account.GetAccountInfromation;
import static com.example.cpen321tutorial1.GlobalClass.MyeventsList;
import static com.example.cpen321tutorial1.GlobalClass.client;
import static com.example.cpen321tutorial1.GlobalClass.myAccount;
import static com.example.cpen321tutorial1.JsonFunctions.NewCallPost;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;

public class PersonalProfileOthers
        extends AppCompatActivity {

    Button AddFriend;

    Button Block;

    TextView Username;

    TextView Email;

    TextView Age;

    TextView Weight;

    TextView Gender;

    //dashboard buttons
    Button Home;

    Button Friends;

    Button Schedule;

    Button Gyms;

    Button PersonalProfile;

    final static String TAG = "PersonalProfileOthers";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_profile_others);
        initWidgets();
        Intent i = getIntent();

        String posFriendName = i.getStringExtra("Name");
        String posFriendId = i.getStringExtra("UserId");
        String posFriendAge = i.getStringExtra("Age");
        String posFriendWeight = i.getStringExtra("Weight");
        String posFriendGender = i.getStringExtra("Gender");

        Account TheAccount = GetAccountInfromation(posFriendId);

        Username.setText(TheAccount.getUsername());
        Email.setText("Only Able To Seem By Friend");
        Age.setText("Only Able To Seem By Friend");
        Weight.setText("Only Able To Seem By Friend");
        Gender.setText(TheAccount.getGender());

        //ArrayList<Account> MyCurrentBlockList = GlobalClass.myAccount.getBlockList();
        //ArrayList<Account> OtherCurrentBlockList = /The Account that you get//.getBlockList();

        /*
        for (int i = 0; i < MyCurrentBlockList.size(); i++){
            if (MyCurrentBlockList.get(i).equals( //The Account that you get// )){
                Toast.makeText(PersonalProfileOthers.this,
                    "In Block List!",
                    Toast.LENGTH_SHORT).show();
                finish();
                // }
        }
         */

        /*
        for (int i = 0; i < OtherCurrentBlockList.size(); i++){
            if (OtherCurrentBlockList.get(i).equals(GlobalClass.myAccount)){

                Toast.makeText(PersonalProfileOthers.this,
                    "In Block List!",
                    Toast.LENGTH_SHORT).show();

                finish();
                // }
        }
         */

        //Get the account information of this user
        //Username.setText();
        //Email.setText();
        //Age.setText();
        //Weight.setText();
        //Gender.setText();

        Home = findViewById(R.id.navigation_home);

        Home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent HomeIntent =
                        new Intent(PersonalProfileOthers.this, Logo.class);
                startActivity(HomeIntent);
            }
        });

        Friends = findViewById(R.id.navigation_friends);

        Friends.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent FriendsIntent =
                        new Intent(PersonalProfileOthers.this, Friends.class);
                startActivity(FriendsIntent);
            }
        });

        Schedule = findViewById(R.id.navigation_schedule);

        Schedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent ScheduleIntent =
                        new Intent(PersonalProfileOthers.this, ScheduleMonthly.class);
                startActivity(ScheduleIntent);
            }
        });

        Gyms = findViewById(R.id.navigation_gyms);

        Gyms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent GymIntent =
                        new Intent(PersonalProfileOthers.this, Gyms.class);
                startActivity(GymIntent);
            }
        });

        PersonalProfile = findViewById(R.id.navigation_profile);

        PersonalProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent ProfileIntent =
                        new Intent(PersonalProfileOthers.this, PersonalProfileUsers.class);
                startActivity(ProfileIntent);
            }
        });

        AddFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Send the friend request
                //get the Account class of this person
                ArrayList<Account> CurrentFriendList =
                        GlobalClass.myAccount.getFriendsList();
                CurrentFriendList.add(TheAccount);
                GlobalClass.myAccount.setFriendsList(CurrentFriendList);
                //POST the account to the database

                //ArrayList<Account> CurrentFriendListOthers =
                //    Account that you get//.getFriendsList();
                //CurrentFriendListOthers.add(GlobalClass.myAccount);
                ////Account that you get//.setFriendsList(CurrentFriendListOthers);
                //POST the account to the database

                RequestBody body = RequestBody.create("{"+ "}",
                        MediaType.parse("application/json"));

                Request addFriend = new Request.Builder()
                        .url("https://20.172.9.70/users/addFriend/"
                                + myAccount.getUserId()
                                + "/" + posFriendId)
                        .put(body)
                        .build();

                NewCallPost(client, addFriend);

                Toast.makeText(PersonalProfileOthers.this,
                        "You made a new friend!",
                        Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(PersonalProfileOthers.this,
                                PossibleFriends.class);

                startActivity(intent);
            }
        });

        Block.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Block the user
                //Get the account information from the database
                ArrayList<Account> BlockAccounts =
                        GlobalClass.myAccount.getBlockList();
                BlockAccounts.add(TheAccount);
                GlobalClass.myAccount.setBlockList(BlockAccounts);

                RequestBody body = RequestBody.create("{"+ "}",
                        MediaType.parse("application/json"));
                Request blockUser = new Request.Builder()
                        .url("https://20.172.9.70/users/blockUser/" +
                                myAccount.getUserId() + "/" +
                                posFriendId)
                        .put(body)
                        .build();
                NewCallPost(client, blockUser);
                //POST the account to the database
                Toast.makeText(PersonalProfileOthers.this,
                        "Add the user in block list",
                        Toast.LENGTH_SHORT).show();

                finish();
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
        Block = findViewById(R.id.Block);
    }
}