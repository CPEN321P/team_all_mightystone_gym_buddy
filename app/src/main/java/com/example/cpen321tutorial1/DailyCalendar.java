package com.example.cpen321tutorial1;

import static com.example.cpen321tutorial1.CalendarUtils.selectedDate;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.time.LocalTime;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class DailyCalendar extends AppCompatActivity {

    private TextView MonthDayText;
    private TextView DayOfWeek;
    private ListView HoureventList;
    private Button PreviousDay;
    private Button NextDay;
    private Button Monthly;
    private Button Weekly;
    private Button NewEvent;
    private Button ClearEvents;
    final static String TAG = "Daily";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily_calendar);
        initWidgets();

        PreviousDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CalendarUtils.selectedDate = CalendarUtils.selectedDate.minusDays(1);
                setDayView();
            }
        });

        NextDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CalendarUtils.selectedDate = CalendarUtils.selectedDate.plusDays(1);
                setDayView();
            }
        });

        Monthly.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent WeeklyIntent = new Intent(DailyCalendar.this, MonthlySchedule.class);
                startActivity(WeeklyIntent);
            }
        });

        Weekly.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent WeeklyIntent = new Intent(DailyCalendar.this, WeekView.class);
                startActivity(WeeklyIntent);
            }
        });

        NewEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent WeeklyIntent = new Intent(DailyCalendar.this, EventEdit.class);
                startActivity(WeeklyIntent);
            }
        });

        ClearEvents.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Event.CleareventsForDate(CalendarUtils.selectedDate);

                //////////////////////////////////////////////////////////////////////////////////////////
                ////DELETE the last event in event list of the giving date(CalendarUtils.selectedDate)////
                //////////////////////////////////////////////////////////////////////////////////////////

                setHourAdapter();
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
        String dayOfWeek = selectedDate.getDayOfWeek().getDisplayName(TextStyle.FULL, Locale.getDefault());
        DayOfWeek.setText(dayOfWeek);

        setHourAdapter();

    }

    private void setHourAdapter() {
        HourAdapter hourAdapter = new HourAdapter(getApplicationContext(), hourEventList());
        HoureventList.setAdapter(hourAdapter);
    }

    private ArrayList<HourEvent> hourEventList() {
        ArrayList<HourEvent> list = new ArrayList<>();
        for (int HalfHour = 0; HalfHour < 48; HalfHour++) {
            LocalTime Time = LocalTime.of(HalfHour/2, HalfHour%2 * 30);
            ArrayList<Event> events = Event.eventsForDateAndTime(selectedDate, Time);
            HourEvent hourEvent = new HourEvent(Time, events);
            list.add(hourEvent);
        }

        return list;
    }

}