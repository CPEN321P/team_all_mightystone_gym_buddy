package com.example.cpen321tutorial1;

import static com.example.cpen321tutorial1.Account.GetAccountInfromation;
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

public class PersonalProfileFriend
        extends AppCompatActivity {

    Button FriendSchedule;

    Button Message;

    Button Block;

    Button Cancel;

    TextView Username;

    TextView Email;

    TextView Age;

    TextView Weight;

    TextView Gym;

    TextView Gender;
    //dashboard buttons
    Button Home;

    Button Friends;

    Button Schedule;

    Button Gyms;

    Button PersonalProfile;


    final static String TAG = "PersonalProfileFriends";

    public static ArrayList<Event> FriendsEvent = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_profile_friend);
        initWidgets();

        Intent i = getIntent();

        Log.d(TAG, "Test111");

        String friendName = i.getStringExtra("Name");
        String friendId = i.getStringExtra("UserId");
        String friendAge = i.getStringExtra("Age");
        String friendWeight = i.getStringExtra("Weight");
        String friendGender = i.getStringExtra("Gender");

        Log.d(TAG, "Test222");

        Account TheAccount = GetAccountInfromation(friendId);
        Log.d(TAG, friendId);

        Username.setText(TheAccount.getUsername());
        Email.setText(TheAccount.getEmailAddress());
        Age.setText(Integer.toString(TheAccount.getAge()) + " Years Old");
        Weight.setText(Integer.toString(TheAccount.getWeight()) + " kg");
        Gender.setText(TheAccount.getGender());
        Gym.setText(TheAccount.getMyGym().getName());

        FriendsEvent.clear();

        ConnectionToBackend c = new ConnectionToBackend();

        ArrayList<Event> TheEventsofFriendsAccount =
                c.getScheduleByUser(friendId);

        if(TheEventsofFriendsAccount != null){
            FriendsEvent = TheEventsofFriendsAccount;
        }
        //Get the event list from database of this user,
        // and upload it into FriendsEvent
        /*

        Home = findViewById(R.id.navigation_home);

        Home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent HomeIntent =
                        new Intent(PersonalProfileFriend.this, Logo.class);
                startActivity(HomeIntent);
            }
        });

        Friends = findViewById(R.id.navigation_friends);

        Friends.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent FriendsIntent =
                        new Intent(PersonalProfileFriend.this, Friends.class);
                startActivity(FriendsIntent);
            }
        });

        Schedule = findViewById(R.id.navigation_schedule);

        Schedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent ScheduleIntent =
                        new Intent(PersonalProfileFriend.this, ScheduleMonthly.class);
                startActivity(ScheduleIntent);
            }
        });

        Gyms = findViewById(R.id.navigation_gyms);

        Gyms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent GymIntent =
                        new Intent(PersonalProfileFriend.this, Gyms.class);
                startActivity(GymIntent);
            }
        });

        PersonalProfile = findViewById(R.id.navigation_profile);

        PersonalProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent ProfileIntent =
                        new Intent(PersonalProfileFriend.this, PersonalProfileUsers.class);
                startActivity(ProfileIntent);
            }
        });

         */

        FriendSchedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity.TestComeFromOutsideOrNot = 1;
                Intent ScheduleIntent =
                        new Intent(PersonalProfileFriend.this,
                                ScheduleFriendsMonthly.class);
                startActivity(ScheduleIntent);
            }
        });

        Message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent chatIntent =
                        new Intent(PersonalProfileFriend.this,Chat.class);
                chatIntent.putExtra("Username", friendName);
                startActivity(chatIntent);
                //Go to the page which send the message to friends
            }
        });

        Block.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Block the user
                //Get the account information from the database
                ArrayList<Account> BlockAccounts =
                        GlobalClass.myAccount.getBlockList();

                RequestBody body = RequestBody.create("{"+ "}",
                        MediaType.parse("application/json"));

                Request blockUser = new Request.Builder()
                        .url("https://20.172.9.70/users/blockUser/"
                                + myAccount.getUserId()
                                + "/" + friendId)
                        .put(body)
                        .build();

                NewCallPost(client, blockUser);
                BlockAccounts.add(TheAccount);
                GlobalClass.myAccount.setBlockList(BlockAccounts);
                //POST the account to the database

                Toast.makeText(PersonalProfileFriend.this,
                        "Add the user in block list",
                        Toast.LENGTH_SHORT).show();

                finish();
            }
        });



        Cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent CancelIntent = new Intent
                        (PersonalProfileFriend.this, Friends.class);
                startActivity(CancelIntent);
            }
        });

    }

    private void initWidgets() {
        FriendSchedule = findViewById(R.id.FriendSchedule);
        Message = findViewById(R.id.Message);
        Username = findViewById(R.id.Username);
        Email = findViewById(R.id.Email);
        Age = findViewById(R.id.Age);
        Weight = findViewById(R.id.Weight);
        Gender = findViewById(R.id.Gender);
        Block = findViewById(R.id.Block);
        Gym = findViewById(R.id.Gym);
        Cancel = findViewById(R.id.Cancel);
    }
}