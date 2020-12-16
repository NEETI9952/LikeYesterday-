package com.example.likeyesterday;


public class FirestoreRecyclerModelClass {

    private String FullName;
    private String PhoneNumber;
    private String friendUid;

    public String getFriendUid() {
        return friendUid;
    }

    public void setFriendUid(String friendUid) {
        this.friendUid = friendUid;
    }

    public FirestoreRecyclerModelClass(){

    }

    public String getFullName() {
        return FullName;
    }

    public void setFullName(String fullName) {
        this.FullName = fullName;
    }

    public FirestoreRecyclerModelClass(String fullName,String friendUid,String PhoneNumber) {
        this.friendUid = friendUid;
        this.FullName = fullName;
        this.PhoneNumber = PhoneNumber;
    }

    public String getPhoneNumber() {
        return PhoneNumber;
    }

    public void setPhoneNumber(String PhoneNumber) {
        this.PhoneNumber = PhoneNumber;
    }
}
