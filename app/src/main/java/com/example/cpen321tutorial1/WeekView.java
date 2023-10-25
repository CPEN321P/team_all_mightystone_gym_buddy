package com.example.cpen321tutorial1;

import static com.example.cpen321tutorial1.CalendarUtils.daysInMonthArray;
import static com.example.cpen321tutorial1.CalendarUtils.daysInWeekArray;
import static com.example.cpen321tutorial1.CalendarUtils.monthYearFromDate;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.time.LocalDate;
import java.util.ArrayList;

public class WeekView extends AppCompatActivity implements CalendarAdapter.OnItemListener{

    private TextView monthYearText;
    private RecyclerView calendarRecyclerView;
    private Button PreviousWeek;
    private Button NextWeek;
    private Button Model;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_week_view);
        initinalWidgets();
        setWeekView();

        PreviousWeek = findViewById(R.id.PreviousWeekAction);
        NextWeek = findViewById(R.id.NextWeekAction);

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

        Model = findViewById(R.id.Model);
        Model.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent WeeklyIntent = new Intent(WeekView.this, WeeklySchedule.class);
                startActivity(WeeklyIntent);
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
    }




    private void initinalWidgets() {
        calendarRecyclerView = findViewById(R.id.calendarRecyclerView);
        monthYearText = findViewById(R.id.WeekDay);
    }
    @Override
    public void onItemClick(int position, LocalDate date) {
            CalendarUtils.selectedDate = date;
            setWeekView();
    }
}