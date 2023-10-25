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

public class SignIn extends AppCompatActivity implements AdapterView.OnItemSelectedListener {


    private Button Done;
    private TextView Email;
    private TextView UserName;
    private TextView Password;
    private TextView ConfirmPassword;
    private Spinner RoleSpinner;

    final static String TAG = "SignInActivity";

    MainActivity.LoginInfo Account = new MainActivity.LoginInfo();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        Done = findViewById(R.id.sign_in_button);

        UserName = findViewById(R.id.username);
        Password = findViewById(R.id.password);
        ConfirmPassword = findViewById(R.id.ConfirmPassword);
        Email = findViewById(R.id.email);

        RoleSpinner = findViewById(R.id.planets_spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.roles, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        RoleSpinner.setAdapter(adapter);
        RoleSpinner.setOnItemSelectedListener(this);

        Done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "Trying to Sign");

                //if (Email is match to a account that excess in the user database){
                //Toast.makeText(SignIn.this, "Email Already Exit!", Toast.LENGTH_SHORT).show();
                //return
                //}else
                if (Password.getText().toString().equals(ConfirmPassword.getText().toString()) && UserName.getText().toString().isEmpty() == false && Password.getText().toString().isEmpty() == false) {

                    if (RoleSpinner.getSelectedItem().toString().equals("User")) {
                        Account.UserOrManager = 0;
                    }
                    else if (RoleSpinner.getSelectedItem().toString().equals("Manager")) {
                        Account.UserOrManager = 1;
                    }
                    else{
                        Toast.makeText(SignIn.this, "Please Select the Role", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    //if (Email is match to a account that excess in the user database)
                        //Toast.makeText(SignIn.this, "Email Already Exit!", Toast.LENGTH_SHORT).show();
                    //else
                    Account.Username = UserName.getText().toString();
                    Account.Password = Password.getText().toString();
                    Account.GoogleAccount = 0;
                    Account.EmailAddress = Email.getText().toString();

                    ////////////////////////////////////////////////////////
                    Log.d(TAG, "UserName " + Account.Username);
                    Log.d(TAG, "Password " + Account.Password);
                    Log.d(TAG, "LinkGoogleAccount " + Account.GoogleAccount);
                    Log.d(TAG, "UserOrManager " + Account.UserOrManager);
                    Log.d(TAG, "Email " + Account.EmailAddress);
                    ////////////////PUSH account into data base/////////////////
                    Toast.makeText(SignIn.this, "Creat Account Successful!", Toast.LENGTH_SHORT).show();
                    Intent BackToLogin = new Intent(SignIn.this, LoginPage.class);
                    startActivity(BackToLogin);
                }
                else if (UserName.getText().toString().isEmpty() || Password.getText().toString().isEmpty()){
                    Toast.makeText(SignIn.this, "No Empty UserName or Password!", Toast.LENGTH_SHORT).show();
                }
                else{
                    //Password and ConfirmPassword does not match
                    Toast.makeText(SignIn.this, "Password and ConfirmPassword does not match!!!", Toast.LENGTH_SHORT).show();
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