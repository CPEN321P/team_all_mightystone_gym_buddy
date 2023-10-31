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

    Button AddFriend;
    TextView Username, Email, Age, Weight, Gender;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_profile_others);
        initWidgets();

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

                //ArrayList<Account> CurrentFriendListOthers = //The Account that you get//.getFriendsList();
                //CurrentFriendListOthers.add(Account.CurrentAccount.get(0));
                ////The Account that you get//.setFriendsList(CurrentFriendListOthers);

                Toast.makeText(PersonalProfileOthers.this, "Make a friend request!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(PersonalProfileOthers.this, PersonalProfileFriend.class);
                startActivity(intent);
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
    }
}