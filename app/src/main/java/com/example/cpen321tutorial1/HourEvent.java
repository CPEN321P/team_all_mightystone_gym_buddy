package com.example.cpen321tutorial1;

import java.time.LocalTime;
import java.util.ArrayList;

public class HourEvent {
    LocalTime StartTime;
    LocalTime EndTime;
    ArrayList<Event> events;

    public HourEvent(LocalTime startTime, LocalTime endTime, ArrayList<Event> events) {
        StartTime = startTime;
        EndTime = endTime;
        this.events = events;
    }

    public LocalTime getStartTime() {
        return StartTime;
    }

    public void setStartTime(LocalTime startTime) {
        StartTime = startTime;
    }

    public LocalTime getEndTime() {
        return EndTime;
    }

    public void setEndTime(LocalTime endTime) {
        EndTime = endTime;
    }

    public ArrayList<Event> getEvents() {
        return events;
    }

    public void setEvents(ArrayList<Event> events) {
        this.events = events;
    }
}
