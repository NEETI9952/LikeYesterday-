package com.example.likeyesterday.MyFriends;

import java.net.URI;

public class FeedItem {

    private String sender;
    private String description;
    private  String Time;
    private String Date;
    private String Image;

    public FeedItem(String sender, String description, String time, String date, String image) {
        this.sender = sender;
        this.description = description;
        Time = time;
        Date = date;
        Image = image;
    }

    public FeedItem() {
    }


    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTime() {
        return Time;
    }

    public void setTime(String time) {
        Time = time;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }
}
