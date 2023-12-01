package com.example.cpen321tutorial1;

import java.util.List;

public class Manager {

    private String _id;

    private String name;

    private String username;

    private String email;

    private String gymId;

    private List<Announcement> Announcements;

    public List<Announcement> getAnnouncements() {
        return Announcements;
    }

    public void setAnnouncements(List<Announcement> announcements) {
        Announcements = announcements;
    }

    public String getGymId() {
        return gymId;
    }

    public void setGymId(String gymId) {
        this.gymId = gymId;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
