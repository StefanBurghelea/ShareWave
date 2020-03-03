package com.example.sharewave.classes;

public class Post {

    private int id ;
    private String beach_name;
    private String rating;
    private String caption;
    private String path;
    private String location;

    public Post(){

    }



    public Post(int id, String beach_name, String rating, String caption, String path,String location) {
        this.id = id;
        this.beach_name = beach_name;
        this.rating = rating;
        this.caption = caption;
        this.path = path;
        this.location = location;
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

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String description) {
        this.caption = description;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) { this.path = path; }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
