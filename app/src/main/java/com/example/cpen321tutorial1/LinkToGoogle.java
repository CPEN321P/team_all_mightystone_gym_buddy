package com.example.cpen321tutorial1;

import static com.example.cpen321tutorial1.MainActivity.StringToInteger;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class LinkToGoogle extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private TextView UserName;
    private TextView Age;
    private TextView Weight;
    private Spinner RoleSpinner;
    private Button Done;

    final static String TAG = "LinkActivity";

    MainActivity.AccountInfo TheAccount = new MainActivity.AccountInfo();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_link_to_google);

        Done = findViewById(R.id.sign_in_button);
        UserName = findViewById(R.id.username);
        Age = findViewById(R.id.Age);
        Weight = findViewById(R.id.Weight);

        RoleSpinner = findViewById(R.id.planets_spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.roles, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        RoleSpinner.setAdapter(adapter);
        RoleSpinner.setOnItemSelectedListener(this);

        Done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "Trying to Sign");

                int NumberOfAge = StringToInteger(Age.getText().toString());
                int NumberOfWeight = StringToInteger(Weight.getText().toString());

                if (UserName.getText().toString().isEmpty() || Age.getText().toString().isEmpty() || Weight.getText().toString().isEmpty()){
                    Toast.makeText(LinkToGoogle.this, "Do not leave space!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (RoleSpinner.getSelectedItem().toString().equals("Select Your Gender")) {
                    Toast.makeText(LinkToGoogle.this, "Please Select the Gender", Toast.LENGTH_SHORT).show();
                    return;
                }else{
                    TheAccount.Gender = RoleSpinner.getSelectedItem().toString();
                }

                if (NumberOfAge <= 0 || NumberOfAge >= 150)
                {
                    Toast.makeText(LinkToGoogle.this, "Invalid Age", Toast.LENGTH_LONG).show();
                    return;
                }
                TheAccount.Age = NumberOfAge;

                if (NumberOfWeight <= 0)
                {
                    Toast.makeText(LinkToGoogle.this, "Invalid Weight", Toast.LENGTH_LONG).show();
                    return;
                }
                TheAccount.Weight = NumberOfWeight;




                TheAccount.Username = UserName.getText().toString();

                if((LoginPageManager.getStringName()).equals("NONE")) {
                    TheAccount.EmailAddress = LoginPage.getStringName(); //User
                    TheAccount.Role = "User";
                }else{
                    TheAccount.EmailAddress = LoginPageManager.getStringName(); //Manager
                    TheAccount.Role = "Manager";
                }

                Log.d(TAG, "UserName: " + TheAccount.Username);
                Log.d(TAG, "Age: " + TheAccount.Age);
                Log.d(TAG, "Weight: " + TheAccount.Weight);
                Log.d(TAG, "Email: " + TheAccount.EmailAddress);
                Log.d(TAG, "Gender: " + TheAccount.Gender);
                Log.d(TAG, "Role: " + TheAccount.Role);

                Toast.makeText(LinkToGoogle.this, "Creat Account Successful!", Toast.LENGTH_SHORT).show();

                ArrayList<Account> TheEmptyFriendList = new ArrayList<>();

                Account CurrentAccount = new Account(TheAccount.Username, TheAccount.EmailAddress, TheAccount.Age, TheAccount.Weight, TheAccount.Gender, TheAccount.Role, TheEmptyFriendList);
                Account.CurrentAccount.clear();
                Account.CurrentAccount.add(CurrentAccount);
                Gym.CurrentGym.clear();
                ////////////////////////////////////////////////////////
                //CurrentAccount
                ////////////////PUSH account into data base/////////////////

                if(TheAccount.Role == "Manager") {
                    //Enter the home page of manager
                    Intent PersonalProfile = new Intent(LinkToGoogle.this, PersonalProfileManager.class);
                    startActivity(PersonalProfile);
                }
                else if(TheAccount.Role == "User") {
                    Intent PersonalProfile = new Intent(LinkToGoogle.this, PersonalProfileUsers.class);
                    startActivity(PersonalProfile);
                }
            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}