package com.example.cpen321tutorial1;

import static com.example.cpen321tutorial1.GlobalClass.MyeventsList;
import static com.example.cpen321tutorial1.GlobalClass.SelectedAnnouncement;
import static com.example.cpen321tutorial1.GlobalClass.myAccount;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.util.ArrayList;

public class LoginPage extends AppCompatActivity {

    Button ModeButton;

    final static String TAG = "UserLogInActivity";

    private static String TheEmail = "NONE";

    private GoogleSignInClient mGoogleSignInClient;




    ActivityResultLauncher<Intent> activityResult =
            registerForActivityResult(
                    new ActivityResultContracts.StartActivityForResult(),
                    new ActivityResultCallback<ActivityResult>() {
                        @Override
                        public void onActivityResult
                                (ActivityResult activityResult) {

                            Intent intent = activityResult.getData();
                            if (intent != null){
                                Task<GoogleSignInAccount> task = GoogleSignIn.
                                        getSignedInAccountFromIntent(intent);
                                handleSignInResult(task);
                            }

                        }
                    }
            );


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);

        myAccount = new Account();

        ModeButton = findViewById(R.id.ManagerMode);

        ModeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent LoginPageManagerIntent =
                        new Intent(LoginPage.this, LoginPageManager.class);

                startActivity(LoginPageManagerIntent);
            }
        });


        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gso =
                new GoogleSignInOptions.
                        Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        findViewById(R.id.sign_in_button).
                setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signIn();
            }
        });

        signOut();
    }

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        //startActivityForResult(signInIntent, RC_SIGN_IN);
        activityResult.launch(signInIntent);

    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {

            GoogleSignInAccount account =
                    completedTask.getResult(ApiException.class);

            // Signed in successfully, show authenticated UI.
            updateUI(account);
            Toast.makeText(LoginPage.this,
                    "Log in successful",
                    Toast.LENGTH_SHORT).show();


        } catch (ApiException e) {
            // ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class
            //  reference for more information.
            Log.w(TAG, "signInResult:failed code=" + e.getStatusCode());
            updateUI(null);
        }
    }

    private void updateUI(GoogleSignInAccount account) {
        if (account == null) {
            Log.d(TAG, "There is no user signed in!");
        }
        else {
            //CHECK IF EMAIL ALREADY EXISTS IN DATABASE BEFORE CREATING

            if(!checkIfUserExists(account.getEmail())){
                myAccount.setEmailAddress(account.getEmail());
                Intent LinkAccountIntent =
                        new Intent(LoginPage.this, LinkToGoogle.class);
                startActivity(LinkAccountIntent);

            } else {
               Intent LinkAccountIntent =
                        new Intent(LoginPage.this, Logo.class);
                startActivity(LinkAccountIntent);
            }


            if(!checkIfEventsExists()){
                MyeventsList = new ArrayList<>();
                //String JsonUserId =
                //      JsonFunctions.JsonUserId(myAccount.getUserId());
                //String JsonDate =
                //      JsonFunctions.JsonUserId(myAccount)
            }
        }
    }

    private boolean checkIfUserExists(String email) {
        ConnectionToBackend c = new ConnectionToBackend();
        Account thisAccount = c.getAccountInformation(email);

        if(thisAccount== null){
            return false;
        }
        ArrayList<Account> items = c.getAllInList(thisAccount.getUserId(), 0);

        myAccount.setEmailAddress(email);
        myAccount = thisAccount;
        myAccount.setFriendsList(items);
        SelectedAnnouncement = new Announcement();

        return true;
    }

    private boolean checkIfEventsExists() {
        ConnectionToBackend c = new ConnectionToBackend();
        ArrayList<Event> TheEventsofThisAccount =
                c.getScheduleByUser(myAccount.getUserId());
        if(TheEventsofThisAccount == null){
            return false;
        }
        MyeventsList = TheEventsofThisAccount;
        return true;

    }

    public static String getStringName(){
        return TheEmail;
    }

    public static void ClearStringName(){
        TheEmail = "NONE";
    }

    private void signOut() {
        mGoogleSignInClient.signOut()
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                    }
                });
    }
}