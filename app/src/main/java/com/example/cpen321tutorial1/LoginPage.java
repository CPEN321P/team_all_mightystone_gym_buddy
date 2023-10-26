package com.example.cpen321tutorial1;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

public class LoginPage extends AppCompatActivity {

    private Button LogInButton;
    private Button ModeButton;
    final static String TAG = "UserLogInActivity";
    private TextView UserName;
    private TextView Password;
    private String RealUserName;
    private String RealPassword;
    private GoogleSignInClient mGoogleSignInClient;
    private static String TheEmail = "NONE";
    private Button SignInButton;

    MainActivity.LoginInfo TheLoginInfo = new MainActivity.LoginInfo();

    ActivityResultLauncher<Intent> activityResult =
            registerForActivityResult(
                    new ActivityResultContracts.StartActivityForResult(),
                    new ActivityResultCallback<ActivityResult>() {
                        @Override
                        public void onActivityResult(ActivityResult activityResult) {
                            int requestCode = activityResult.getResultCode();
                            Intent intent = activityResult.getData();
                            if (intent != null){
                                Log.d(TAG, "User signed in");
                                Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(intent);
                                handleSignInResult(task);
                            }

                        }
                    }
            );


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);

        LogInButton = findViewById(R.id.loginbutton);
        ModeButton = findViewById(R.id.ManagerMode);
        UserName = findViewById(R.id.username);
        Password = findViewById(R.id.password);
        SignInButton = findViewById(R.id.signinbutton);

        ModeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "Trying to Switch to Manager Mode");

                Intent LoginPageManagerIntent = new Intent(LoginPage.this, LoginPageManager.class);
                startActivity(LoginPageManagerIntent);
            }
        });


        SignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "Trying to Sign in");

                Intent SignInIntent = new Intent(LoginPage.this, SignIn.class);
                startActivity(SignInIntent);
            }
        });

        LogInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "Trying to Log In");

                TheLoginInfo.EmailAddress = UserName.getText().toString();
                TheLoginInfo.Password = Password.getText().toString();
                TheLoginInfo.UserOrManager = 0;

                ///Need to pull from data base to confirm///
                RealUserName = "114514"; //Edit Later, we get from the database, if nothing, then it would be empty string
                RealPassword = "1919810"; //Edit Later, we get from the database, if nothing, then it would be empty string
                //////////////////////////////////////////

                if(UserName.getText().toString().equals(RealUserName)
                && Password.getText().toString().equals(RealPassword)) {
                    //correct
                    Toast.makeText(LoginPage.this, "Log in successful", Toast.LENGTH_SHORT).show();
                }else{
                    //incorrect
                    Toast.makeText(LoginPage.this, "User Name or Password Incorrect!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        findViewById(R.id.sign_in_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signIn();
            }
        });


    }

    public static String getStringName(){
        return TheEmail;
    }
    public static void ClearStringName(){
        TheEmail = "NONE";
    }

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        //startActivityForResult(signInIntent, RC_SIGN_IN);
        activityResult.launch(signInIntent);

    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);

            // Signed in successfully, show authenticated UI.
            updateUI(account);
            Toast.makeText(LoginPage.this, "Log in successful", Toast.LENGTH_SHORT).show();
            //Intent Informationintent = new Intent(LoginPage.this, ServerInfo.class);
            //startActivity(Informationintent);
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w(TAG, "signInResult:failed code=" + e.getStatusCode());
            updateUI(null);
        }
    }

    private void updateUI(GoogleSignInAccount account) {
        if (account == null) {
            Log.d(TAG, "There is no user signed in!");
        }
        else {
            Log.d(TAG, "Pref Name: " + account.getDisplayName());
            Log.d(TAG, "Email: " + account.getEmail());
            Log.d(TAG, "Given Name: " + account.getGivenName());
            Log.d(TAG, "Family Name: " + account.getFamilyName());
            Log.d(TAG, "Display URI: " + account.getPhotoUrl());

            TheLoginInfo.GoogleAccount = 1;
            TheLoginInfo.EmailAddress = account.getEmail();
            TheLoginInfo.UserOrManager = 0;
            //TheLoginInfo.Username = GET from database base on the email address;
            //TheLoginInfo.Password = GET from database base on the email address;
            TheEmail = account.getEmail();
            LoginPageManager.ClearStringName();

            //If the EmailAddress did not search from the database, then jump to activity_link_to_google
            Intent LinkAccountIntent = new Intent(LoginPage.this, LinkToGoogle.class);
            startActivity(LinkAccountIntent);
            //Otherwise, jump to the home page
        }
    }

}