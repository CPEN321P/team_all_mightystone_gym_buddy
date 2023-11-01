package com.example.cpen321tutorial1;

import static com.example.cpen321tutorial1.MainActivity.StringToInteger;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.util.ArrayList;

public class PersonalProfileEdit extends AppCompatActivity {

    private TextView UserName, Age, Weight;
    private Spinner GenderSpinner;
    private Button Done;
    private Button Cancel;
    private Button LogOut;
    private GoogleSignInClient mGoogleSignInClient;
    final static String TAG = "PersonalProfileEdit";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_profile_edition_user);
        initWidgets();

        UserName.setText(GlobalClass.myAccount.getUsername());
        Age.setText(Integer.toString(GlobalClass.myAccount.getAge()));
        Weight.setText(Integer.toString(GlobalClass.myAccount.getWeight()));

        Log.d(TAG, GlobalClass.myAccount.getUsername());
        Log.d(TAG, Integer.toString(GlobalClass.myAccount.getAge()));
        Log.d(TAG, Integer.toString(GlobalClass.myAccount.getWeight()));
        Log.d(TAG, GlobalClass.myAccount.getGender());

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.roles, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        GenderSpinner.setAdapter(adapter);

        Done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (StringToInteger(Age.getText().toString())<= 0 || StringToInteger(Age.getText().toString()) >= 150){
                    Toast.makeText(PersonalProfileEdit.this, "Invalid Age", Toast.LENGTH_LONG).show();
                    return;
                }
                if (StringToInteger(Weight.getText().toString()) <= 0)
                {
                    Toast.makeText(PersonalProfileEdit.this, "Invalid Weight", Toast.LENGTH_LONG).show();
                    return;
                }
                if (GenderSpinner.getSelectedItem().toString().equals("Select Your Gender")) {
                    Toast.makeText(PersonalProfileEdit.this, "Please Select your Gender", Toast.LENGTH_SHORT).show();
                    return;
                }

                Account CurrentAccount = new Account(UserName.getText().toString(), GlobalClass.myAccount.getEmailAddress(), StringToInteger(Age.getText().toString()),
                        StringToInteger(Weight.getText().toString()), GenderSpinner.getSelectedItem().toString(), GlobalClass.myAccount.getRole(),
                        GlobalClass.myAccount.getFriendsList(), GlobalClass.myAccount.getFriendsList());
                //Account.CurrentAccount.clear();
                //Account.CurrentAccount.add(CurrentAccount);

//                Log.d(TAG, "UserName: " + UserName.getText().toString());
//                Log.d(TAG, "Age: " + Integer.parseInt(Age.getText().toString()));
//                Log.d(TAG, "Weight: " + Integer.parseInt(Weight.getText().toString()));
//                Log.d(TAG, "Email: " + GlobalClass.myAccount.getEmailAddress());
//                Log.d(TAG, "Gender: " + GenderSpinner.getSelectedItem().toString());
//                Log.d(TAG, "Role: " + GlobalClass.myAccount.getRole());
//


                //////////////////////////////////////////////////
                ///Upload the CurrentAccount information into database///
                //////////////////////////////////////////////////

                if(GlobalClass.myAccount.getRole() == "Manager"){
                    Intent PersonalProfileIntent = new Intent(PersonalProfileEdit.this, PersonalProfileManager.class);
                    startActivity(PersonalProfileIntent);
                }
                else if(GlobalClass.myAccount.getRole() == "User"){
                    Intent PersonalProfileIntent = new Intent(PersonalProfileEdit.this, PersonalProfileUsers.class);
                    startActivity(PersonalProfileIntent);
                }

            }
        });

        Cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "Cancel Editing");
                finish();
            }
        });

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(PersonalProfileEdit.this, gso);

        LogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signOut();
                Intent LogOutIntent = new Intent(PersonalProfileEdit.this, LoginPage.class);
                startActivity(LogOutIntent);
            }
        });
    }

    private void initWidgets() {
        UserName = findViewById(R.id.username);
        Age = findViewById(R.id.Age);
        Weight = findViewById(R.id.Weight);
        GenderSpinner = findViewById(R.id.planets_spinner);
        Done = findViewById(R.id.Done);
        Cancel = findViewById(R.id.Cancel);
        LogOut = findViewById(R.id.LogOut);
    }

    private void signOut() {
        mGoogleSignInClient.signOut()
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        // ...
                        Log.d(TAG, "Log out successful");
                        Toast.makeText(PersonalProfileEdit.this, "Log out successful", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}