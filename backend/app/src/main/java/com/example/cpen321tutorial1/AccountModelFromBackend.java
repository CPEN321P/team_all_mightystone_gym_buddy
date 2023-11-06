package com.example.cpen321tutorial1;

import java.util.ArrayList;
import java.util.List;

public class AccountModelFromBackend {

    private String _id;
    private String name;
    private String phone;
    private String email;
    private Long age;
    private String gender;
    private Long weight;
    private String pfp;
    //this array holds friends' IDS
    private List<String> friends;
    private List<String> friendRequests;
    private String description;
    private String homeGym;
    private Long reported;
    //this array holds this account's chatIds
    private List<String> chats;
    private List<String> blockedUsers;

    public String getId() {
        return _id;
    }

    public void setId(String id) {
        this._id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public Long getAge() {
        return age;
    }

    public void setAge(Long age) {
        this.age = age;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public Long getWeight() {
        return weight;
    }

    public void setWeight(Long weight) {
        this.weight = weight;
    }

    public String getPfp() {
        return pfp;
    }

    public void setPfp(String pfp) {
        this.pfp = pfp;
    }

    public List<String> getFriends() {
        return friends;
    }

    public void setFriends(List<String> friends) {
        this.friends = friends;
    }

    public List<String> getFriendRequests() {
        return friendRequests;
    }

    public void setFriendRequests(List<String> friendRequests) {
        this.friendRequests = friendRequests;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getHomeGym() {
        return homeGym;
    }

    public void setHomeGym(String homeGym) {
        this.homeGym = homeGym;
    }

    public Long getReported() {
        return reported;
    }

    public void setReported(Long reported) {
        this.reported = reported;
    }

    public List<String> getChats() {
        return chats;
    }

    public void setChats(List<String> chats) {
        this.chats = chats;
    }

    public List<String> getBlockedUsers() {
        return blockedUsers;
    }

    public void setBlockedUsers(List<String> blockedUsers) {
        this.blockedUsers = blockedUsers;
    }

    public String toString() {
        return String.format("_id:%s,name:%d,phone:%s,email:%s,age:%s,gender:%s,weight:%s,pfp:%s,friends:%s,friendRequests:%s,description:%s,homeGym:%s,reported:%s,chats:%s", _id, name, phone, email, age, gender, weight, pfp, friends, friendRequests, description, homeGym, reported, chats);
    }
}
