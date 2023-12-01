package com.example.cpen321tutorial1;

import static com.example.cpen321tutorial1.GlobalClass.client;
import static com.example.cpen321tutorial1.GlobalClass.manager;
import static com.example.cpen321tutorial1.GlobalClass.myAccount;
import static com.example.cpen321tutorial1.JsonFunctions.NewCallPost;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import okhttp3.FormBody;
import okhttp3.Request;
import okhttp3.RequestBody;

public class EditGymsProfile
        extends AppCompatActivity {

    private TextView Name;

    private TextView Location;

    private TextView Phone;

    private TextView Description;

    private Button Done;

    private Button Cancel;

    final static String TAG = "Gym Edit";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_gyms_profile);
        initWidgets();

        //the manager is the gym's owner and
        // they are the only ones able to edit it

        //TODO: uncomment these later
        Name.setText(myAccount.getMyGym().getName());
        Location.setText(myAccount.getMyGym().getAddress());
        Phone.setText(myAccount.getMyGym().getPhone());
        Description.setText(myAccount.getMyGym().getDescription());

        Done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Account Owner = myAccount;
                //ArrayList<Account> SubscribedUserss = new ArrayList<>();
                String InputName = Name.getText().toString();
                String InputLocation = Location.getText().toString();
                String InputPhone = Phone.getText().toString();
                String InputDescription = Description.getText().toString();

                String Json = "";
                String JsonName = JsonFunctions.JsonName(InputName);
                String JsonDescription = JsonFunctions.JsonDescription(InputDescription);
                String JsonLocation = JsonFunctions.JsonLocation(InputLocation);
                String JsonPhone = JsonFunctions.JsonPhone(InputPhone);

                Json = "{" + JsonName + "," + JsonDescription +
                        "," + JsonLocation + "," + JsonPhone + "}";

                RequestBody body = new FormBody.Builder()
                        .add("name", InputName)
                        .add("description", InputDescription)
                        .add("location", InputLocation)
                        .add("phone", InputPhone)
                        .build();

                Request putUserRequest = new Request.Builder()
                        .url("https://20.172.9.70/gyms/gymId/" +
                                manager.getGymId())
                        .put(body)
                        .build();

                NewCallPost(client, putUserRequest);

                //////////////////////////////////////////////////
                ///Upload the CurrentAccount information into database///
                //////////////////////////////////////////////////

                Toast.makeText(EditGymsProfile.this,
                        "Gym Profile Edit Success",
                        Toast.LENGTH_SHORT).show();

                Intent NewGymsIntent =
                        new Intent(EditGymsProfile.this,
                                PersonalProfileManager.class);

                startActivity(NewGymsIntent);
            }
        });

        Cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void initWidgets() {
        Name = findViewById(R.id.GymName);
        Location = findViewById(R.id.Location);
        Phone = findViewById(R.id.Phone);
        Description = findViewById(R.id.Description);
        Done = findViewById(R.id.Done);
        Cancel = findViewById(R.id.Cancel);
    }
}