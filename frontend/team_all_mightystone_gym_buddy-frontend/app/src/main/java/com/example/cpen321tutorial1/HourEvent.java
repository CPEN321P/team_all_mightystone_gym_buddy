package com.example.cpen321tutorial1;

import java.time.LocalTime;
import java.util.ArrayList;

public class HourEvent {

    LocalTime Time;

    ArrayList<Event> events;

    public HourEvent(LocalTime time, ArrayList<Event> events) {
        Time = time;
        this.events = events;
    }

    public LocalTime getTime() {
        return Time;
    }

    public void setTime(LocalTime time) {
        Time = time;
    }

    public ArrayList<Event> getEvents() {
        return events;
    }

    public void setEvents(ArrayList<Event> events) {
        this.events = events;
    }
}
