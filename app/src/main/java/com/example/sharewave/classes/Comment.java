package com.example.sharewave.classes;

public class Comment {

    int id;
    String Comment;
    String id_post;
    String id_user;

    public Comment(){}

    public Comment(int id, String comment, String id_post, String id_user) {
        this.id = id;
        Comment = comment;
        this.id_post = id_post;
        this.id_user = id_user;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getComment() {
        return Comment;
    }

    public void setComment(String comment) {
        Comment = comment;
    }

    public String getId_post() {
        return id_post;
    }

    public void setId_post(String id_post) {
        this.id_post = id_post;
    }

    public String getId_user() {
        return id_user;
    }

    public void setId_user(String id_user) {
        this.id_user = id_user;
    }
}
