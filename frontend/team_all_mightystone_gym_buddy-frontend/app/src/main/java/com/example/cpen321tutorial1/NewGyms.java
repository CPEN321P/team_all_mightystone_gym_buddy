package com.example.cpen321tutorial1;

import static com.example.cpen321tutorial1.GlobalClass.client;
import static com.example.cpen321tutorial1.GlobalClass.manager;
import static com.example.cpen321tutorial1.GlobalClass.myAccount;
import static com.example.cpen321tutorial1.JsonFunctions.NewCallPost;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.Status;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.Arrays;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class NewGyms
        extends AppCompatActivity {

    private TextView Name;

    private TextView Location;

    private AutocompleteSupportFragment LocationAutoComplete;

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
        if (!Places.isInitialized()) {
            Places.initialize(getApplicationContext(), "AIzaSyAaNL7ekL0-QhFIbVPu5sexexVwmcWPeek");
        }
        LocationAutoComplete.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.ADDRESS, Place.Field.NAME));
        LocationAutoComplete.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onError(@NonNull Status status) {
                Log.e(TAG, "Error:" + status.getStatusMessage());
            }

            @Override
            public void onPlaceSelected(@NonNull Place place) {
                Location.setText(place.getAddress());
                Name.setText(place.getName());
                Phone.setText(place.getPhoneNumber());
            }
        });

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

                client.newCall(requestName).enqueue(new Callback() {
                    @Override public void onFailure(Call call, IOException e) {
                        e.printStackTrace();
                    }

                    @Override public void onResponse(Call call, Response response)
                            throws IOException {
                        try (ResponseBody responseBody = response.body()) {
                            if (!response.isSuccessful())
                                throw new IOException("Unexpected code " + response);
                            manager.setGymId(responseBody.toString());
                            Log.d(TAG, "Manager's gym id: "+ manager.getGymId());
                        }
                    }
                });
                NewCallPost(client, requestName);


                /////////////////////////////////////////////
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
        LocationAutoComplete = (AutocompleteSupportFragment)
                getSupportFragmentManager().findFragmentById(R.id.places_autocomplete);
        Phone = findViewById(R.id.Phone);
        //Email = findViewById(R.id.Email);
        Description = findViewById(R.id.Description);
        Done = findViewById(R.id.Done);
        Cancel = findViewById(R.id.Cancel);
    }
}