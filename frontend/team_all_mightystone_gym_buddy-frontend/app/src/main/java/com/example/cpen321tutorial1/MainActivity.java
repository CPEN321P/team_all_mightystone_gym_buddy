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

        GlobalClass.myAccount = new Account();
        GlobalClass.MyeventsList = new ArrayList<>();

        GetStarted = findViewById(R.id.getStarted);

        GetStarted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent LoginIntent =
                        new Intent(MainActivity.this, LoginPage.class);
                startActivity(LoginIntent);
            }
        });
    }



    private void checkLocationPermissions() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
            && ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

            Toast.makeText(MainActivity.this,
                    "Location Permissions Accessed",
                    Toast.LENGTH_LONG).show();

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