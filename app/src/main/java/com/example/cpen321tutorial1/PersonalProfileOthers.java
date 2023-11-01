package com.example.cpen321tutorial1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class PersonalProfileOthers extends AppCompatActivity {

    Button AddFriend, Block;
    TextView Username, Email, Age, Weight, Gender;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_profile_others);
        initWidgets();

        ArrayList<Account> MyCurrentBlockList = Account.CurrentAccount.get(0).getBlockList();
        //ArrayList<Account> OtherCurrentBlockList = /The Account that you get//.getBlockList();

        /*
        for (int i = 0; i < MyCurrentBlockList.size(); i++){
            if (MyCurrentBlockList.get(i).equals( //The Account that you get// )){
                Toast.makeText(PersonalProfileOthers.this, "In Block List!", Toast.LENGTH_SHORT).show();
                finish();
                // }
        }
         */

        /*
        for (int i = 0; i < OtherCurrentBlockList.size(); i++){
            if (OtherCurrentBlockList.get(i).equals(Account.CurrentAccount.get(0))){
                Toast.makeText(PersonalProfileOthers.this, "In Block List!", Toast.LENGTH_SHORT).show();
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

        AddFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Send the friend request
                //get the Account class of this person
                ArrayList<Account> CurrentFriendList = Account.CurrentAccount.get(0).getFriendsList();
                //CurrentFriendList.add( //The Account information of this person // );
                Account.CurrentAccount.get(0).setFriendsList(CurrentFriendList);
                //POST the account to the database

                //ArrayList<Account> CurrentFriendListOthers = //The Account that you get//.getFriendsList();
                //CurrentFriendListOthers.add(Account.CurrentAccount.get(0));
                ////The Account that you get//.setFriendsList(CurrentFriendListOthers);
                //POST the account to the database

                Toast.makeText(PersonalProfileOthers.this, "Make a friend request!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(PersonalProfileOthers.this, PersonalProfileFriend.class);
                startActivity(intent);
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
                Toast.makeText(PersonalProfileOthers.this, "Add the user in block list", Toast.LENGTH_SHORT).show();

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