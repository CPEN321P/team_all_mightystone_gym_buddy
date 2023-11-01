package com.example.cpen321tutorial1;

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

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

public class LinkToGoogle extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private TextView UserName;
    private TextView Age;
    private TextView Weight;
    private Spinner RoleSpinner;
    private Button Done;

    final static String TAG = "LinkActivity";

    static final class AccountInfo {
        String Username;
        String EmailAddress;
        int Age;
        int Weight;
        String Gender;
        String Role;
    }

    AccountInfo TheAccount = new AccountInfo();

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
                ArrayList<Account> TheEmptyBlockList = new ArrayList<>();

                Account CurrentAccount = new Account(TheAccount.Username, TheAccount.EmailAddress, TheAccount.Age, TheAccount.Weight, TheAccount.Gender, TheAccount.Role, TheEmptyFriendList, TheEmptyBlockList);
                Account.CurrentAccount.clear();
                Account.CurrentAccount.add(CurrentAccount);
                Gym.CurrentGym.clear();
                ////////////////////////////////////////////////////////
                //CurrentAccount
                ////////////////POST account into data base/////////////////

                String Json = "";
                if (CurrentAccount.getRole() == "User") {
                    String JsonName = JsonFunctions.JsonName(CurrentAccount.getUsername());
                    String JsonEmail = JsonFunctions.JsonEmail(CurrentAccount.getEmailAddress());
                    String JsonAge = JsonFunctions.JsonAge(CurrentAccount.getAge());
                    String JsonWeight = JsonFunctions.JsonWeight(CurrentAccount.getWeight());
                    String JsonGender = JsonFunctions.JsonGender(CurrentAccount.getGender());
                    Json = "{" + JsonName + "," + JsonEmail + "," + JsonAge + "," + JsonWeight + "," + JsonGender + "}";
                    Log.d(TAG, Json);
                }
                else if (CurrentAccount.getRole() == "Manager"){
                    String JsonName = JsonFunctions.JsonName(CurrentAccount.getUsername());
                    String JsonEmail = JsonFunctions.JsonEmail(CurrentAccount.getEmailAddress());
                    Json = JsonName + JsonEmail;
                }
                RequestBody body = RequestBody.create(Json,
                        MediaType.parse("application/json"));

                final OkHttpClient client = new OkHttpClient();

                Request requestName = new Request.Builder()
                        .url("http://20.172.9.70:8081/users")
                        .post(body)
                        .build();

                NewCallPost(client, requestName);

                //String JsonRole = JsonFunctions.JsonRole(CurrentAccount.getRole());
                //String JsonFriendList =


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