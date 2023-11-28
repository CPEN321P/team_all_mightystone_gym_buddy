package com.example.cpen321tutorial1;

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

    private ArrayList<Chat> ChatList;

    public ArrayList<Chat> getChatList() {
        return ChatList;
    }

    public void setChatList(ArrayList<Chat> chatList) {
        ChatList = chatList;
    }

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
        FriendsList = new ArrayList<>();
        BlockList = new ArrayList<>();
        UserId = "";
        ChatList = new ArrayList<>();
    }

    public Account(String username, String emailAddress,
                   int age, int weight, String gender,
                   ArrayList<Account> friendsList,
                   ArrayList<Account> blockList, ArrayList<Chat> chatList) {

        Username = username;
        EmailAddress = emailAddress;
        Age = age;
        Weight = weight;
        Gender = gender;
        FriendsList = friendsList;
        BlockList = blockList;
        UserId = "";
        ChatList = chatList;
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

    public static Account GetAccountInfromation(String UserId) {
        ConnectionToBackend c = new ConnectionToBackend();
        Account thisAccount = c.getAccountInformation(UserId);

        return thisAccount;

    }

}
