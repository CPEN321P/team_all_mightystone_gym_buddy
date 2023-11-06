package com.example.cpen321tutorial1;

import static com.example.cpen321tutorial1.CalendarUtils.daysInMonthArray;
import static com.example.cpen321tutorial1.CalendarUtils.monthYearFromDate;
import static com.example.cpen321tutorial1.CalendarUtils.selectedDate;
import static com.example.cpen321tutorial1.MainActivity.TestComeFromOutsideOrNot;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.time.LocalDate;
import java.util.ArrayList;

public class ScheduleFriendsMonthly
        extends AppCompatActivity
        implements CalendarAdapter.OnItemListener{

    private TextView monthYearText;

    private RecyclerView calendarRecyclerView;

    private Button PreviousMonth;

    private Button NextMonth;

    private Button Weekly;

    private Button Daily;

    final static String TAG = "ScheduleFriendsMonthly";

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule_friends_monthly);
        initinalWidgets();
        selectedDate = LocalDate.now();
        setMonthView();

        if(TestComeFromOutsideOrNot == 1){

            Intent WeeklyIntent =
                    new Intent(ScheduleFriendsMonthly.this,
                            ScheduleFriendsWeekly.class);

            startActivity(WeeklyIntent);
            TestComeFromOutsideOrNot = 0;
        }


        PreviousMonth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectedDate = selectedDate.minusMonths(1);
                setMonthView();
            }
        });

        NextMonth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectedDate = selectedDate.plusMonths(1);
                setMonthView();
            }
        });


        Weekly.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent WeeklyIntent =
                        new Intent(ScheduleFriendsMonthly.this,
                                ScheduleFriendsWeekly.class);
                startActivity(WeeklyIntent);
            }
        });

        Daily.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent DailyIntent =
                        new Intent(ScheduleFriendsMonthly.this,
                                ScheduleFriendsDaily.class);
                startActivity(DailyIntent);
            }
        });

    }

    private void setMonthView() {
        monthYearText.setText(monthYearFromDate(selectedDate));
        ArrayList<LocalDate> daysInMonth = daysInMonthArray();

        CalendarAdapter calendarAdapter =
                new CalendarAdapter(daysInMonth, this);
        RecyclerView.LayoutManager layoutManager =
                new GridLayoutManager(getApplicationContext(), 7);
        calendarRecyclerView.setLayoutManager(layoutManager);
        calendarRecyclerView.setAdapter(calendarAdapter);
    }



    private void initinalWidgets() {
        calendarRecyclerView = findViewById(R.id.calendarRecyclerView);
        monthYearText = findViewById(R.id.WeekDay);
        PreviousMonth = findViewById(R.id.PreviousMonthAction);
        NextMonth = findViewById(R.id.NextMonthAction);
        Weekly = findViewById(R.id.Weekly);
        Daily = findViewById(R.id.Daily);
    }

    @Override
    public void onItemClick(int position, LocalDate date) {
        if(date != null) {
            selectedDate = date;
            setMonthView();
        }
    }
}