package com.example.cpen321tutorial1;

import android.util.Log;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;

public class Event {

    final static String TAG = "Event";
    public static ArrayList<Event> eventsList = new ArrayList<>();
    public static ArrayList<Event> eventsForDate(LocalDate date)
    {
        ArrayList<Event> events = new ArrayList<>();

        for(Event event: eventsList)
        {
            if(event.getDate().equals(date))
                events.add(event);
        }

        return events;
    }

    public static void CleareventsForDate(LocalDate date)
    {
        ArrayList<Event> events = new ArrayList<>();

        int count = 0;
        for(Event event: eventsList)
        {
            Log.d(TAG, Integer.toString(count));
            if(event.getDate().equals(date))
                eventsList.remove(count);
            count++;
        }
        Log.d(TAG, "TEST2");
    }


    private String name;
    private LocalDate date;
    private LocalTime Starttime;
    private LocalTime Endtime;

    public Event(String name, LocalDate date, LocalTime starttime, LocalTime endtime) {
        this.name = name;
        this.date = date;
        Starttime = starttime;
        Endtime = endtime;
    }

    public static ArrayList<Event> getEventsList() {
        return eventsList;
    }

    public static void setEventsList(ArrayList<Event> eventsList) {
        Event.eventsList = eventsList;
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

    public LocalTime getStarttime() {
        return Starttime;
    }

    public void setStarttime(LocalTime starttime) {
        Starttime = starttime;
    }

    public LocalTime getEndtime() {
        return Endtime;
    }

    public void setEndtime(LocalTime endtime) {
        Endtime = endtime;
    }
}
