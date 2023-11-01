package com.example.cpen321tutorial1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class GymProfile extends AppCompatActivity {

    private TextView Name, Location, AccessTime, Website, Tips;
    private Button Subscript, CancelSubscript;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gym_profile);
        initWidgets();

        //Get the relative information from the database
        //Name.setText();
        //Location.setText();
        //AccessTime.setText();
        //Website.setText();
        //Tips.setText();

        //ArrayList<Account> TheOldGymUserList = //Gym class that you get from database//.getSubscriptedUser();
        Subscript.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ////////////////////////////////////////////////////////////////////////////////////////
                //Get the Gym class file from the backend database base on the information of the Gym///
                //Add the current user into the SubscriptedUser arraylist of this gym///////////////////
                ////////////////////////////////////////////////////////////////////////////////////////

                //TheOldGymUserList.add(Account.CurrentAccount.get(0));
                //Gym class that you get from database//.setSubscriptedUser(TheOldGymUserList)
                Toast.makeText(GymProfile.this, "Subscript the Gym!", Toast.LENGTH_SHORT).show();
            }
        });

        CancelSubscript.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ////////////////////////////////////////////////////////////////////////////////////////
                //Get the Gym class file from the backend database base on the information of the Gym///
                //Remove the current user into the SubscriptedUser arraylist of this gym///////////////////
                ////////////////////////////////////////////////////////////////////////////////////////

                /*
                for(int i = 0; i < TheOldGymUserList.size(); i++){
                    if ((TheOldGymUserList.get(i)).equals(Account.CurrentAccount.get(0))){
                        TheOldGymUserList.remove(i);
                        //Gym class that you get from database//.setSubscriptedUser(TheOldGymUserList);
                        return;
                    }
                }
                 */
                Toast.makeText(GymProfile.this, "Subscription Cancelled!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initWidgets() {
        Location = findViewById(R.id.Location);
        AccessTime = findViewById(R.id.AccessTime);
        Website = findViewById(R.id.Website);
        Name = findViewById(R.id.GymName);
        Tips = findViewById(R.id.Tips);
        Subscript = findViewById(R.id.Subscript);
        CancelSubscript = findViewById(R.id.CancelSubscription);
    }
}