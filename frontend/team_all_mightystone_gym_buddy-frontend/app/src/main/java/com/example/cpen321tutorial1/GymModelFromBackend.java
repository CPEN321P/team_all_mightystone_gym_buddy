package com.example.cpen321tutorial1;

import java.util.List;

public class GymModelFromBackend {

    private String _id;

    private String name;

    private String description;

    private String location;

    private String phone;

    private String email;

    private List<String> images;

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
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

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }

    public String toString() {
        return String.format("_id:%d,name:%d,description:%d," +
                        "location:%d,phone:%s,email:%s,images:%d",
                _id, name, description,
                location, phone, email, images);
    }
}
