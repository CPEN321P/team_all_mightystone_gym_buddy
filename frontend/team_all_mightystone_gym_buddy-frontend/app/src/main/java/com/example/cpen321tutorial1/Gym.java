package com.example.cpen321tutorial1;

public class Gym {

    private String name;

    private String address;

    private String AccessTime;

    private String Website;

    private String Tips;

    private int image;

    public Gym(){

    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public Gym(String name, String address,
               String accessTime, String website, String tips) {

        this.name = name;
        this.address = address;
        AccessTime = accessTime;
        Website = website;
        Tips = tips;

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
