package com.example.cpen321tutorial1;

import java.util.ArrayList;

public class Gym {

    public static ArrayList<Gym> CurrentGym = new ArrayList<>();
    private Account Owner;
    private ArrayList<Account> SubscribedUsers;
    private String GymName;
    private String Location;
    private String AccessTime;
    private String Website;
    private String Tips;

    public Gym(Account owner, ArrayList<Account> subscribedUsers, String gymName, String location, String accessTime, String website, String tips) {
        Owner = owner;
        SubscribedUsers = subscribedUsers;
        GymName = gymName;
        Location = location;
        AccessTime = accessTime;
        Website = website;
        Tips = tips;
    }

    public Account getOwner() {
        return Owner;
    }

    public void setOwner(Account owner) {
        Owner = owner;
    }

    public ArrayList<Account> getSubscribedUsers() {
        return SubscribedUsers;
    }

    public void setSubscribedUsers(ArrayList<Account> SubscribedUsers) {
        SubscribedUsers = SubscribedUsers;
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
