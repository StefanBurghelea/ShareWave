package com.example.sharewave.classes;

import java.io.Serializable;

public class Location implements Serializable {

    int id;
    String beach_name;
    String location_name;
    String longitude;
    String latitude;

    public Location(){}

    public Location(int id, String beach_name, String location_name, String latitude, String longitude) {
        this.id = id;
        this.beach_name = beach_name;
        this.location_name = location_name;
        this.longitude = longitude;
        this.latitude = latitude;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getBeach_name() {
        return beach_name;
    }

    public void setBeach_name(String beach_name) {
        this.beach_name = beach_name;
    }

    public String getLocation_name() {
        return location_name;
    }

    public void setLocation_name(String location_name) {
        this.location_name = location_name;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String toString() {
        return location_name + " - " + beach_name;
    }
}
