package com.example.cpen321tutorial1;

import static com.example.cpen321tutorial1.GlobalClass.client;
import static com.example.cpen321tutorial1.GlobalClass.myAccount;
import static com.example.cpen321tutorial1.JsonFunctions.NewCallPost;
import static com.example.cpen321tutorial1.MainActivity.StringToInteger;

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

import okhttp3.Request;
import okhttp3.RequestBody;

import androidx.appcompat.app.AppCompatActivity;


import okhttp3.MediaType;

public class LinkToGoogle extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private TextView UserName;
    private TextView Age;
    private TextView Weight;
    private Spinner RoleSpinner;
    private Button Done;

    final static String TAG = "LinkActivity";

    Account account = GlobalClass.myAccount;

    //TODO: implement what happens if user has logged in in the past

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
                Log.d(TAG, "Trying to Sign in");

                int NumberOfAge = StringToInteger(Age.getText().toString());
                int NumberOfWeight = StringToInteger(Weight.getText().toString());

                if (UserName.getText().toString().isEmpty() || Age.getText().toString().isEmpty() || Weight.getText().toString().isEmpty()){
                    Toast.makeText(LinkToGoogle.this, "Do not leave space!", Toast.LENGTH_SHORT).show();
                    return;
                }
                account.setUsername(UserName.getText().toString());


                if (RoleSpinner.getSelectedItem().toString().equals("Select Your Gender")) {
                    Toast.makeText(LinkToGoogle.this, "Please Select the Gender", Toast.LENGTH_SHORT).show();
                    return;
                }
                account.setGender(RoleSpinner.getSelectedItem().toString());


                if (NumberOfAge <= 0 || NumberOfAge >= 150)
                {
                    Toast.makeText(LinkToGoogle.this, "Invalid Age", Toast.LENGTH_LONG).show();
                    return;
                }
                account.setAge(NumberOfAge);

                if (NumberOfWeight <= 0)
                {
                    Toast.makeText(LinkToGoogle.this, "Invalid Weight", Toast.LENGTH_LONG).show();
                    return;
                }
                account.setWeight(NumberOfWeight);

                if((LoginPageManager.getStringName()).equals("NONE")) {
                    //account.setEmailAddress(LoginPage.getStringName()); //User
                    account.setRole("User");
                }else{
                    //account.setEmailAddress(LoginPageManager.getStringName()); //Manager
                    account.setRole("Manager");
                }

//                Log.d(TAG, "UserName: " + account.Username);
//                Log.d(TAG, "Age: " + account.Age);
//                Log.d(TAG, "Weight: " + account.Weight);
//                Log.d(TAG, "Email: " + account.EmailAddress);
//                Log.d(TAG, "Gender: " + account.Gender);
//                Log.d(TAG, "Role: " + account.Role);

                Toast.makeText(LinkToGoogle.this, "Account Created Successfully!", Toast.LENGTH_SHORT).show();

                ArrayList<Account> TheEmptyFriendList = new ArrayList<>();
                ArrayList<Account> TheEmptyBlockList = new ArrayList<>();

                account.setFriendsList(TheEmptyFriendList);
                account.setBlockList(TheEmptyBlockList);

                //Account.CurrentAccount.clear();
                //Account.CurrentAccount.add(account);
                Gym.CurrentGym.clear();
                ////////////////////////////////////////////////////////
                //CurrentAccount
                ////////////////POST account into data base/////////////////

                String Json = "";
                if (GlobalClass.myAccount.getRole() == "User") {
                    String JsonName = JsonFunctions.JsonName(GlobalClass.myAccount.getUsername());
                    String JsonEmail = JsonFunctions.JsonEmail(GlobalClass.myAccount.getEmailAddress());
                    String JsonAge = JsonFunctions.JsonAge(GlobalClass.myAccount.getAge());
                    String JsonWeight = JsonFunctions.JsonWeight(GlobalClass.myAccount.getWeight());
                    String JsonGender = JsonFunctions.JsonGender(GlobalClass.myAccount.getGender());
                    Json = "{" + JsonName + "," + JsonEmail + "," + JsonAge + "," + JsonWeight + "," + JsonGender + "}";
                    Log.d(TAG, Json);
                }
                else if (GlobalClass.myAccount.getRole() == "Manager"){
                    String JsonName = JsonFunctions.JsonName(GlobalClass.myAccount.getUsername());
                    String JsonEmail = JsonFunctions.JsonEmail(GlobalClass.myAccount.getEmailAddress());
                    Json = JsonName + JsonEmail;
                }
                RequestBody body = RequestBody.create(Json,
                        MediaType.parse("application/json"));



                Request requestName = new Request.Builder()
                        .url("https://20.172.9.70/users")
                        .post(body)
                        .build();

                NewCallPost(client, requestName);

                ConnectionToBackend c = new ConnectionToBackend();
                c.getAccountInformationFromEmail(myAccount.getEmailAddress());


                if(account.getRole() == "Manager") {

                    //TODO: add manager to database



                    Intent PersonalProfile = new Intent(LinkToGoogle.this, PersonalProfileManager.class);
                    startActivity(PersonalProfile);
                }
                else if(account.getRole() == "User") {

                    //adding user account to database



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