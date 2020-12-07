package com.example.likeyesterday.LoginSignup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.example.likeyesterday.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;


public class userProfile extends AppCompatActivity {
    Button update;
    TextView fullNameProfile,usernameProfile,friendsLabel,requestsLabel;
    TextInputLayout emailID,fullName,phoneNumber;
    private Boolean fullNameChange,emailChange,phoneNoChange;

    public FirebaseFirestore db= FirebaseFirestore.getInstance();
    DocumentReference user_profileReference;

    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    String uid = user.getUid();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        update=findViewById(R.id.updateProfile);
        fullNameProfile=findViewById(R.id.fullNameProfile);
        usernameProfile=findViewById(R.id.userNameProfile);
        friendsLabel=findViewById(R.id.friendsLabel);
        requestsLabel=findViewById(R.id.requestsLabel);
        fullName=findViewById(R.id.fullNameProf);
        emailID=findViewById(R.id.emailIDProfile);
        phoneNumber=findViewById(R.id.phoneNumberProfile);



//        uid=getIntent().getStringExtra("uid");
//        Log.i("uid",uid);

        user_profileReference = db.collection(uid).document("User Profile");

        user_profileReference.get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if(documentSnapshot.exists()){

                            fullNameProfile.setText(documentSnapshot.getString("Full Name"));
                            fullName.getEditText().setText(documentSnapshot.getString("Full Name"));
                            usernameProfile.setText(documentSnapshot.getString("Username"));
                            emailID.getEditText().setText(documentSnapshot.getString("Email ID"));
                            phoneNumber.getEditText().setText(documentSnapshot.getString("Phone Number"));
                            friendsLabel.setText(documentSnapshot.get("Number of friends").toString());
                            requestsLabel.setText(documentSnapshot.get("Number of requests").toString());
                            fullNameChange=false;
                            emailChange=false;
                            phoneNoChange=false;
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(userProfile.this,"profile couldn't load",Toast.LENGTH_SHORT).show();
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
                if(!emailChange && !phoneNoChange && !fullNameChange){
                    Toast.makeText(userProfile.this, "Already up to date!", Toast.LENGTH_SHORT).show();
                }
//                if(passwordChange){
//                    user_profileReference.update("Password",password.getEditText().getText().toString().trim());
//                    Toast.makeText(userProfile.this, "Successfully updated!", Toast.LENGTH_SHORT).show();
//                    fullNameChange=false;
//                    emailChange=false;
//                    phoneNoChange=false;
//                    passwordChange=false;
//                }
                if(emailChange){
                    user_profileReference.update("Email ID",emailID.getEditText().getText().toString().trim());
                    Toast.makeText(userProfile.this, "Successfully updated!", Toast.LENGTH_SHORT).show();
                    fullNameChange=false;
                    emailChange=false;
                    phoneNoChange=false;
//                    passwordChange=false;
                }
                if(phoneNoChange){
//                    user_profileReference.update("Phone Number",phoneNumber.getEditText().getText().toString());
                    Toast.makeText(userProfile.this, "Cannot Update Phone Number", Toast.LENGTH_SHORT).show();

                    user_profileReference.get()
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
                                    Toast.makeText(userProfile.this,"profile couldn't load",Toast.LENGTH_SHORT).show();
                                }
                            });
                }
                if(fullNameChange){
                    user_profileReference.update("Full Name",fullName.getEditText().getText().toString().trim());
                    Toast.makeText(userProfile.this, "Successfully updated!", Toast.LENGTH_SHORT).show();
                    user_profileReference.get()
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

    }




}