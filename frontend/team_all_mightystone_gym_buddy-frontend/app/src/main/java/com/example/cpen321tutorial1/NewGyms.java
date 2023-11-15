package com.example.cpen321tutorial1;

import static com.example.cpen321tutorial1.GlobalClass.client;
import static com.example.cpen321tutorial1.GlobalClass.manager;
import static com.example.cpen321tutorial1.GlobalClass.myAccount;
import static com.example.cpen321tutorial1.GlobalClass.myGym;
import static com.example.cpen321tutorial1.JsonFunctions.NewCallPost;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;

public class NewGyms
        extends AppCompatActivity {

    private TextView Name;

    private TextView Location;

    private TextView Phone;

    //private TextView Email;

    private TextView Description;

    private Button Done;

    private Button Cancel;

    final static String TAG = "NewGyms";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_gyms);
        initWidgets();

        Done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Account Owner = GlobalClass.myAccount;
                //ArrayList<Account> SubscribedUserss = new ArrayList<>();
                String InputName = Name.getText().toString();
                String InputLocation = Location.getText().toString();
                String InputPhone = Phone.getText().toString();
                //String InputEmail = Email.getText().toString();
                String InputDescription = Description.getText().toString();

                Log.d(TAG, "Owner: " + Owner.getUsername());
                Log.d(TAG, "Name: " + InputName);
                Log.d(TAG, "Location: " + InputLocation);
                Log.d(TAG, "Phone: " + InputPhone);
                //Log.d(TAG, "Email: " + InputEmail);
                Log.d(TAG, "Description: " + InputDescription);

                /*
                Gym TheAddGym = new Gym(InputName,
                        InputLocation, InputAccessTime,
                        InputWebsite, InputTips);

                 */
                //Gym.CurrentGym.clear();
                //Gym.CurrentGym.add(TheAddGym);

                String Json = "";
                String JsonName = JsonFunctions.JsonName(InputName);
                String JsonDescription = JsonFunctions.JsonDescription(InputDescription);
                String JsonLocation = JsonFunctions.JsonLocation(InputLocation);
                String JsonPhone = JsonFunctions.JsonPhone(InputPhone);
                String JsonEmail = JsonFunctions.JsonEmail(manager.getEmail());

                Json = "{" + JsonName + "," + JsonDescription +
                        "," + JsonLocation + "," + JsonPhone +
                        "," + JsonEmail + "}";

                Log.d(TAG, Json);

                RequestBody body = RequestBody.create(Json,
                        MediaType.parse("application/json"));

                Request requestName = new Request.Builder()
                        .url("https://20.172.9.70/gyms")
                        .post(body)
                        .build();

                NewCallPost(client, requestName);


                /////////////////////////////////////////////
                myGym.setName(InputName);
                myGym.setDescription(InputDescription);
                myGym.setAddress(InputLocation);
                myGym.setPhone(InputPhone);
                myGym.setEmail(manager.getEmail());
                /////////////////////////////////////////////


                /*
                ConnectionToBackend c =
                        new ConnectionToBackend();
                Gym TheNewGym = c.getGymByEmail(myAccount.getEmailAddress());
                myGym = TheNewGym;
                manager.set_id(TheNewGym.getGymId());

                 */


                //////////////////////////////////////////////////
                ///Upload the CurrentAccount information into database///
                //////////////////////////////////////////////////

                Toast.makeText(NewGyms.this,
                        "New Gym Added Success",
                        Toast.LENGTH_SHORT).show();

                Intent NewGymsIntent =
                        new Intent(NewGyms.this, PersonalProfileManager.class);

                startActivity(NewGymsIntent);
            }
        });

        Cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "Cancel");
                finish();
            }
        });

    }

    private void initWidgets() {
        Name = findViewById(R.id.GymName);
        Location = findViewById(R.id.Location);
        Phone = findViewById(R.id.Phone);
        //Email = findViewById(R.id.Email);
        Description = findViewById(R.id.Description);
        Done = findViewById(R.id.Done);
        Cancel = findViewById(R.id.Cancel);
    }
}