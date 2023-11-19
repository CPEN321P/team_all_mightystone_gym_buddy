package com.example.cpen321tutorial1;

import static com.example.cpen321tutorial1.GlobalClass.client;
import static com.example.cpen321tutorial1.GlobalClass.myAccount;
import static com.example.cpen321tutorial1.JsonFunctions.NewCallPost;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;

public class Unblock extends AppCompatActivity {

    Button Confirm;

    Button Cancel;

    TextView Username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_unblock);
        initWidgets();

        Intent i = getIntent();
        String personName = i.getStringExtra("PersonName");
        String personId = i.getStringExtra("PersonId");
        Username.setText(personName);

        Confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RequestBody body = RequestBody.create("{"+ "}",
                        MediaType.parse("application/json"));

                Request unblockUser = new Request.Builder()
                        .url("https://20.172.9.70/users/unblockUser/"
                                + myAccount.getUserId()
                                + "/" + personId)
                        .put(body)
                        .build();
                NewCallPost(client, unblockUser);
                Intent ConfirmIntent =
                        new Intent(Unblock.this, BlockedUsers.class);
                startActivity(ConfirmIntent);
            }
        });

        Cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent CancelIntent =
                        new Intent(Unblock.this, BlockedUsers.class);
                startActivity(CancelIntent);
            }
        });
    }

    private void initWidgets() {
        Confirm = findViewById(R.id.Confirm);
        Cancel = findViewById(R.id.Cancel);
        Username = findViewById(R.id.Username);
    }


}