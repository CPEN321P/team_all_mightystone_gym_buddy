package com.example.cpen321tutorial1;

public class PersonItem {

    String name;
    String username;
    int image;

    public PersonItem(String name, String username, int image) {
        this.name = name;
        this.username = username;
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }
}
