package com.example.cpen321tutorial1;

import java.util.ArrayList;

public class Gym {

    private String GymName;
    private String Location;
    private String AccessTime;
    private String Website;
    private String Tips;

    public Gym(){

    }

    public Gym( String gymName, String location, String accessTime, String website, String tips) {

        GymName = gymName;
        Location = location;
        AccessTime = accessTime;
        Website = website;
        Tips = tips;
    }

    public String getGymName() {
        return GymName;
    }

    public void setGymName(String gymName) {
        GymName = gymName;
    }

    public String getLocation() {
        return Location;
    }

    public void setLocation(String location) {
        Location = location;
    }

    public String getAccessTime() {
        return AccessTime;
    }

    public void setAccessTime(String accessTime) {
        AccessTime = accessTime;
    }

    public String getWebsite() {
        return Website;
    }

    public void setWebsite(String website) {
        Website = website;
    }

    public String getTips() {
        return Tips;
    }

    public void setTips(String tips) {
        Tips = tips;
    }
}
