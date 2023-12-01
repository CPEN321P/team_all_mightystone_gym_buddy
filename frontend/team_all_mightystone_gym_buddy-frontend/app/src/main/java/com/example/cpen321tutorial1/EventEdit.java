package com.example.cpen321tutorial1;

import static com.example.cpen321tutorial1.Event.eventsForDate;
import static com.example.cpen321tutorial1.GlobalClass.MyeventsList;
import static com.example.cpen321tutorial1.GlobalClass.client;
import static com.example.cpen321tutorial1.GlobalClass.myAccount;
import static com.example.cpen321tutorial1.JsonFunctions.ConvertEventArrayListToJson;
import static com.example.cpen321tutorial1.JsonFunctions.DateToStringNum;
import static com.example.cpen321tutorial1.JsonFunctions.NewCallPost;
import static com.example.cpen321tutorial1.MainActivity.StringToInteger;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;

public class EventEdit
        extends AppCompatActivity {

    //private EditText eventName;
    //private TextView eventDate;

    //private TextView eventStartTime;

    private TextView HowLong;

    private TextView eventName;



    LocalTime StrTime;

    LocalTime EndTime;

    LocalDate EventDate;

    private Button Done;

    private Button Cancel;

    public static Button dateButton;

    public static Button timeButton;

    int hour;

    int minute;

    final static String TAG = "EventEdit";

    private DatePickerDialog datePickerDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_edit);
        initWidgets();
        initDatePicker();
        dateButton.setText(getTodaysDate());

        dateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                datePickerDialog.show();
            }
        });

        timeButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                TimePickerDialog.OnTimeSetListener onTimeSetListener = new TimePickerDialog.OnTimeSetListener(){
                    public void onTimeSet(TimePicker timePocker, int selectedHour, int selectedMinute){
                        hour = selectedHour;
                        minute = selectedMinute;
                        timeButton.setText(String.format(Locale.getDefault(), "%02d:%02d", hour, minute));
                    }
                };

                int style = AlertDialog.THEME_HOLO_LIGHT;

                TimePickerDialog timePickerDialog = new TimePickerDialog(EventEdit.this, style, onTimeSetListener, hour, minute, true);

                timePickerDialog.setTitle("Select Time");
                timePickerDialog.show();
            }
        });

        Cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        Done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String StartTimeInString =
                        timeButton.getText().toString() + ":00";
                int NumberOfHour =
                        StringToInteger(HowLong.getText().toString());
                if (NumberOfHour <= 0 || NumberOfHour >= 24)
                {
                    Toast.makeText(EventEdit.this,
                            "Invalid Number Of Hours",
                            Toast.LENGTH_SHORT).show();
                    return;
                }
                try {
                    StrTime = LocalTime.parse(StartTimeInString);
                    EndTime = StrTime.plusHours(NumberOfHour);
                    EventDate = LocalDate.parse(dateButton.getText().toString());
                    int value1 = EndTime.compareTo(LocalTime.parse("00:00:00"));
                    //Compare the time to see is it excess 24:00
                    int value2 = EndTime.compareTo(StrTime);
                    //Compare the time to see is it excess 24:00
                    if((value2 < 0 && value1 > 0)){
                        Toast.makeText(EventEdit.this,
                                "Invalid time for Start Time or End Time!",
                                Toast.LENGTH_SHORT).show();
                        return;
                    }

                } catch (DateTimeParseException | NullPointerException e) {
                    Toast.makeText(EventEdit.this,
                            "Invalid time for Start Time or End Time!",
                            Toast.LENGTH_SHORT).show();
                    return;
                }

                for (Event event: MyeventsList){
                    if(event.getDate().equals(EventDate) &&
                            !(StrTime.compareTo(event.getEndtime()) >= 0 ||
                                    EndTime.compareTo(event.getStartTime()) <= 0)) {

                        Toast.makeText(EventEdit.this,
                                "TimeConflict!",
                                Toast.LENGTH_SHORT).show();
                        return;
                    }
                }

                Event newEvent = new Event(myAccount.getUsername() +
                        ": " + eventName.getText().toString(),
                        EventDate, StrTime, EndTime);
                newEvent.setUserId(myAccount.getUserId());
                MyeventsList.add(newEvent);



                String JsonUserId =
                        JsonFunctions.JsonUserId(myAccount.getUserId());
                String JsonDate =
                        JsonFunctions.JsonDate(EventDate);

                String JsonEventName =
                        JsonFunctions.JsonName(myAccount.getUsername() + ": " +
                                eventName.getText().toString());

                String JsonEventWeight = JsonFunctions.JsonWeightEvent("");
                String JsonEventSets = JsonFunctions.JsonSets(0);
                String JsonEventReps = JsonFunctions.JsonReps(0);
                String JsonEventTimeStart =
                        JsonFunctions.JsonStartTime(StrTime);
                String JsonEventTimeEnd = JsonFunctions.JsonEndTime(EndTime);
                String JsonEvent = JsonFunctions.JsonEvent
                        (JsonEventName, JsonEventWeight,
                                JsonEventSets, JsonEventReps,
                                JsonEventTimeStart, JsonEventTimeEnd);

                String JsonSchedule = JsonFunctions.JsonSchedule(JsonEvent);

                if(!checkIfSingleEventsExists(EventDate)){

                    String Json = "{" +  JsonUserId + "," +
                            JsonDate + "," + JsonSchedule + "}";

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

                    String DateString = DateToStringNum(EventDate);

                    RequestBody body = RequestBody.create(Json,
                            MediaType.parse("application/json"));

                    Request requestName = new Request.Builder()
                            .url("https://20.172.9.70/schedules/byUser/" +
                                    myAccount.getUserId() + "/" + DateString)
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

    public static String getTodaysDate() {
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        month = month + 1;
        int day = cal.get(Calendar.DAY_OF_MONTH);
        return makeDateStringFromal(day, month, year);
    }

    private void initWidgets(){
        Done = findViewById(R.id.Done);
        Cancel = findViewById(R.id.CancelAddEvent);
        //eventDate = findViewById(R.id.Date);
        eventName = findViewById(R.id.Event);
        //eventStartTime = findViewById(R.id.StartTime);
        HowLong = findViewById(R.id.HowLong);
        dateButton = findViewById(R.id.Date);
        timeButton = findViewById(R.id.StartTime);
    }

    private void initDatePicker(){
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month + 1;
                String date = makeDateStringFromal(day, month, year);
                dateButton.setText(date);
            }
        };

        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);

        int style = AlertDialog.THEME_HOLO_LIGHT;
        datePickerDialog = new DatePickerDialog(this, style, dateSetListener, year, month, day);
    }

    public static String makeDateStringFromal(int day, int month, int year) {
        return year + "-" + ConvertNumFormal(month) + "-" + ConvertNumFormal(day);
    }

    public static String ConvertNumFormal (int num)
    {
        if (num == 1)
            return "01";
        else if (num == 2)
            return "02";
        else if (num == 3)
            return "03";
        else if (num == 4)
            return "04";
        else if (num == 5)
            return "05";
        else if (num == 6)
            return "06";
        else if (num == 7)
            return "07";
        else if (num == 8)
            return "08";
        else if (num == 9)
            return "09";
        else
            return Integer.toString(num);

    }

    /*
    private String getMonthFormat(int month) {
        if (month == 1)
            return "JAN";
        if (month == 2)
            return "FEB";
        if (month == 3)
            return "MAR";
        if (month == 4)
            return "APR";
        if (month == 5)
            return "MAY";
        if (month == 6)
            return "JUN";
        if (month == 7)
            return "JUL";
        if (month == 8)
            return "AUG";
        if (month == 9)
            return "SEP";
        if (month == 10)
            return "OCT";
        if (month == 12)
            return "NOV";
        if (month == 13)
            return "DEC";

        return "NON";
    }

     */


    private boolean checkIfSingleEventsExists(LocalDate Today) {
        ConnectionToBackend c = new ConnectionToBackend();
        ArrayList<Event> TheEventsofThisAccount =
                c.getScheduleByUser(myAccount.getUserId(), Today);

        return TheEventsofThisAccount != null;
    }



}