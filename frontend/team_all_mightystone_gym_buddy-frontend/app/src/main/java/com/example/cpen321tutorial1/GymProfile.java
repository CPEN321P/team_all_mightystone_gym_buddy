package com.example.cpen321tutorial1;

import static com.example.cpen321tutorial1.GlobalClass.client;
import static com.example.cpen321tutorial1.GlobalClass.manager;
import static com.example.cpen321tutorial1.GlobalClass.myAccount;
import static com.example.cpen321tutorial1.JsonFunctions.JsonHomeGym;
import static com.example.cpen321tutorial1.JsonFunctions.NewCallPost;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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

    private Button Subscribe;

    private Button Announcement;

    private boolean isSubscribed = false;

    final static String TAG = "GymProfile";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gym_profile);
        initWidgets();
        Intent i = getIntent();

        Log.d(TAG, "Test1");

        String gymAddress = i.getStringExtra("GymAddress");
        String gymName = i.getStringExtra("GymName");
        String gymEmail = i.getStringExtra("GymEmail");
        String gymDescription = i.getStringExtra("GymDescription");
        String gymPhone = i.getStringExtra("GymPhone");
        String gymId = i.getStringExtra("GymId");

        Log.d(TAG, "Test2");

        //Log.d("HAHA", "address: " + gymAddress);
        Name.setText(gymName);
        Location.setText(gymAddress);
        Email.setText(gymEmail);
        Description.setText(gymDescription);
        Phone.setText(gymPhone);

        Announcement.setVisibility(View.GONE);

        Log.d(TAG, "Test3");

        if(myAccount.getMyGym() != null){
            if(myAccount.getMyGym().getGymId().equals(gymId)){
                isSubscribed = true;
                Subscribe.setText("Unsubscribe");
                Announcement.setVisibility(View.VISIBLE);
            }
        }

        //TEST

        Log.d(TAG, "Test4");

        //Get the relative information from the database
        //ArrayList<Account> TheOldGymUserList =
        // Gym class that you get from database//.getSubscribedUsers();
        Subscribe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ////////////////////////////////////////////////////////////////////////////////////////
                //Get the Gym class file from the backend database base on the information of the Gym///
                //Add the current user into the SubscribedUsers arraylist of this gym///////////////////
                ////////////////////////////////////////////////////////////////////////////////////////

                //TheOldGymUserList.add(GlobalClass.myAccount);
                //Gym class that you get from database//.setSubscribedUsers(TheOldGymUserList)
                if(!isSubscribed) {
                    RequestBody body = RequestBody.create
                            ("{" + JsonHomeGym(gymId) + "}",
                                    MediaType.parse("application/json"));

                    Request subscribeToGym = new Request.Builder()
                            .url("https://20.172.9.70/users/userId/" +
                                    myAccount.getUserId())
                            .put(body)
                            .build();
                    NewCallPost(client, subscribeToGym);
                    myAccount.setMyGym(new Gym(gymName, gymAddress, gymPhone, gymEmail, gymDescription, gymId));
                    isSubscribed = true;
                    Subscribe.setText("Unsubscribe");
                    Toast.makeText(GymProfile.this,
                            "Subscribed to the Gym!",
                            Toast.LENGTH_SHORT).show();

                    Announcement.setVisibility(View.VISIBLE);
                }
                else{
                    RequestBody body = RequestBody.create
                            ("{" + JsonHomeGym("None") + "}",
                                    MediaType.parse("application/json"));

                    Request unsubscribeToGym = new Request.Builder()
                            .url("https://20.172.9.70/users/userId/" +
                                    myAccount.getUserId())
                            .put(body)
                            .build();
                    NewCallPost(client, unsubscribeToGym);
                    myAccount.setMyGym(new Gym());
                    isSubscribed = false;
                    Subscribe.setText("Subscribe");
                    Toast.makeText(GymProfile.this,
                            "Unsubscribed the Gym!",
                            Toast.LENGTH_SHORT).show();

                    Announcement.setVisibility(View.GONE);
                }
            }
        });

        Announcement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ConnectionToBackend c = new ConnectionToBackend();
                Manager thisManagerFromBackend =
                        c.getManagerInformationFromEmail(gymEmail);

                GlobalClass.AnnouncementList = thisManagerFromBackend.getAnnouncements();

                Intent Intent =
                        new Intent(GymProfile.this,
                                AnnouncementList.class);
                startActivity(Intent);
            }
        });

        /*CancelSubscript.setOnClickListener(new View.OnClickListener() {
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

                Toast.makeText(GymProfile.this,
                        "Subscription Cancelled!",
                        Toast.LENGTH_SHORT).show();
            }
        });*/
    }

    private void initWidgets() {
        Location = findViewById(R.id.Location);
        Phone = findViewById(R.id.Phone);
        Email = findViewById(R.id.Email);
        Name = findViewById(R.id.GymName);
        Description = findViewById(R.id.Description);
        Subscribe = findViewById(R.id.Subscribe);
        //CancelSubscript = findViewById(R.id.CancelSubscription);
        Announcement = findViewById(R.id.Announcement);
    }
}