package com.example.cpen321tutorial1;

import static com.example.cpen321tutorial1.GlobalClass.manager;
import static com.example.cpen321tutorial1.GlobalClass.myAccount;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

public class PersonalProfileManager
        extends AppCompatActivity {

    private Button MyGyms;

    private Button LogOut;

    private TextView Username;

    private TextView Email;

    private Button TheAnnouncement;

    private GoogleSignInClient mGoogleSignInClient;

    final static String TAG = "PersonalProfileManager";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_profile_manager);
        initWidgets();


        MainActivity.TestComeFromOutsideOrNot = 0;
        Username.setText(GlobalClass.manager.getUsername());
        Email.setText(GlobalClass.manager.getEmail());

        //Profile.setOnClickListener(new View.OnClickListener() {
        //    @Override
        //    public void onClick(View view) {
        //        Intent PersonalProfileIntent =
        //              new Intent(PersonalProfileManager.this,
        //                  PersonalProfileEdit.class);
        //        startActivity(PersonalProfileIntent);
        //    }
        //});



        TheAnnouncement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Enter the page of announcement
            }
        });


        MyGyms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Scarch for the backend database to search for
                //      the gym base on the manager Email;
                //If search it, then jump to the GymProfileManager Activity
                //Intent MyGymsIntent = new Intent
                //      (PersonalProfileManager.this, GymsProfileManager.class);
                //startActivity(MyGymsIntent);
                //Otherwise, jump to the NewGyms Activity
                //TODO FIX THIS WTFS
                /*
                if (true) {
                    Intent NewGymsIntent =
                            new Intent(PersonalProfileManager.this,
                                    NewGyms.class);
                    startActivity(NewGymsIntent);
                } else {
                    Intent NewGymsIntent =
                            new Intent(PersonalProfileManager.this,
                                    GymsProfileManager.class);
                    startActivity(NewGymsIntent);
                }

                 */
                if(!checkIfGymExists(manager.getEmail())){
                    Log.d(TAG,
                            "The Gym Does Not EXIST ON THE DATABASE");
                    Intent NewGymsIntent =
                            new Intent(PersonalProfileManager.this,
                                    NewGyms.class);
                    startActivity(NewGymsIntent);

                } else {
                    Log.d(TAG,
                            "YIPPIEEEEE The Gym EXIST ON THE DATABASE");
                    Intent NewGymsIntent =
                            new Intent(PersonalProfileManager.this,
                                    GymsProfileManager.class);
                    startActivity(NewGymsIntent);
                }

            }
        });

        GoogleSignInOptions gso =
                new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                        .requestEmail()
                        .build();

        mGoogleSignInClient =
                GoogleSignIn.getClient(PersonalProfileManager.this, gso);

        LogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signOut();
                Intent LogOutIntent =
                        new Intent(PersonalProfileManager.this,
                                LoginPageManager.class);
                startActivity(LogOutIntent);
            }
        });
    }

    private void initWidgets() {
        Username = findViewById(R.id.Username);
        Email = findViewById(R.id.Email);
        TheAnnouncement = findViewById(R.id.Announcement);
        MyGyms = findViewById(R.id.MyGyms);
        LogOut = findViewById(R.id.LogOut);
    }

    private void signOut() {
        mGoogleSignInClient.signOut()
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        // ...
                        Log.d(TAG, "Log out successful");
                        Toast.makeText(PersonalProfileManager.this,
                                "Log out successful",
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private boolean checkIfGymExists(String email) {

        ConnectionToBackend c = new ConnectionToBackend();
        Gym thisGymFromBackend =
                c.getGymByEmail(email);

        if(thisGymFromBackend == null){
            Log.d("THISSSSSSS", "manager is null :c");
            return false;
        }
        myAccount.setMyGym(thisGymFromBackend);
        manager.setGymId(thisGymFromBackend.getGymId());
        Log.d(TAG, thisGymFromBackend.getGymId());
        return true;

    }
}