package com.example.cpen321tutorial1;

import java.util.List;

public class ManagerModelFromBackend {

    private String _id;
    private String name;
    private String username;
    private String gymId;
    private String phone;
    private String email;
    private String pfp;
    private String description;
    private Long reported;
    private List<Announcement> announcements;

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

    public String getGymId() {
        return gymId;
    }

    public void setGymId(String gymId) {
        this.gymId = gymId;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPfp() {
        return pfp;
    }

    public void setPfp(String pfp) {
        this.pfp = pfp;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getReported() {
        return reported;
    }

    public void setReported(Long reported) {
        this.reported = reported;
    }

    public List<Announcement> getAnnouncements() {
        return announcements;
    }

    public void setAnnouncements(List<Announcement> announcements) {
        this.announcements = announcements;
    }

    public String toString() {
        return String.format("_id:%d,name:%d,username:%d,gymId:%d,phone:%s,email:%s,pfp:%d,description:%d,reported:%d,announcements:%d", _id, name, username, gymId, phone, email, pfp, description, reported, announcements);
    }
}
