package com.example.cpen321tutorial1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

public class EditGymsProfile extends AppCompatActivity {

    private TextView Name, Location, AccessTime, Website, Tips;

    private Button Done, Cancel;

    final static String TAG = "Gym Edit";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_gyms_profile);
        initWidgets();

        //the manager is the gym's owner and they are the only ones able to edit it

        //TODO: uncomment these later
//        Name.setText(Gym.CurrentGym.get(0).getGymName());
//        Location.setText(Gym.CurrentGym.get(0).getLocation());
//        AccessTime.setText(Gym.CurrentGym.get(0).getAccessTime());
//        Website.setText(Gym.CurrentGym.get(0).getWebsite());
//        Tips.setText(Gym.CurrentGym.get(0).getTips());

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

                Gym TheAddGym = new Gym( InputName, InputLocation, InputAccessTime, InputWebsite, InputTips);

                //////////////////////////////////////////////////
                ///Upload the CurrentAccount information into database///
                //////////////////////////////////////////////////

                Intent NewGymsIntent = new Intent(EditGymsProfile.this, GymsProfileManager.class);
                startActivity(NewGymsIntent);
            }
        });
    }

    private void initWidgets() {
        Location = findViewById(R.id.Location);
        AccessTime = findViewById(R.id.AccessTime);
        Website = findViewById(R.id.Website);
        Name = findViewById(R.id.GymName);
        Tips = findViewById(R.id.Tips);
        Done = findViewById(R.id.Done);
        Cancel = findViewById(R.id.Cancel);
    }
}