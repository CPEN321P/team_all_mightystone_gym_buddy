package com.example.cpen321tutorial1;

import static com.example.cpen321tutorial1.CalendarUtils.daysInWeekArray;
import static com.example.cpen321tutorial1.CalendarUtils.monthYearFromDate;
import static com.example.cpen321tutorial1.CalendarUtils.selectedDate;
import static com.example.cpen321tutorial1.Event.eventsForDate;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.time.LocalDate;
import java.util.ArrayList;

public class ScheduleWeekly
        extends AppCompatActivity
        implements CalendarAdapter.OnItemListener{

    private TextView monthYearText;

    private RecyclerView calendarRecyclerView;

    private ListView eventList;

    private Button PreviousWeek;

    private Button NextWeek;

    private Button MonthlyModel;

    private Button DailyModel;

    private Button NewEvent;

    private Button ClearEvents;

    private Button Cancel;

    final static String TAG = "WeekView";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule_weekly);
        initinalWidgets();
        setWeekView();



        PreviousWeek.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectedDate =
                        CalendarUtils.selectedDate.minusWeeks(1);
                setWeekView();
            }
        });

        NextWeek.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CalendarUtils.selectedDate =
                        CalendarUtils.selectedDate.plusWeeks(1);
                setWeekView();
            }
        });

        NewEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent WeeklyIntent =
                        new Intent(ScheduleWeekly.this, EventEdit.class);
                startActivity(WeeklyIntent);
            }
        });


        MonthlyModel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent EventIntent =
                        new Intent(ScheduleWeekly.this, ScheduleMonthly.class);
                startActivity(EventIntent);
            }
        });

        DailyModel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent DailyIntent = new Intent
                        (ScheduleWeekly.this, ScheduleDaily.class);
                startActivity(DailyIntent);
            }
        });

        ClearEvents.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int EventNumber = eventsForDate(selectedDate).size();
                if (EventNumber == 0) {
                    Toast.makeText(ScheduleWeekly.this,
                            "No Event For Today!",
                            Toast.LENGTH_SHORT).show();
                    return;
                }
                Event.CleareventsForDate(selectedDate);
                setEventAdapter();
            }
        });

        Cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent DailyIntent = new Intent
                        (ScheduleWeekly.this, Logo.class);
                startActivity(DailyIntent);
            }
        });
    }

    private void setWeekView() {
        monthYearText.setText(monthYearFromDate(selectedDate));
        ArrayList<LocalDate> days = daysInWeekArray(selectedDate);
        CalendarAdapter calendarAdapter =
                new CalendarAdapter(days, this);
        RecyclerView.LayoutManager layoutManager =
                new GridLayoutManager(getApplicationContext(), 7);

        calendarRecyclerView.setLayoutManager(layoutManager);
        calendarRecyclerView.setAdapter(calendarAdapter);
        setEventAdapter();

    }

    private void initinalWidgets() {
        calendarRecyclerView = findViewById(R.id.calendarRecyclerView);
        monthYearText = findViewById(R.id.WeekDay);
        eventList = findViewById(R.id.eventList);
        PreviousWeek = findViewById(R.id.PreviousWeekAction);
        NextWeek = findViewById(R.id.NextWeekAction);
        NewEvent = findViewById(R.id.AddEvent);
        ClearEvents = findViewById(R.id.ClearEvent);
        MonthlyModel = findViewById(R.id.Weekly);
        DailyModel = findViewById(R.id.Daily);
        Cancel = findViewById(R.id.Cancel);
    }

    @Override
    public void onItemClick(int position, LocalDate date) {
        selectedDate = date;
        setWeekView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setEventAdapter();
    }

    private void setEventAdapter() {
        ArrayList<Event> dailyEvents =
                Event.eventsForDate(selectedDate);
        EventAdapter eventAdapter =
                new EventAdapter(getApplicationContext(), dailyEvents);
        eventList.setAdapter(eventAdapter);
    }
}