package com.example.sharewave.classes;

public class Post {

    private int id ;
    private String path;
    private String caption;
    private String rating;
    private String image_size;
    private String id_location;
    private String id_user;
    private String created_at;
    private String beach_name;
    private String location_name;

    public Post(){

    }

    public Post(int id, String path, String caption, String rating, String image_size, String id_location, String id_user, String created_at, String beach_name, String location_name) {
        this.id = id;
        this.path = path;
        this.caption = caption;
        this.rating = rating;
        this.image_size = image_size;
        this.id_location = id_location;
        this.id_user = id_user;
        this.created_at = created_at;
        this.beach_name = beach_name;
        this.location_name = location_name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getImage_size() {
        return image_size;
    }

    public void setImage_size(String image_size) {
        this.image_size = image_size;
    }

    public String getId_location() {
        return id_location;
    }

    public void setId_location(String id_location) {
        this.id_location = id_location;
    }

    public String getId_user() {
        return id_user;
    }

    public void setId_user(String id_user) {
        this.id_user = id_user;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
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
}
