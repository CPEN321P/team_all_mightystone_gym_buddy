package com.example.cpen321tutorial1;

import static com.example.cpen321tutorial1.MainActivity.StringToInteger;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;

public class EventEdit extends AppCompatActivity {

    //private EditText eventName;
    private TextView eventDate, eventStartTime, HowLong, eventName;
    private LocalTime StrTime;
    private LocalTime EndTime;
    private LocalDate EventDate;
    private Button Done;
    private Button Cancel;
    final static String TAG = "EventEdit";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_edit);
        initWidgets();

        Cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "Trying get Back");
                finish();
            }
        });


        Done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String StartTimeInString = eventStartTime.getText().toString() + ":00";
                int NumberOfHour = StringToInteger(HowLong.getText().toString());
                if (NumberOfHour <= 0)
                {
                    Toast.makeText(EventEdit.this, "Invalid Number Of Hours", Toast.LENGTH_LONG).show();
                    return;
                }
                try {
                    StrTime = LocalTime.parse(StartTimeInString);
                    EndTime = StrTime.plusHours(NumberOfHour);
                    EventDate = LocalDate.parse(eventDate.getText().toString());
                    int value1 = EndTime.compareTo(LocalTime.parse("00:00:00")); //Compare the time to see is it excess 24:00
                    int value2 = EndTime.compareTo(StrTime); //Compare the time to see is it excess 24:00
                    Log.d(TAG, "Valid time starting: " + StrTime);
                    Log.d(TAG, "Valid time ending: " + EndTime);
                    Log.d(TAG, "Date: " + EventDate);
                    if((value2 < 0 && value1 > 0) || NumberOfHour >= 24){
                        Toast.makeText(EventEdit.this, "Invalid time for Start Time or End Time!", Toast.LENGTH_LONG).show();
                        return;
                    }

                } catch (DateTimeParseException | NullPointerException e) {
                    Log.d(TAG, "Invalid time string for Start Time, End Time or Date!");
                    Toast.makeText(EventEdit.this, "Invalid time for Start Time or End Time!", Toast.LENGTH_LONG).show();
                    return;
                }

                Event newEvent = new Event(eventName.getText().toString(), EventDate, StrTime, EndTime);
                Event.eventsList.add(newEvent);

                /////////////////////////////////////////
                //POST the event list to the back end////
                /////////////////////////////////////////
                finish();
            }
        });
    }

    private void initWidgets(){
        Done = findViewById(R.id.Done);
        Cancel = findViewById(R.id.CancelAddEvent);
        eventDate = findViewById(R.id.Date);
        eventName = findViewById(R.id.Event);
        eventStartTime = findViewById(R.id.StartTime);
        HowLong = findViewById(R.id.HowLong);
    }

}