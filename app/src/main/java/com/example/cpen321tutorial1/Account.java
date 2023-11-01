package com.example.cpen321tutorial1;

import android.content.Intent;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;

public class Account {

    //public static ArrayList<Account> CurrentAccount = new ArrayList<>();
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

//    public static ArrayList<Account> getCurrentAccount() {
//        return CurrentAccount;
//    }

//    public static void setCurrentAccount(ArrayList<Account> currentAccount) {
//        CurrentAccount = currentAccount;
//    }

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
        Role = "NO ROLE SET";
        FriendsList = null;
        BlockList = null;
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

    public static void passAccountAsIntent (Intent intent, Account account){
        intent.putExtra("Username", account.getUsername());
        intent.putExtra("EmailAddress", account.getEmailAddress());
        intent.putExtra("Age", account.getAge());
        intent.putExtra("Weight", account.getWeight());
        intent.putExtra("Gender", account.getGender());
        intent.putExtra("Role", account.getRole());

    }

    public static Account getAccountFromIntent(Intent intent){
        Account account = new Account();

        account.setUsername(intent.getStringExtra("Username"));
        account.setEmailAddress(intent.getStringExtra("EmailAddress"));
        account.setAge(intent.getIntExtra("Age", 0));
        account.setWeight(intent.getIntExtra("Weight", 0));
        account.setGender(intent.getStringExtra("Gender"));
        account.setRole(intent.getStringExtra("Role"));


        return account;

    }
}
