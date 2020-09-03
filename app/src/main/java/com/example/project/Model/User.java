package com.example.project.Model;

public class User {

    private String uid,uImg,uName;


    public User() {
    }

    public User(String uid, String uImg, String uName) {
        this.uid = uid;
        this.uImg = uImg;
        this.uName = uName;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getuImg() {
        return uImg;
    }

    public void setuImg(String uImg) {
        this.uImg = uImg;
    }

    public String getuName() {
        return uName;
    }

    public void setuName(String uName) {
        this.uName = uName;
    }
}
