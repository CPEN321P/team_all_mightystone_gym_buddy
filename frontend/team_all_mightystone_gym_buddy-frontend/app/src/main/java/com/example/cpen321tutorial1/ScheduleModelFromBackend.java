package com.example.cpen321tutorial1;

import java.util.List;

public class ScheduleModelFromBackend {

    private String userId;

    private Long date;

    private List<EventModelFromBackend> exercises;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Long getDate() {
        return date;
    }

    public void setDate(Long date) {
        this.date = date;
    }

    public List<EventModelFromBackend> getExercises() {
        return exercises;
    }

    public void setExercises(List<EventModelFromBackend> exercises) {
        this.exercises = exercises;
    }
}
