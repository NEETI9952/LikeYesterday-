package com.example.likeyesterday;


public class FirestoreRecyclerModelClass {

    private String FullName;

    public FirestoreRecyclerModelClass(){

    }

    public String getFullName() {
        return FullName;
    }

    public void setFullName(String fullName) {
        FullName = fullName;
    }

    public FirestoreRecyclerModelClass(String fullName) {
        FullName = fullName;
    }
}
