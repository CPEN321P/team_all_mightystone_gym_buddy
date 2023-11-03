package com.example.cpen321tutorial1;

import static com.example.cpen321tutorial1.GlobalClass.MyeventsList;
import static java.time.temporal.ChronoUnit.MINUTES;

import android.util.Log;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;

public class Event {

    final static String TAG = "Event";
    public static ArrayList<Event> eventsForDate(LocalDate date)
    {
        ArrayList<Event> events = new ArrayList<>();

        for(Event event: MyeventsList)
        {
            if(event.getDate().equals(date))
                events.add(event);
        }

        return events;
    }

    public static ArrayList<Event> eventsForDateOthers(LocalDate date, ArrayList<Event> OtherEventArray)
    {
        ArrayList<Event> events = new ArrayList<>();

        for(Event event: OtherEventArray)
        {
            if(event.getDate().equals(date))
                events.add(event);
        }

        return events;
    }

    public static ArrayList<Event> eventsForDateAndTime(LocalDate date, LocalTime StartTime)
    {
        ArrayList<Event> events = new ArrayList<>();

        for(Event event: MyeventsList)
        {

            int eventMinsStart = round(event.StartTime.getHour()*60 + event.StartTime.getMinute(), 30);
            int eventMinsEnd = round(event.EndTime.getHour()*60 + event.EndTime.getMinute(), 30);
            int cellMins = StartTime.getHour()*60 + StartTime.getMinute();


            if(event.getDate().equals(date))
                if(eventMinsStart <= cellMins && cellMins < eventMinsEnd)
                    events.add(event);
        }

        return events;
    }

    //For the comparsion between the user and their friend
    public static ArrayList<Event> eventsForDateAndTimeOthers(LocalDate date, LocalTime StartTime, ArrayList<Event> OtherEventArray)
    {
        ArrayList<Event> events = new ArrayList<>();

        for(Event event: MyeventsList)
        {

            int eventMinsStart = round(event.StartTime.getHour()*60 + event.StartTime.getMinute(), 30);
            int eventMinsEnd = round(event.EndTime.getHour()*60 + event.EndTime.getMinute(), 30);
            int cellMins = StartTime.getHour()*60 + StartTime.getMinute();

            if(event.getDate().equals(date))
                if(eventMinsStart <= cellMins && cellMins < eventMinsEnd)
                    events.add(event);
        }

        for(Event event: OtherEventArray)
        {
            int eventMinsStart = round(event.StartTime.getHour()*60 + event.StartTime.getMinute(), 30);
            int eventMinsEnd = round(event.EndTime.getHour()*60 + event.EndTime.getMinute(), 30);
            int cellMins = StartTime.getHour()*60 + StartTime.getMinute();


            if(event.getDate().equals(date))
                if(eventMinsStart <= cellMins && cellMins < eventMinsEnd)
                    events.add(event);
        }

        return events;
    }

    public static int round(double value, int nearest) {       //Round the mins of current into the multiple of 30
        return (int) Math.round(value / nearest) * nearest;
    }

    public static void CleareventsForDate(LocalDate date)      //Clear the last event of the specific date
    {

        int count = MyeventsList.size()-1;
        for(Event event: MyeventsList)
        {
            //.d(TAG, Integer.toString(count));
            if(event.getDate().equals(date)) {
                MyeventsList.remove(count);
                break;
            }
            count--;
        }
    }


    private String name;
    private LocalDate date;
    private LocalTime StartTime;
    private LocalTime EndTime;
    private String UserId;

    public LocalTime getEndTime() {
        return EndTime;
    }

    public void setEndTime(LocalTime endTime) {
        EndTime = endTime;
    }

    public String getUserId() {
        return UserId;
    }

    public void setUserId(String userId) {
        UserId = userId;
    }

    public Event(String name, LocalDate date, LocalTime startTime, LocalTime endtime) {
        this.name = name;
        this.date = date;
        StartTime = startTime;
        EndTime = endtime;
        UserId = "";
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public LocalTime getStartTime() {
        return StartTime;
    }

    public void setStartTime(LocalTime startTime) {
        StartTime = startTime;
    }

    public LocalTime getEndtime() {
        return EndTime;
    }

    public void setEndtime(LocalTime endtime) {
        EndTime = endtime;
    }
}
