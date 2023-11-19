package com.example.cpen321tutorial1;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;

public class MainActivity
        extends AppCompatActivity {

    final static String TAG = "MainActivity";

    //private Button Gyms;
    //private Button Friends;
    //private Button MFour;
    //private Button Schedule;
    //private Button PersonalProfile;
    //private Button AddGym;
    //private Button OtherProfile, FriendProfile, UserGyms;

    Button GetStarted;

    public static int TestComeFromOutsideOrNot = 0;
    //A public integer that use for jump to weekly schedule

    private static String stringName = "NONE";
    //private ActivityMainBinding binding;

    public static String getStringName(){
        return stringName;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        checkLocationPermissions();

        GlobalClass.TestTempPeopleList = 0;

        GlobalClass.myAccount = new Account();
        GlobalClass.MyeventsList = new ArrayList<>();

        //Intent LoginIntent =
        //        new Intent(MainActivity.this, LoginPage.class);
        //startActivity(LoginIntent);

        GetStarted = findViewById(R.id.getStarted);

        GetStarted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent LoginIntent =
                        new Intent(MainActivity.this, LoginPage.class);
                startActivity(LoginIntent);
            }
        });


        /*
        Gyms = findViewById(R.id.gyms);

        Gyms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent GymIntent =
                        new Intent(MainActivity.this, Gyms.class);
                startActivity(GymIntent);
            }
        });

         */

        /*
        Friends = findViewById(R.id.friends);

        Friends.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent FriendIntent =
                        new Intent(MainActivity.this, Friends.class);
                startActivity(FriendIntent);
            }
        });
         */

        //Schedule = findViewById(R.id.scheduleTest);
        //Schedule.setOnClickListener(new View.OnClickListener() {
        //    @Override
        //    public void onClick(View view) {
        //        TestComeFromOutsideOrNot = 1;
        //        Intent ScheduleIntent =
        //              new Intent(MainActivity.this, MonthlySchedule.class);
        //        startActivity(ScheduleIntent);
        //    }
        //});

        //PersonalProfile = findViewById(R.id.EditPersonalProfile);
        //PersonalProfile.setOnClickListener(new View.OnClickListener() {
        //    @Override
        //    public void onClick(View view) {
        //        Intent PersonalProfileEditIntent =
        //                new Intent(MainActivity.this, PersonalProfileUsers.class);
        //        startActivity(PersonalProfileEditIntent);
        //    }
        //});

        /*
        signOutButton = findViewById(R.id.Sign_out_button);
        signOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signOut();
            }
        });

         */

        /*
        MFour = findViewById(R.id.mfour);
        MFour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "Trying to open google maps");

                Intent mfourIntent =
                        new Intent(MainActivity.this, LoginPage.class);
                startActivity(mfourIntent);
            }
        });

        AddGym = findViewById(R.id.AddGym);
        AddGym.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "Add Gym");

                Intent mfourIntent =
                      new Intent(MainActivity.this, NewGyms.class);
                startActivity(mfourIntent);
            }
        });

        OtherProfile = findViewById(R.id.OthersProfile);
        FriendProfile = findViewById(R.id.FriendsProfile);
        UserGyms = findViewById(R.id.GymProfileUser);

        OtherProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent Intent =
                      new Intent(MainActivity.this,
                            PersonalProfileOthers.class);
                startActivity(Intent);
            }
        });

        FriendProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent Intent =
                      new Intent(MainActivity.this,
                      PersonalProfileFriend.class);
                startActivity(Intent);
            }
        });

        UserGyms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent Intent =
                      new Intent(MainActivity.this, GymProfile.class);
                startActivity(Intent);
            }
        });
         */
    }


    /*
    private void signOut() {
        mGoogleSignInClient.signOut()
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        // ...
                        Log.d(TAG, "Log out successful");
                        Toast.makeText(MainActivity.this,
                                "Log out successful",
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }

     */


    private void checkLocationPermissions() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
            && ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

            Toast.makeText(MainActivity.this,
                    "Location Permissions Accessed",
                    Toast.LENGTH_LONG).show();

            return;
        }
        else{
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                    || ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {

                Toast.makeText(MainActivity.this,
                        "We need these location permissions to run!",
                        Toast.LENGTH_LONG).show();

                new AlertDialog.Builder(this)
                        .setTitle
                                ("Need Location Permissions")
                        .setMessage
                                ("We need the location permissions to mark your location on a map")
                        .setNegativeButton
                                ("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Toast.makeText(MainActivity.this, "We need these location permission to run!", Toast.LENGTH_LONG).show();
                                dialogInterface.dismiss();
                            }
                        })
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                ActivityCompat.requestPermissions(MainActivity.this,
                                        new String[] {Manifest.permission.ACCESS_COARSE_LOCATION,
                                                Manifest.permission.ACCESS_FINE_LOCATION}, 1);
                            }
                        })
                        .create()
                        .show();
            }
            else{
                ActivityCompat.requestPermissions(this,
                        new String[] {Manifest.permission.ACCESS_COARSE_LOCATION,
                                Manifest.permission.ACCESS_FINE_LOCATION},
                        1);
            }
        }
    }

    public static int StringToInteger(String input){
        try {
            int number = Integer.valueOf(input);
            return number;
        } catch (NumberFormatException e) {
            return 0;
        }
    }
}