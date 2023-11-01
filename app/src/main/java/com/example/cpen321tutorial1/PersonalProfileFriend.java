package com.example.cpen321tutorial1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class PersonalProfileFriend extends AppCompatActivity {

    Button Schedule, Message, Block;
    TextView Username, Email, Age, Weight, Gender;
    public static ArrayList<Event> FriendsEvent = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_profile_friend);
        initWidgets();

        ArrayList<Account> MyCurrentBlockList = Account.CurrentAccount.get(0).getBlockList();
        //ArrayList<Account> OtherCurrentBlockList = //The Account that you get//.getBlockList();

        /*
        for (int i = 0; i < MyCurrentBlockList.size(); i++){
            if (MyCurrentBlockList.get(i).equals( //The Account that you get// )){
                Toast.makeText(PersonalProfileFriends.this, "In Block List!", Toast.LENGTH_SHORT).show();
                finish();
                // }
        }
         */

        /*
        for (int i = 0; i < OtherCurrentBlockList.size(); i++){
            if (OtherCurrentBlockList.get(i).equals(Account.CurrentAccount.get(0))){
                Toast.makeText(PersonalProfileFriends.this, "In Block List!", Toast.LENGTH_SHORT).show();
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

        Block.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Block the user
                //Get the account information from the database
                ArrayList<Account> BlockAccounts = Account.CurrentAccount.get(0).getBlockList();
                //BlockAccounts.add(//the account information from the database//);
                Account.CurrentAccount.get(0).setBlockList(BlockAccounts);
                //POST the account to the database
                Toast.makeText(PersonalProfileFriend.this, "Add the user in block list", Toast.LENGTH_SHORT).show();

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
    }
}