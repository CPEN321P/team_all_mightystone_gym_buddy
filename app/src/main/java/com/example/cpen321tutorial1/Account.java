package com.example.cpen321tutorial1;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;

public class Account {

    public static ArrayList<Account> CurrentAccount = new ArrayList<>();

    private String Username;
    private String EmailAddress;
    private int Age;
    private int Weight;
    private String Gender;
    private String Role;
    private ArrayList<Account> FriendsList;
    private ArrayList<Account> BlockList;

    public ArrayList<Account> getBlockList() {
        return BlockList;
    }

    public void setBlockList(ArrayList<Account> blockList) {
        BlockList = blockList;
    }

    public static ArrayList<Account> getCurrentAccount() {
        return CurrentAccount;
    }

    public static void setCurrentAccount(ArrayList<Account> currentAccount) {
        CurrentAccount = currentAccount;
    }

    public ArrayList<Account> getFriendsList() {
        return FriendsList;
    }

    public void setFriendsList(ArrayList<Account> friendsList) {
        FriendsList = friendsList;
    }

    public Account(String username, String emailAddress, int age, int weight, String gender, String role, ArrayList<Account> friendsList, ArrayList<Account> blockList) {
        Username = username;
        EmailAddress = emailAddress;
        Age = age;
        Weight = weight;
        Gender = gender;
        Role = role;
        FriendsList = friendsList;
        BlockList = blockList;
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

    public String getRole() {
        return Role;
    }

    public void setRole(String role) {
        Role = role;
    }
}
