package com.example.cpen321tutorial1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

public class NewGyms
        extends AppCompatActivity {

    private TextView Name;

    private TextView Location;

    private TextView AccessTime;

    private TextView Website;

    private TextView Tips;

    private Button Done;

    private Button Cancel;

    final static String TAG = "NewGyms";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_gyms);
        initWidgets();

        Done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Account Owner = GlobalClass.myAccount;
                ArrayList<Account> SubscribedUserss = new ArrayList<>();
                String InputName = Name.getText().toString();
                String InputLocation = Location.getText().toString();
                String InputAccessTime = AccessTime.getText().toString();
                String InputWebsite = Website.getText().toString();
                String InputTips = Tips.getText().toString();

                Log.d(TAG, "Owner: " + Owner.getUsername());
                Log.d(TAG, "Name: " + InputName);
                Log.d(TAG, "Location: " + InputLocation);
                Log.d(TAG, "AccessTime: " + InputAccessTime);
                Log.d(TAG, "Website: " + InputWebsite);
                Log.d(TAG, "Tips: " + InputTips);

                Gym TheAddGym = new Gym(InputName,
                        InputLocation, InputAccessTime,
                        InputWebsite, InputTips);
                //Gym.CurrentGym.clear();
                //Gym.CurrentGym.add(TheAddGym);

                //////////////////////////////////////////////////
                ///Upload the CurrentAccount information into database///
                //////////////////////////////////////////////////

                Intent NewGymsIntent = new Intent(NewGyms.this,
                        GymsProfileManager.class);
                startActivity(NewGymsIntent);
            }
        });

        Cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "Cancel");
                finish();
            }
        });

    }

    private void initWidgets() {
        Name = findViewById(R.id.GymName);
        Location = findViewById(R.id.Location);
        AccessTime = findViewById(R.id.AccessTime);
        Website = findViewById(R.id.Website);
        Tips = findViewById(R.id.Tips);
        Done = findViewById(R.id.Done);
        Cancel = findViewById(R.id.Cancel);
    }
}