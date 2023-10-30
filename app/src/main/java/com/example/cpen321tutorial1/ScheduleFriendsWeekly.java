package com.example.cpen321tutorial1;

import static com.example.cpen321tutorial1.CalendarUtils.daysInWeekArray;
import static com.example.cpen321tutorial1.CalendarUtils.monthYearFromDate;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.time.LocalDate;
import java.util.ArrayList;

public class ScheduleFriendsWeekly extends AppCompatActivity implements CalendarAdapter.OnItemListener{

    private TextView monthYearText;
    private RecyclerView calendarRecyclerView;
    private ListView eventList;
    private Button PreviousWeek;
    private Button NextWeek;
    private Button MonthlyModel;
    private Button DailyModel;
    final static String TAG = "ScheduleFriendsWeekly";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_week_view);
        initinalWidgets();
        setWeekView();


        PreviousWeek.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CalendarUtils.selectedDate = CalendarUtils.selectedDate.minusWeeks(1);
                setWeekView();
            }
        });

        NextWeek.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CalendarUtils.selectedDate = CalendarUtils.selectedDate.plusWeeks(1);
                setWeekView();
            }
        });


        MonthlyModel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent EventIntent = new Intent(ScheduleFriendsWeekly.this, MonthlySchedule.class);
                startActivity(EventIntent);
            }
        });

        DailyModel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent DailyIntent = new Intent(ScheduleFriendsWeekly.this, DailyCalendar.class);
                startActivity(DailyIntent);
            }
        });
    }

    private void setWeekView() {
        monthYearText.setText(monthYearFromDate(CalendarUtils.selectedDate));
        ArrayList<LocalDate> days = daysInWeekArray(CalendarUtils.selectedDate);

        CalendarAdapter calendarAdapter = new CalendarAdapter(days, this);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getApplicationContext(), 7);
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
        MonthlyModel = findViewById(R.id.Weekly);
        DailyModel = findViewById(R.id.Daily);
    }
    @Override
    public void onItemClick(int position, LocalDate date) {
        CalendarUtils.selectedDate = date;
        setWeekView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setEventAdapter();
    }

    private void setEventAdapter() {
        ArrayList<Event> FriendsEvent = PersonalProfileFriend.FriendsEvent;
        ArrayList<Event> dailyEvents = Event.eventsForDateOthers(CalendarUtils.selectedDate, FriendsEvent);
        EventAdapter eventAdapter = new EventAdapter(getApplicationContext(), dailyEvents);
        eventList.setAdapter(eventAdapter);
    }
}