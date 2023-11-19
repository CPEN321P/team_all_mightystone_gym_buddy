package com.example.cpen321tutorial1;

import static com.example.cpen321tutorial1.CalendarUtils.selectedDate;
import static com.example.cpen321tutorial1.Event.CleareventsForDate;
import static com.example.cpen321tutorial1.Event.eventsForDate;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.time.LocalTime;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.Locale;

public class ScheduleDaily extends AppCompatActivity {

    private TextView MonthDayText;

    private TextView DayOfWeek;

    private ListView HoureventList;

    private Button PreviousDay;

    private Button NextDay;

    private Button Monthly;

    private Button Weekly;

    private Button NewEvent;

    private Button ClearEvents;

    private Button Cancel;

    final static String TAG = "Daily";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule_daily);
        initWidgets();

        PreviousDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectedDate = selectedDate.minusDays(1);
                setDayView();
            }
        });

        NextDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectedDate = selectedDate.plusDays(1);
                setDayView();
            }
        });

        Monthly.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent WeeklyIntent =
                        new Intent(ScheduleDaily.this, ScheduleMonthly.class);
                startActivity(WeeklyIntent);
            }
        });

        Weekly.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent WeeklyIntent =
                        new Intent(ScheduleDaily.this, ScheduleWeekly.class);
                startActivity(WeeklyIntent);
            }
        });

        NewEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent WeeklyIntent =
                        new Intent(ScheduleDaily.this, EventEdit.class);
                startActivity(WeeklyIntent);
            }
        });

        ClearEvents.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int EventNumber = eventsForDate(selectedDate).size();
                if (EventNumber == 0) {
                    Toast.makeText(ScheduleDaily.this,
                            "No Event For Today!",
                            Toast.LENGTH_SHORT).show();
                    return;
                }
                CleareventsForDate(selectedDate);
                setHourAdapter();
            }
        });

        Cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent DailyIntent = new Intent
                        (ScheduleDaily.this, Logo.class);
                startActivity(DailyIntent);
            }
        });
    }

    private void initWidgets() {
        MonthDayText = findViewById(R.id.Day);
        DayOfWeek = findViewById(R.id.dayOfWeek);
        HoureventList = findViewById(R.id.hourEventList);;
        PreviousDay = findViewById(R.id.PreviousDayAction);
        NextDay = findViewById(R.id.NextDayAction);
        NewEvent = findViewById(R.id.AddEvent);
        ClearEvents = findViewById(R.id.ClearEvent);
        Cancel = findViewById(R.id.Cancel);
        Monthly = findViewById(R.id.Monthly);
        Weekly = findViewById(R.id.Weekly);
    }

    @Override
    protected void onResume() {
        super.onResume();
        setDayView();
    }

    private void setDayView() {
        MonthDayText.setText(CalendarUtils.monthDayFromDate(selectedDate));
        String dayOfWeek = selectedDate.getDayOfWeek().
                        getDisplayName(TextStyle.FULL, Locale.getDefault());
        DayOfWeek.setText(dayOfWeek);

        setHourAdapter();

    }

    private void setHourAdapter() {
        HourAdapter hourAdapter =
                new HourAdapter(getApplicationContext(), hourEventList());
        HoureventList.setAdapter(hourAdapter);
    }

    private ArrayList<HourEvent> hourEventList() {
        ArrayList<HourEvent> list = new ArrayList<>();
        for (int HalfHour = 0; HalfHour < 48; HalfHour++) {
            LocalTime Time = LocalTime.of(HalfHour/2, HalfHour%2 * 30);
            ArrayList<Event> events =
                    Event.eventsForDateAndTime(selectedDate, Time);
            HourEvent hourEvent = new HourEvent(Time, events);
            list.add(hourEvent);
        }

        return list;
    }

}