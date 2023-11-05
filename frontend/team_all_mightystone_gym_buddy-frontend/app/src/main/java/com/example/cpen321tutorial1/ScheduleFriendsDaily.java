package com.example.cpen321tutorial1;

import static com.example.cpen321tutorial1.CalendarUtils.selectedDate;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.time.LocalTime;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.Locale;

public class ScheduleFriendsDaily extends AppCompatActivity {

    private TextView MonthDayText, DayOfWeek;

    private ListView HoureventList;

    private Button PreviousDay, NextDay, Monthly, Weekly;

    final static String TAG = "Daily";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule_friends_daily);
        initWidgets();

        PreviousDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CalendarUtils.selectedDate =
                        CalendarUtils.selectedDate.minusDays(1);
                setDayView();
            }
        });

        NextDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CalendarUtils.selectedDate =
                        CalendarUtils.selectedDate.plusDays(1);
                setDayView();
            }
        });

        Monthly.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent WeeklyIntent =
                        new Intent(ScheduleFriendsDaily.this, ScheduleFriendsMonthly.class);
                startActivity(WeeklyIntent);
            }
        });

        Weekly.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent WeeklyIntent =
                        new Intent(ScheduleFriendsDaily.this, ScheduleFriendsWeekly.class);
                startActivity(WeeklyIntent);
            }
        });
    }

    private void initWidgets() {
        MonthDayText = findViewById(R.id.Day);
        DayOfWeek = findViewById(R.id.dayOfWeek);
        HoureventList = findViewById(R.id.hourEventList);;
        PreviousDay = findViewById(R.id.PreviousDayAction);
        NextDay = findViewById(R.id.NextDayAction);

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
        HourAdapter hourAdapter =
                new HourAdapter(getApplicationContext(), hourEventList());
        HoureventList.setAdapter(hourAdapter);
    }

    private ArrayList<HourEvent> hourEventList() {
        ArrayList<HourEvent> list = new ArrayList<>();
        ArrayList<Event> FriendsEvent = PersonalProfileFriend.FriendsEvent;
        for (int HalfHour = 0; HalfHour < 48; HalfHour++) {
            LocalTime Time = LocalTime.of(HalfHour/2, HalfHour%2 * 30);
            ArrayList<Event> events =
                    Event.eventsForDateAndTimeOthers(selectedDate, Time, FriendsEvent);
            HourEvent hourEvent = new HourEvent(Time, events);
            list.add(hourEvent);
        }

        return list;
    }

}