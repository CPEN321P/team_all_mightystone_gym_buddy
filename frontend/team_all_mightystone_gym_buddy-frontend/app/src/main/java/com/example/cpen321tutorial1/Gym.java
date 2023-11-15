package com.example.cpen321tutorial1;

public class Gym {

    private String name;

    private String address;

    private String phone;

    private String email;

    private String description;

    private String GymId;

    public Gym(String name, String address, String phone, String email, String description, String gymId) {
        this.name = name;
        this.address = address;
        this.phone = phone;
        this.email = email;
        this.description = description;
        GymId = gymId;
    }

    public Gym(String name, String address, String phone, String email, String description) {
        this.name = name;
        this.address = address;
        this.phone = phone;
        this.email = email;
        this.description = description;
        GymId = "";
    }

    public Gym() {
        this.name = "NO NAME";
        this.address = "NO ADDRESS";
        this.phone = "0000000000";
        this.email = "NOTHING";
        this.description = "NOT FOR NOW!!!";
        this.GymId = "";
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getGymId() {
        return GymId;
    }

    public void setGymId(String gymId) {
        GymId = gymId;
    }
}
