package com.example.cpen321tutorial1;

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

public class LinkToGoogle extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private TextView UserName;
    private TextView Password;
    private TextView ConfirmPassword;
    private Spinner RoleSpinner;
    private Button Done;

    final static String TAG = "LinkActivity";

    MainActivity.LoginInfo Account = new MainActivity.LoginInfo();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_link_to_google);

        Done = findViewById(R.id.sign_in_button);
        UserName = findViewById(R.id.username);
        Password = findViewById(R.id.password);
        ConfirmPassword = findViewById(R.id.ConfirmPassword);

        RoleSpinner = findViewById(R.id.planets_spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.roles, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        RoleSpinner.setAdapter(adapter);
        RoleSpinner.setOnItemSelectedListener(this);

        Done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "Trying to Sign");


                if (Password.getText().toString().equals(ConfirmPassword.getText().toString()) && UserName.getText().toString().isEmpty() == false && Password.getText().toString().isEmpty() == false) {

                    if (RoleSpinner.getSelectedItem().toString().equals("User")) {
                        Account.UserOrManager = 0;
                    }
                    else if (RoleSpinner.getSelectedItem().toString().equals("Manager")) {
                        Account.UserOrManager = 1;
                    }
                    else{
                        Toast.makeText(LinkToGoogle.this, "Please Select the Role", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    Account.Username = UserName.getText().toString();
                    Account.Password = Password.getText().toString();
                    Account.GoogleAccount = 1;
                    //Account.EmailAddress = ;
                    String EmailUser = LoginPage.getStringName();
                    String EmailManager = LoginPageManager.getStringName();

                    if (EmailUser.equals("NONE")){
                        Account.EmailAddress = LoginPageManager.getStringName();
                    }else{
                        Account.EmailAddress = LoginPageManager.getStringName();
                    }

                    ////////////////////////////////////////////////////////
                    Log.d(TAG, "UserName " + Account.Username);
                    Log.d(TAG, "Password " + Account.Password);
                    Log.d(TAG, "LinkGoogleAccount " + Account.GoogleAccount);
                    Log.d(TAG, "UserOrManager " + Account.UserOrManager);
                    Log.d(TAG, "Email " + Account.EmailAddress);
                    ////////////////PUSH account into data base/////////////////
                    Toast.makeText(LinkToGoogle.this, "Creat Account Successful!", Toast.LENGTH_SHORT).show();
                    Intent BackToLogin = new Intent(LinkToGoogle.this, LoginPage.class);
                    startActivity(BackToLogin);
                }
                else if (UserName.getText().toString().isEmpty() || Password.getText().toString().isEmpty()){
                    Toast.makeText(LinkToGoogle.this, "No Empty UserName or Password!", Toast.LENGTH_SHORT).show();
                }
                else{
                    //Password and ConfirmPassword does not match
                    Toast.makeText(LinkToGoogle.this, "Password and ConfirmPassword does not match!!!", Toast.LENGTH_SHORT).show();
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