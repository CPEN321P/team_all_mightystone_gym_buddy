package com.example.cpen321tutorial1;

import static com.example.cpen321tutorial1.Event.eventsForDate;
import static com.example.cpen321tutorial1.GlobalClass.MyeventsList;
import static com.example.cpen321tutorial1.GlobalClass.client;
import static com.example.cpen321tutorial1.GlobalClass.myAccount;
import static com.example.cpen321tutorial1.JsonFunctions.ConvertEventArrayListToJson;
import static com.example.cpen321tutorial1.JsonFunctions.DateToStringNum;
import static com.example.cpen321tutorial1.JsonFunctions.JsonFriends;
import static com.example.cpen321tutorial1.JsonFunctions.JsonName;
import static com.example.cpen321tutorial1.JsonFunctions.JsonUserId;
import static com.example.cpen321tutorial1.JsonFunctions.NewCallPost;
import static com.example.cpen321tutorial1.MainActivity.StringToInteger;

import androidx.appcompat.app.AppCompatActivity;

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
import java.util.ArrayList;

import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;

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
                    int value1 = EndTime.compareTo(LocalTime.parse("00:00:00"));
                    //Compare the time to see is it excess 24:00
                    int value2 = EndTime.compareTo(StrTime);
                    //Compare the time to see is it excess 24:00
                    Log.d(TAG, "Valid time starting: " + StrTime);
                    Log.d(TAG, "Valid time ending: " + EndTime);
                    Log.d(TAG, "Date: " + EventDate);
                    if((value2 < 0 && value1 > 0) || NumberOfHour >= 24){
                        Toast.makeText(EventEdit.this, "Invalid time for Start Time or End Time!", Toast.LENGTH_LONG).show();
                        return;
                    }

                } catch (DateTimeParseException | NullPointerException e) {
                    Log.d(TAG, "Invalid time string for Start Time, End Time or Date!");
                    Toast.makeText(EventEdit.this,
                            "Invalid time for Start Time or End Time!",
                            Toast.LENGTH_LONG).show();
                    return;
                }

                for (Event event: MyeventsList){
                    if(event.getDate().equals(EventDate)) {
                        if(!(StrTime.compareTo(event.getEndtime()) >= 0 ||
                                EndTime.compareTo(event.getStartTime()) <= 0)){
                            Toast.makeText(EventEdit.this, "TimeConflict!", Toast.LENGTH_LONG).show();
                            return;
                        }
                    }
                }

                Event newEvent = new Event(myAccount.getUsername() + ": " + eventName.getText().toString(), EventDate, StrTime, EndTime);
                newEvent.setUserId(myAccount.getUserId());
                MyeventsList.add(newEvent);



                String JsonUserId = JsonFunctions.JsonUserId(myAccount.getUserId());
                String JsonDate = JsonFunctions.JsonDate(EventDate);

                String JsonEventName = JsonFunctions.JsonName(myAccount.getUsername() + ": " + eventName.getText().toString());
                String JsonEventWeight = JsonFunctions.JsonWeightEvent("");
                String JsonEventSets = JsonFunctions.JsonSets(0);
                String JsonEventReps = JsonFunctions.JsonReps(0);
                String JsonEventTimeStart = JsonFunctions.JsonStartTime(StrTime);
                String JsonEventTimeEnd = JsonFunctions.JsonEndTime(EndTime);
                String JsonEvent = JsonFunctions.JsonEvent(JsonEventName, JsonEventWeight, JsonEventSets, JsonEventReps, JsonEventTimeStart, JsonEventTimeEnd);

                String JsonSchedule = JsonFunctions.JsonSchedule(JsonEvent);

                if(!checkIfSingleEventsExists(EventDate)){
                    String Json = "{" +  JsonUserId + "," + JsonDate + "," + JsonSchedule + "}";
                    Log.d(TAG, Json);
                    RequestBody body = RequestBody.create(Json,
                            MediaType.parse("application/json"));

                    Request requestName = new Request.Builder()
                            .url("https://20.172.9.70/schedules")
                            .post(body)
                            .build();

                    NewCallPost(client, requestName);
                }
                else{
                    ArrayList<Event> TodaysEvent = eventsForDate(EventDate);
                    String Json = ConvertEventArrayListToJson
                            (TodaysEvent, myAccount.getUserId(), EventDate);
                    Log.d(TAG + " add", Json);
                    Log.d(TAG + "1", Integer.toString(MyeventsList.size()));

                    String DateString = DateToStringNum(EventDate);

                    RequestBody body = RequestBody.create(Json,
                            MediaType.parse("application/json"));

                    Request requestName = new Request.Builder()
                            .url("https://20.172.9.70/schedules/byUser/" + myAccount.getUserId() + "/" + DateString)
                            .put(body)
                            .build();

                    NewCallPost(client, requestName);
                }

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

    private boolean checkIfSingleEventsExists(LocalDate Today) {
        ConnectionToBackend c = new ConnectionToBackend();
        ArrayList<Event> TheEventsofThisAccount = c.getScheduleByUserAndDate(myAccount.getUserId(), Today);
        if(TheEventsofThisAccount == null){
            Log.d("THIS IS", "FALSE BRO");
            return false;
        }
        Log.d("THIS IS", "TRUE");
        return true;

    }



}