package com.example.cpen321tutorial1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.net.URISyntaxException;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

public class Logo
        extends AppCompatActivity {

    Button Home;

    Button Friends;

    Button Schedule;

    Button Gyms;

    Button PersonalProfile;

    Button LogOut;

    private GoogleSignInClient mGoogleSignInClient;

    final static String TAG = "Logo";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logo);

        Home = findViewById(R.id.navigation_home);

        Home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent HomeIntent =
                        new Intent(Logo.this, Logo.class);
                startActivity(HomeIntent);
//
//                Socket socket;
//
//                try {
//                    // Replace "http://your-server-url" with the actual URL of your Socket.IO server
//                    socket = IO.socket("https://20.172.9.70:443");
//
//                    socket.on(Socket.EVENT_CONNECT, new Emitter.Listener() {
//                        @Override
//                        public void call(Object... args) {
//                            Log.d("SOCKETTTTT", "Connected to server");
//                        }
//                    });
//
//                    socket.connect();
//                } catch (URISyntaxException e) {
//                    e.printStackTrace();
//                    throw new RuntimeException(e);
//                }
            }
        });

        Friends = findViewById(R.id.navigation_friends);

        Friends.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent FriendsIntent =
                        new Intent(Logo.this, Friends.class);
                startActivity(FriendsIntent);
            }
        });

        Schedule = findViewById(R.id.navigation_schedule);

        Schedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent ScheduleIntent =
                        new Intent(Logo.this, ScheduleMonthly.class);
                startActivity(ScheduleIntent);
            }
        });

        Gyms = findViewById(R.id.navigation_gyms);

        Gyms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent GymIntent =
                        new Intent(Logo.this, Gyms.class);
                startActivity(GymIntent);
            }
        });

        PersonalProfile = findViewById(R.id.navigation_profile);

        PersonalProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent ProfileIntent =
                        new Intent(Logo.this, PersonalProfileUsers.class);
                startActivity(ProfileIntent);
            }
        });

        GoogleSignInOptions gso =
                new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                        .requestEmail()
                        .build();

        mGoogleSignInClient =
                GoogleSignIn.getClient(Logo.this, gso);

        LogOut = findViewById(R.id.LogOut);

        LogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signOut();
                Intent LogOutIntent =
                        new Intent(Logo.this,
                                LoginPage.class);
                startActivity(LogOutIntent);
            }
        });

    }

    private void signOut() {
        mGoogleSignInClient.signOut()
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        // ...
                        Log.d(TAG, "Log out successful");
                        Toast.makeText(Logo.this,
                                "Log out successful",
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    public void onBackPressed(){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(Logo.this);
        alertDialog.setTitle("Exit App");
        alertDialog.setMessage("Do you want to exit the app?");
        alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                finishAffinity();
            }
        });
        alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });

        alertDialog.show();

    }
}