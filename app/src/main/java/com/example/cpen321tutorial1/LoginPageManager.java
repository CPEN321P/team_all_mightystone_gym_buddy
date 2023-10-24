package com.example.cpen321tutorial1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class LoginPageManager extends AppCompatActivity {

    private Button LogInButton;
    private Button ModeButton;
    final static String TAG = "ManagerLogInActivity";
    private TextView UserName;
    private android.widget.TextView Password;
    private String InputUserName;
    private String InputPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page_manager);

        LogInButton = findViewById(R.id.loginbutton);
        ModeButton = findViewById(R.id.ManagerMode);
        UserName = findViewById(R.id.username);
        Password = findViewById(R.id.password);

        ModeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "Trying to Switch to User Mode");

                Intent LoginPageUserIntent = new Intent(LoginPageManager.this, LoginPage.class);
                startActivity(LoginPageUserIntent);
            }
        });

        InputUserName = "114514"; //Edit Later
        InputPassword = "1919810"; //Edit Later

        LogInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "Trying to Log In");

                if(UserName.getText().toString().equals(InputUserName)
                        && Password.getText().toString().equals(InputPassword)) {
                    //correct
                    Toast.makeText(LoginPageManager.this, "Log in successful", Toast.LENGTH_SHORT).show();
                }else{
                    //incorrect
                    Toast.makeText(LoginPageManager.this, "User Name or Password Incorrect!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}