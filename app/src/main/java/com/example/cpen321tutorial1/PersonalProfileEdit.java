package com.example.cpen321tutorial1;

import static com.example.cpen321tutorial1.MainActivity.StringToInteger;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class PersonalProfileEdit extends AppCompatActivity {

    private TextView UserName, Age, Weight;
    private Spinner GenderSpinner;
    private Button Done;
    private Button Cancel;
    final static String TAG = "PersonalProfileEdit";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_profile_edition_user);
        initWidgets();


        UserName.setText(Account.CurrentAccount.get(0).getUsername());
        Age.setText(Integer.toString(Account.CurrentAccount.get(0).getAge()));
        Weight.setText(Integer.toString(Account.CurrentAccount.get(0).getWeight()));

        Log.d(TAG, Account.CurrentAccount.get(0).getUsername());
        Log.d(TAG, Integer.toString(Account.CurrentAccount.get(0).getAge()));
        Log.d(TAG, Integer.toString(Account.CurrentAccount.get(0).getWeight()));
        Log.d(TAG, Account.CurrentAccount.get(0).getGender());

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.roles, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        GenderSpinner.setAdapter(adapter);

        Done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (StringToInteger(Age.getText().toString())<= 0 || StringToInteger(Age.getText().toString()) >= 150){
                    Toast.makeText(PersonalProfileEdit.this, "Invalid Age", Toast.LENGTH_LONG).show();
                    return;
                }
                if (StringToInteger(Weight.getText().toString()) <= 0)
                {
                    Toast.makeText(PersonalProfileEdit.this, "Invalid Weight", Toast.LENGTH_LONG).show();
                    return;
                }
                if (GenderSpinner.getSelectedItem().toString().equals("Select Your Gender")) {
                    Toast.makeText(PersonalProfileEdit.this, "Please Select the Gender", Toast.LENGTH_SHORT).show();
                    return;
                }

                Account CurrentAccount = new Account(UserName.getText().toString(), Account.CurrentAccount.get(0).getEmailAddress(), StringToInteger(Age.getText().toString()),
                        StringToInteger(Weight.getText().toString()), GenderSpinner.getSelectedItem().toString(), Account.CurrentAccount.get(0).getRole());
                Account.CurrentAccount.clear();
                Account.CurrentAccount.add(CurrentAccount);

                Log.d(TAG, "UserName: " + UserName.getText().toString());
                Log.d(TAG, "Age: " + Integer.parseInt(Age.getText().toString()));
                Log.d(TAG, "Weight: " + Integer.parseInt(Weight.getText().toString()));
                Log.d(TAG, "Email: " + Account.CurrentAccount.get(0).getEmailAddress());
                Log.d(TAG, "Gender: " + GenderSpinner.getSelectedItem().toString());
                Log.d(TAG, "Role: " + Account.CurrentAccount.get(0).getRole());

                //////////////////////////////////////////////////
                ///Upload the CurrentAccount information into database///
                //////////////////////////////////////////////////
            }
        });

        Cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "Cancel Editing");
                finish();
            }
        });
    }

    private void initWidgets() {
        UserName = findViewById(R.id.username);
        Age = findViewById(R.id.Age);
        Weight = findViewById(R.id.Weight);
        GenderSpinner = findViewById(R.id.planets_spinner);
        Done = findViewById(R.id.Done);
        Cancel = findViewById(R.id.Cancel);
    }
}