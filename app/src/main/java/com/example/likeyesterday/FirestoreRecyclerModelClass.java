package com.example.likeyesterday;


public class FirestoreRecyclerModelClass {

    private String FullName;
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
        FullName = fullName;
    }

    public FirestoreRecyclerModelClass(String fullName,String friendUid) {
        this.friendUid=friendUid;
        FullName = fullName;
    }
}
