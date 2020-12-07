package com.example.likeyesterday;


import android.os.Bundle;
import androidx.annotation.NonNull;
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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;


public class ProfileFragment extends Fragment {

    Button update;
    TextView fullNameProfile,usernameProfile,friendsLabel,requestsLabel;
    TextInputLayout emailID,fullName,password,phoneNumber;
    private Boolean fullNameChange,emailChange,phoneNoChange,passwordChange;

    public FirebaseFirestore db= FirebaseFirestore.getInstance();
    DocumentReference user_profileReference;

    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    String uid = user.getUid();

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
        password=root.findViewById(R.id.passwordProfile);
        phoneNumber=root.findViewById(R.id.phoneNumberProfile);


//        uid=getActivity().getIntent().getStringExtra("uid");
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
                            password.getEditText().setText(documentSnapshot.getString("Password"));
                            friendsLabel.setText(documentSnapshot.get("Number of friends").toString());
                            requestsLabel.setText(documentSnapshot.get("Number of requests").toString());
                            fullNameChange=false;
                            emailChange=false;
                            phoneNoChange=false;
                            passwordChange=false;
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getActivity(),"profile couldn't load",Toast.LENGTH_SHORT).show();
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

        password.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                passwordChange=true;
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!passwordChange && !emailChange && !phoneNoChange && !fullNameChange){
                    Toast.makeText(getActivity(), "Already up to date!", Toast.LENGTH_SHORT).show();
                }
                if(passwordChange){
                    user_profileReference.update("Password",password.getEditText().getText().toString().trim());
                    Toast.makeText(getActivity(), "Successfully updated!", Toast.LENGTH_SHORT).show();
                    fullNameChange=false;
                    emailChange=false;
                    phoneNoChange=false;
                    passwordChange=false;
                }
                if(emailChange){
                    user_profileReference.update("Email ID",emailID.getEditText().getText().toString().trim());
                    Toast.makeText(getActivity(), "Successfully updated!", Toast.LENGTH_SHORT).show();
                    fullNameChange=false;
                    emailChange=false;
                    phoneNoChange=false;
                    passwordChange=false;
                }
                if(phoneNoChange){
//                    user_profileReference.update("Phone Number",phoneNumber.getEditText().getText().toString());
                    Toast.makeText(getActivity(), "Cannot Update Phone Number", Toast.LENGTH_SHORT).show();

                    user_profileReference.get()
                            .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                @Override
                                public void onSuccess(DocumentSnapshot documentSnapshot) {
                                    if(documentSnapshot.exists()){
                                        phoneNumber.getEditText().setText(documentSnapshot.getString("Phone Number"));
                                        fullNameChange=false;
                                        emailChange=false;
                                        phoneNoChange=false;
                                        passwordChange=false;
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
                    user_profileReference.update("Full Name",fullName.getEditText().getText().toString().trim());
                    Toast.makeText(getActivity(), "Successfully updated!", Toast.LENGTH_SHORT).show();
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
                    passwordChange=false;
                }
            }
        });
        return root;
    }
}