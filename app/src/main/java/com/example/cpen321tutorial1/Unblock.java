package com.example.cpen321tutorial1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class Unblock extends AppCompatActivity {

    Button Confirm, Cancel;
    TextView Username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_unblock);
        initWidgets();

        Confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent ConfirmIntent = new Intent(Unblock.this, BlockedUsers.class);
                startActivity(ConfirmIntent);
            }
        });

        Cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent CancelIntent = new Intent(Unblock.this, BlockedUsers.class);
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