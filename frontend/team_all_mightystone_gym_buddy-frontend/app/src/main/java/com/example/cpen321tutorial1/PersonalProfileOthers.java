package com.example.cpen321tutorial1;

import static com.example.cpen321tutorial1.GlobalClass.client;
import static com.example.cpen321tutorial1.GlobalClass.myAccount;
import static com.example.cpen321tutorial1.JsonFunctions.JsonHomeGym;
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

public class PersonalProfileOthers extends AppCompatActivity {

    Button AddFriend;

    Button Block;

    TextView Username;

    TextView Email;

    TextView Age;

    TextView Weight;

    TextView Gender;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_profile_others);
        initWidgets();
        Intent i = getIntent();

        String posFriendName = i.getStringExtra("posFriendName");
        String posFriendId = i.getStringExtra("posFriendUserId");
        String posFriendAge = i.getStringExtra("posFriendAge");
        String posFriendWeight = i.getStringExtra("posFriendWeight");
        String posFriendGender = i.getStringExtra("posFriendGender");

        Username.setText(posFriendName);
        Age.setText(posFriendAge);
        Weight.setText(posFriendWeight);
        Gender.setText(posFriendGender);
        ArrayList<Account> MyCurrentBlockList = GlobalClass.myAccount.getBlockList();
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
            if (OtherCurrentBlockList.get(i).equals(GlobalClass.myAccount)){
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
                ArrayList<Account> CurrentFriendList = GlobalClass.myAccount.getFriendsList();
                //CurrentFriendList.add( //The Account information of this person // );
                GlobalClass.myAccount.setFriendsList(CurrentFriendList);
                //POST the account to the database

                //ArrayList<Account> CurrentFriendListOthers =
                    // The Account that you get//.getFriendsList();
                //CurrentFriendListOthers.add(GlobalClass.myAccount);
                ////The Account that you get//.setFriendsList(CurrentFriendListOthers);
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
                Toast.makeText(PersonalProfileOthers.this, "You made a new friend!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(PersonalProfileOthers.this, PossibleFriends.class);
                startActivity(intent);
            }
        });

        Block.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Block the user
                //Get the account information from the database
                ArrayList<Account> BlockAccounts = GlobalClass.myAccount.getBlockList();
                //BlockAccounts.add(//the account information from the database//);
                GlobalClass.myAccount.setBlockList(BlockAccounts);

                RequestBody body = RequestBody.create("{"+ "}",
                        MediaType.parse("application/json"));
                Request blockUser = new Request.Builder()
                        .url("https://20.172.9.70/users/blockUser/" + myAccount.getUserId() + "/" + posFriendId)
                        .put(body)
                        .build();
                NewCallPost(client, blockUser);
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