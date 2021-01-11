package com.example.likeyesterday;


import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.likeyesterday.MyFriends.FriendsListFragment;
import com.example.likeyesterday.MyFriends.RequestListFragment;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;


public class ProfileFragment extends Fragment {
    public static FirebaseFirestore db= FirebaseFirestore.getInstance();
    public static FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    public static String uid = user.getUid();
//    public static FirebaseFirestore db;
//    public static FirebaseUser user;
//    public static String uid;

    public static DocumentReference currentUserDocumentReference = db.collection("Users").document(uid);

    Button update;
    TextView fullNameProfile,usernameProfile,friendsLabel,requestsLabel;
    TextInputLayout emailID,fullName,phoneNumber;
    CardView friendCard,requestCard;

    private Boolean fullNameChange,emailChange,phoneNoChange;

    private int friendsCounter=0;
    private int requestsCounter=0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup root=(ViewGroup) inflater.inflate(R.layout.fragment_profile, container, false);
        // Inflate the layout for this fragment
        update=root.findViewById(R.id.updateProfile);
        fullNameProfile=root.findViewById(R.id.fullNameProfile);
        usernameProfile=root.findViewById(R.id.userNameProfile);
        friendsLabel=root.findViewById(R.id.friendsLabel);
        requestsLabel=root.findViewById(R.id.requestsLabel);
        fullName=root.findViewById(R.id.fullNameProf);
        emailID=root.findViewById(R.id.emailIDProfile);
        phoneNumber=root.findViewById(R.id.phoneNumberProfile);
        friendCard=root.findViewById(R.id.friendCard);
        requestCard=root.findViewById(R.id.requestCard);

        user = FirebaseAuth.getInstance().getCurrentUser();
        uid = user.getUid();
        currentUserDocumentReference = db.collection("Users").document(uid);
        Log.i("userid",uid);


        currentUserDocumentReference.get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if(documentSnapshot.exists()){

                            currentUserDocumentReference.collection("FriendsList").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                @Override
                                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {


                                    friendsCounter=queryDocumentSnapshots.size();
                                    friendsLabel.setText(String.valueOf(friendsCounter));
                                }
                            });

                            currentUserDocumentReference.collection("Request List").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                @Override
                                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                                    requestsCounter=queryDocumentSnapshots.size();
                                    requestsLabel.setText(String.valueOf(requestsCounter));
                                }
                            });

                            fullNameProfile.setText(documentSnapshot.getString("Full Name"));
                            fullName.getEditText().setText(documentSnapshot.getString("Full Name"));
                            usernameProfile.setText(documentSnapshot.getString("Username"));
                            emailID.getEditText().setText(documentSnapshot.getString("Email ID"));
                            phoneNumber.getEditText().setText(documentSnapshot.getString("Phone Number"));
//                            password.getEditText().setText(documentSnapshot.getString("Password"));
                            fullNameChange=false;
                            emailChange=false;
                            phoneNoChange=false;
//                            passwordChange=false;
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getActivity(),"profile couldn't load",Toast.LENGTH_SHORT).show();
                    }
                });

        friendCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().beginTransaction().replace(R.id.fragment_container,new FriendsListFragment()).commit();
            }
        });

        requestCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().beginTransaction().replace(R.id.fragment_container,new RequestListFragment()).commit();
            }
        });

        fullName.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                fullNameChange=true;
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        emailID.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                emailChange=true;
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        phoneNumber.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                phoneNoChange=true;
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

//        password.getEditText().addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                passwordChange=true;
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//
//            }
//        });

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if( !emailChange && !phoneNoChange && !fullNameChange){
                    Toast.makeText(getActivity(), "Already up to date!", Toast.LENGTH_SHORT).show();
                }
//                if(passwordChange){
//                    user_profileReference.update("Password",password.getEditText().getText().toString().trim());
//                    Toast.makeText(getActivity(), "Successfully updated!", Toast.LENGTH_SHORT).show();
//                    fullNameChange=false;
//                    emailChange=false;
//                    phoneNoChange=false;
//                    passwordChange=false;
//                }
                if(emailChange){
                    currentUserDocumentReference.update("Email ID",emailID.getEditText().getText().toString().trim());
                    Toast.makeText(getActivity(), "Successfully updated!", Toast.LENGTH_SHORT).show();
                    fullNameChange=false;
                    emailChange=false;
                    phoneNoChange=false;
//                    passwordChange=false;
                }
                if(phoneNoChange){
//                    user_profileReference.update("Phone Number",phoneNumber.getEditText().getText().toString());
                    Toast.makeText(getActivity(), "Cannot Update Phone Number", Toast.LENGTH_SHORT).show();

                    currentUserDocumentReference.get()
                            .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                @Override
                                public void onSuccess(DocumentSnapshot documentSnapshot) {
                                    if(documentSnapshot.exists()){
                                        phoneNumber.getEditText().setText(documentSnapshot.getString("Phone Number"));
                                        fullNameChange=false;
                                        emailChange=false;
                                        phoneNoChange=false;
//                                        passwordChange=false;
                                    }
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(getActivity(),"profile couldn't load",Toast.LENGTH_SHORT).show();
                                }
                            });
                }
                if(fullNameChange){
                    currentUserDocumentReference.update("Full Name",fullName.getEditText().getText().toString().trim());
                    Toast.makeText(getActivity(), "Successfully updated!", Toast.LENGTH_SHORT).show();
                    currentUserDocumentReference.get()
                            .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                @Override
                                public void onSuccess(DocumentSnapshot documentSnapshot) {
                                    fullNameProfile.setText(documentSnapshot.getString("Full Name"));
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {

                                }
                            });
                    fullNameChange=false;
                    emailChange=false;
                    phoneNoChange=false;
//                    passwordChange=false;
                }
            }
        });
        return root;
    }


}