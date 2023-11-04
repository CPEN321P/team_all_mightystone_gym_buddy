package com.example.cpen321tutorial1;

import static com.example.cpen321tutorial1.CalendarUtils.daysInMonthArray;
import static com.example.cpen321tutorial1.CalendarUtils.monthYearFromDate;
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

//The Implementation of Schedule is base on the tutorial from Youtube
//https://www.youtube.com/watch?v=Ba0Q-cK1fJo&list=LL&index=6
//https://www.youtube.com/watch?v=knpSbtbPz3o&list=LL&index=4&t=541s
//https://www.youtube.com/watch?v=Aig99t-gNqM&list=LL&index=6&t=238s
//Thanks for the support from @Code With Cal

public class MonthlySchedule extends AppCompatActivity implements CalendarAdapter.OnItemListener{

    private TextView monthYearText;
    private RecyclerView calendarRecyclerView;
    private Button PreviousMonth;
    private Button NextMonth;
    private Button Weekly;
    private Button Daily;
    final static String TAG = "Schedule";

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weekly_schedule);
        initinalWidgets();
        CalendarUtils.selectedDate = LocalDate.now();
        setMonthView();

        if(TestComeFromOutsideOrNot == 1){
            Intent WeeklyIntent = new Intent(MonthlySchedule.this, WeekView.class);
            startActivity(WeeklyIntent);
            TestComeFromOutsideOrNot = 0;
        }


        PreviousMonth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CalendarUtils.selectedDate = CalendarUtils.selectedDate.minusMonths(1);
                setMonthView();
            }
        });

        NextMonth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CalendarUtils.selectedDate = CalendarUtils.selectedDate.plusMonths(1);
                setMonthView();
            }
        });


        Weekly.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent WeeklyIntent = new Intent(MonthlySchedule.this, WeekView.class);
                startActivity(WeeklyIntent);
            }
        });

        Daily.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent DailyIntent = new Intent(MonthlySchedule.this, DailyCalendar.class);
                startActivity(DailyIntent);
            }
        });

    }

    private void setMonthView() {
        monthYearText.setText(monthYearFromDate(CalendarUtils.selectedDate));
        ArrayList<LocalDate> daysInMonth = daysInMonthArray();

        CalendarAdapter calendarAdapter = new CalendarAdapter(daysInMonth, this);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getApplicationContext(), 7);
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
            CalendarUtils.selectedDate = date;
            setMonthView();
        }
    }
}