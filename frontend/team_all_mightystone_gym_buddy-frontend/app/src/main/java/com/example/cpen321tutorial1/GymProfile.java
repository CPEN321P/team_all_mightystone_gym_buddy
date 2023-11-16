package com.example.cpen321tutorial1;

import static com.example.cpen321tutorial1.GlobalClass.client;
import static com.example.cpen321tutorial1.GlobalClass.myAccount;
import static com.example.cpen321tutorial1.GlobalClass.myGym;
import static com.example.cpen321tutorial1.JsonFunctions.JsonHomeGym;
import static com.example.cpen321tutorial1.JsonFunctions.NewCallPost;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;

public class GymProfile
        extends AppCompatActivity {

    private TextView Name;

    private TextView Location;

    private TextView Phone;

    private TextView Email;

    private TextView Description;

    private Button Subscript;

    private Button CancelSubscript;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gym_profile);
        initWidgets();
        Intent i = getIntent();

        String gymAddress = i.getStringExtra("GymAddress");
        String gymName = i.getStringExtra("GymName");
        //Log.d("HAHA", "address: " + gymAddress);
        Name.setText(gymName);
        Location.setText(gymAddress);

        //Get the relative information from the database


        //ArrayList<Account> TheOldGymUserList =
        // Gym class that you get from database//.getSubscribedUsers();
        Subscript.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ////////////////////////////////////////////////////////////////////////////////////////
                //Get the Gym class file from the backend database base on the information of the Gym///
                //Add the current user into the SubscribedUsers arraylist of this gym///////////////////
                ////////////////////////////////////////////////////////////////////////////////////////

                //TheOldGymUserList.add(GlobalClass.myAccount);
                //Gym class that you get from database//.setSubscribedUsers(TheOldGymUserList)
                RequestBody body = RequestBody.create
                        ("{"+ JsonHomeGym(myGym.getName()) + "}",
                        MediaType.parse("application/json"));

                Request subscribeToGym = new Request.Builder()
                        .url("https://20.172.9.70/users/userId/" +
                                myAccount.getUserId())
                        .put(body)
                        .build();
                NewCallPost(client, subscribeToGym);


                Toast.makeText(GymProfile.this,
                        "Subscribed to the Gym!",
                        Toast.LENGTH_SHORT).show();
            }
        });

        CancelSubscript.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ////////////////////////////////////////////////////////////////////////////////////////
                //Get the Gym class file from the backend database base on the information of the Gym///
                //Remove the current user into the SubscribedUsers arraylist of this gym///////////////////
                ////////////////////////////////////////////////////////////////////////////////////////

                /*
                for(int i = 0; i < TheOldGymUserList.size(); i++){
                    if ((TheOldGymUserList.get(i)).
                        equals(GlobalClass.myAccount)){
                        TheOldGymUserList.remove(i);
                        //Gym class that you get from database//.setSubscribedUsers(TheOldGymUserList);
                        return;
                    }
                }
                 */
                Toast.makeText(GymProfile.this,
                        "Subscription Cancelled!",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initWidgets() {
        Location = findViewById(R.id.Location);
        Phone = findViewById(R.id.Phone);
        Email = findViewById(R.id.Email);
        Name = findViewById(R.id.GymName);
        Description = findViewById(R.id.Description);
        Subscript = findViewById(R.id.Subscribe);
        CancelSubscript = findViewById(R.id.CancelSubscription);
    }
}