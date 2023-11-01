package com.example.cpen321tutorial1;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
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
    }

    private void initWidgets() {
        Confirm = findViewById(R.id.Confirm);
        Cancel = findViewById(R.id.Cancel);
        Username = findViewById(R.id.Username);
    }


}