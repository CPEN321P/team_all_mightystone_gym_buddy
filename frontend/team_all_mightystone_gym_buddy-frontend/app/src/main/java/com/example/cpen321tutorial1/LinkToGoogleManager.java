package com.example.cpen321tutorial1;

import static com.example.cpen321tutorial1.GlobalClass.client;
import static com.example.cpen321tutorial1.GlobalClass.manager;
import static com.example.cpen321tutorial1.JsonFunctions.NewCallPost;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;

public class LinkToGoogleManager
        extends AppCompatActivity
        implements AdapterView.OnItemSelectedListener{

    private TextView UserName;

    //private TextView
    Button Done;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_link_to_google_manager);

        UserName = findViewById(R.id.username);
        Done = findViewById(R.id.sign_in_button);

        Done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (UserName.getText().toString().isEmpty()) {
                    Toast.makeText(LinkToGoogleManager.this,
                            "Enter your username!", Toast.LENGTH_SHORT).show();
                    return;
                }

                manager.setUsername(UserName.getText().toString());
                manager.setName(UserName.getText().toString());

                Toast.makeText(LinkToGoogleManager.this,
                        "Account Created Successfully!",
                        Toast.LENGTH_SHORT).show();
                //Log.d("THIS IS WHAT YOURE LOOKING FOR", "CREATED ACCOUNT");
                //adding manager to database

                String Json = "";

                String JsonName = JsonFunctions.
                        JsonName(manager.getName());

                String JsonUsername = JsonFunctions.
                        JsonUsername(manager.getUsername());

                String JsonEmail = JsonFunctions.
                        JsonEmail(manager.getEmail());

                Json = "{" + JsonName + "," + JsonUsername +
                        "," + JsonEmail + "}";

                //Log.d("this is what", Json);

                RequestBody body = RequestBody.create(Json,
                        MediaType.parse("application/json"));


                Request requestName = new Request.Builder()
                        .url("https://20.172.9.70/gymsUsers")
                        .post(body)
                        .build();

                NewCallPost(client, requestName);
                //Log.d("this is what", "post worked");

                ConnectionToBackend c =
                        new ConnectionToBackend();
                c.getManagerInformationFromEmail(manager.getEmail());

                Intent PersonalProfileManager =
                        new Intent(LinkToGoogleManager.this,
                                PersonalProfileManager.class);

                startActivity(PersonalProfileManager);

            }

        });


    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView,
                               View view, int i, long l) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

}