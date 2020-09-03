package com.example.project.Model;

import com.google.firebase.database.ServerValue;

public class Comment {
    private String content,uid,uImg,uName;
    private Object timestamp;

    public Comment() {
    }

    public Comment(String content, String uid, String uImg, String uName) {
        this.content = content;
        this.uid = uid;
        this.uImg = uImg;
        this.uName = uName;
        this.timestamp= ServerValue.TIMESTAMP;

    }

    public Comment(String content, String uid, String uImg, String uName, Object timestamp) {
        this.content = content;
        this.uid = uid;
        this.uImg = uImg;
        this.uName = uName;
        this.timestamp = timestamp;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
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

    public Object getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Object timestamp) {
        this.timestamp = timestamp;
    }
}
