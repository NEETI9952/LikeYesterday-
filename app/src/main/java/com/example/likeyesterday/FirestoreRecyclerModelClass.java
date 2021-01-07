package com.example.likeyesterday;


import com.google.firebase.firestore.GeoPoint;

public class FirestoreRecyclerModelClass {

    private String FullName;
    private String PhoneNumber;
    private String friendUid;
    private String PlaceName;
    private GeoPoint geopoint;
    private String description;
    private int NoOfTimes;
    private String Date;
    private String Time;

    public FirestoreRecyclerModelClass(){
    }

    public FirestoreRecyclerModelClass(String fullName, String friendUid, String PhoneNumber, String PlaceName,GeoPoint geopoint,String description,int NoOfTimes,String Date,String Time) {
        this.friendUid = friendUid;
        this.FullName = fullName;
        this.PhoneNumber = PhoneNumber;
        this.geopoint = geopoint;
        this.PlaceName = PlaceName;
        this.description = description;
        this.NoOfTimes=NoOfTimes;
        this.Date=Date;
        this.Time=Time;
    }

    public String getFriendUid() {
        return friendUid;
    }

    public void setFriendUid(String friendUid) {
        this.friendUid = friendUid;
    }


    public String getFullName() {
        return FullName;
    }

    public void setFullName(String fullName) {
        this.FullName = fullName;
    }

    public String getPlaceName() { return PlaceName; }

    public void setPlaceName(String placeName) { this.PlaceName = placeName; }

    public String getPhoneNumber() {
        return PhoneNumber;
    }

    public void setPhoneNumber(String PhoneNumber) {
        this.PhoneNumber = PhoneNumber;
    }

    public GeoPoint getgeopoint() {
        return geopoint;
    }

    public void setgeopoint(GeoPoint geopoint) {
        this.geopoint = geopoint;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getNoOfTimes() { return NoOfTimes; }

    public void setNoOfTimes(int noOfTimes) { NoOfTimes = noOfTimes; }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getTime() {
        return Time;
    }

    public void setTime(String time) {
        Time = time;
    }

}
