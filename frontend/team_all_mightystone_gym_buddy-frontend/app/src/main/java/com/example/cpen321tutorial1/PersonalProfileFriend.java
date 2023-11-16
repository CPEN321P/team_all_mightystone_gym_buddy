package com.example.cpen321tutorial1;

import static com.example.cpen321tutorial1.GlobalClass.client;
import static com.example.cpen321tutorial1.GlobalClass.myAccount;
import static com.example.cpen321tutorial1.JsonFunctions.NewCallPost;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
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

    Button Schedule;

    Button Message;

    Button Block;

    Button Cancel;

    TextView Username;

    TextView Email;

    TextView Age;

    TextView Weight;

    TextView Gender;

    public static ArrayList<Event> FriendsEvent = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_profile_friend);
        initWidgets();

        Intent i = getIntent();

        String friendName = i.getStringExtra("FriendName");
        String friendId = i.getStringExtra("FriendUserId");
        String friendAge = i.getStringExtra("FriendAge");
        String friendWeight = i.getStringExtra("FriendWeight");
        String friendGender = i.getStringExtra("FriendGender");

        Username.setText(friendName);
        Email.setText(friendId);
        Age.setText(friendAge);
        Weight.setText(friendWeight);
        Gender.setText(friendGender);

        FriendsEvent.clear();

        ConnectionToBackend c = new ConnectionToBackend();

        ArrayList<Event> TheEventsofFriendsAccount =
                c.getScheduleByUser(friendId);

        if(TheEventsofFriendsAccount != null){
            FriendsEvent = TheEventsofFriendsAccount;
        }
        //Get the event list from database of this user,
        // and upload it into FriendsEvent


        Schedule.setOnClickListener(new View.OnClickListener() {
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
                //BlockAccounts.add(//the account information from the database//);
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
                finish();
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
        Block = findViewById(R.id.Block);
        Cancel = findViewById(R.id.Cancel);
    }
}