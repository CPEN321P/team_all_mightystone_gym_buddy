package com.example.cpen321tutorial1;

import android.content.Intent;

import java.util.ArrayList;

public class Account {

    //public static ArrayList<Account> CurrentAccount = new ArrayList<>();
    private String Username;

    private String EmailAddress;

    private String Gender;

    private String UserId;

    private int Age;

    private int Weight;

    private Gym myGym;

    private ArrayList<Account> FriendsList;

    private ArrayList<Account> BlockList;

    public String getUserId() {
        return UserId;
    }

    public void setUserId(String userId) {
        UserId = userId;
    }

    public ArrayList<Account> getBlockList() {
        return BlockList;
    }

    public void setBlockList(ArrayList<Account> blockList) {
        BlockList = blockList;
    }

    public Gym getMyGym() {
        return myGym;
    }

    public void setMyGym(Gym myGym) {
        this.myGym = myGym;
    }

    public ArrayList<Account> getFriendsList() {
        return FriendsList;
    }

    public void setFriendsList(ArrayList<Account> friendsList) {
        FriendsList = friendsList;
    }

    public Account() {
        Username = "NO USERNAME!!!";
        EmailAddress = "NO EMAIL";
        Age = -1;
        Weight = -1;
        Gender = "NO GENDER SET";
        FriendsList = null;
        BlockList = null;
        UserId = "";
    }

    public Account(String username, String emailAddress, int age, int weight, String gender, ArrayList<Account> friendsList, ArrayList<Account> blockList) {
        Username = username;
        EmailAddress = emailAddress;
        Age = age;
        Weight = weight;
        Gender = gender;
        FriendsList = friendsList;
        BlockList = blockList;
        UserId = "";
    }

    public String getUsername() {
        return Username;
    }

    public void setUsername(String username) {
        Username = username;
    }

    public String getEmailAddress() {
        return EmailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        EmailAddress = emailAddress;
    }

    public int getAge() {
        return Age;
    }

    public void setAge(int age) {
        Age = age;
    }

    public int getWeight() {
        return Weight;
    }

    public void setWeight(int weight) {
        Weight = weight;
    }

    public String getGender() {
        return Gender;
    }

    public void setGender(String gender) {
        Gender = gender;
    }

}
