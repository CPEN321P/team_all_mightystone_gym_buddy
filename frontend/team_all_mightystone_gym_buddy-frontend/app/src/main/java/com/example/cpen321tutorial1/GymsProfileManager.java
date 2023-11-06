package com.example.cpen321tutorial1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class GymsProfileManager
        extends AppCompatActivity {

    TextView Name;

    TextView Location;

    TextView AccessTime;

    TextView Website;

    TextView Tips;

    Button Edit;

    Button Cancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gyms_profile_manager);
        initWidgets();

        //Get the relative information from the database
        //Name.setText(Gym.CurrentGym.get(0).getGymName());
        //Location.setText(Gym.CurrentGym.get(0).getLocation());
        //AccessTime.setText(Gym.CurrentGym.get(0).getAccessTime());
        //Website.setText(Gym.CurrentGym.get(0).getWebsite());
        //Tips.setText(Gym.CurrentGym.get(0).getTips());

        Edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent EditGymProfile =
                        new Intent(GymsProfileManager.this,
                                EditGymsProfile.class);
                startActivity(EditGymProfile);
            }
        });

        Cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent CancelProfile =
                        new Intent(GymsProfileManager.this,
                                PersonalProfileManager.class);
                startActivity(CancelProfile);
            }
        });
    }

    private void initWidgets() {
        Location = findViewById(R.id.Location);
        AccessTime = findViewById(R.id.AccessTime);
        Website = findViewById(R.id.Website);
        Name = findViewById(R.id.GymName);
        Tips = findViewById(R.id.Tips);
        Edit = findViewById(R.id.Edit);
        Cancel = findViewById(R.id.Cancel);
    }
}