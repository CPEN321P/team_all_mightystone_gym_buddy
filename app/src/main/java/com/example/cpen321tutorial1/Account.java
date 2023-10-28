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

    public Account(String username, String emailAddress, int age, int weight, String gender, String role) {
        Username = username;
        EmailAddress = emailAddress;
        Age = age;
        Weight = weight;
        Gender = gender;
        Role = role;
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
