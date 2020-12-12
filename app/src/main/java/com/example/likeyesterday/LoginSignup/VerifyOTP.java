package com.example.likeyesterday.LoginSignup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import com.chaos.view.PinView;
import com.example.likeyesterday.HomeScreenActivity2;
import com.example.likeyesterday.MainActivity;
import com.example.likeyesterday.ProfileFragment;
import com.example.likeyesterday.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class VerifyOTP extends AppCompatActivity {
    PinView pinView;
    String systemOtp;
    FirebaseAuth mAuth;
    ProgressBar progressBar;
    Button verify;
    TextView textViewNumber;
    public FirebaseFirestore db= FirebaseFirestore.getInstance();
    String fullName;
    String emailID;
    String username;
    String newPassword;
    String gender;
    String dob;
    String phoneNumber;
    int noOfFriends=0,noOfRequests=0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_verify_o_t_p);

        pinView=findViewById(R.id.pinView);
        progressBar=findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);
        verify=findViewById(R.id.verifyOtp);
        mAuth=FirebaseAuth.getInstance();
        textViewNumber=findViewById(R.id.textViewNumber);

        fullName=getIntent().getStringExtra("Fullname");
        emailID=getIntent().getStringExtra("EmailID");
        username=getIntent().getStringExtra("Username");
        newPassword=getIntent().getStringExtra("Password");
        gender=getIntent().getStringExtra("Gender");
        dob=getIntent().getStringExtra("DOB");

        phoneNumber=getIntent().getStringExtra("PhoneNumber");

        textViewNumber.setText("Enter One Time Password sent on \n   "+phoneNumber);

        sendVerificationCodeToUser(phoneNumber);
    }

    private void sendVerificationCodeToUser(String phoneNumber) {
        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber(phoneNumber)       // Phone number to verify
                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(this)                 // Activity (for callback binding)
                        .setCallbacks(mCallbacks)          // OnVerificationStateChangedCallbacks
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks=
            new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

                @Override
                public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {

                    String code=phoneAuthCredential.getSmsCode();
                    if(code!=null){
                        pinView.setText(code);
                        progressBar.setVisibility(View.VISIBLE);
                        verifyCode(code);
                    }

                }

                @Override
                public void onVerificationFailed(@NonNull FirebaseException e) {
                    Toast.makeText(VerifyOTP.this, e.getMessage(), Toast.LENGTH_LONG).show();
                }

                @Override
                public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                    super.onCodeSent(s, forceResendingToken);

                    systemOtp=s;
                }
            };

    private void verifyCode(String codeByUser) {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(systemOtp,codeByUser);
        SignInUsingCredential(credential);
    }

    private void SignInUsingCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(VerifyOTP.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("Otp", "signInWithCredential:success");
                            FirebaseUser user = task.getResult().getUser();
                            String uid = user.getUid();
                            DocumentReference user_profileReference = db.collection("Users").document(uid);

                            Map<String,Object> profile = new HashMap<>();
                            profile.put("Full Name",fullName);
                            profile.put("Username",username);
                            profile.put("Email ID",emailID);
                            profile.put("Phone Number",phoneNumber);
                            profile.put("Gender",gender);
                            profile.put("D.O.B",dob);
                            profile.put("Number of friends",noOfFriends);
                            profile.put("Number of requests",noOfRequests);
                            profile.put("Email ID",emailID);


                            user_profileReference.set(profile)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Toast.makeText(VerifyOTP.this,"Account Created",Toast.LENGTH_SHORT).show();
                                            DocumentReference FriendReference = db.collection(uid).document("Friends");
                                            DocumentReference requestReference = db.collection(uid).document("Request");
//                                            Map<String,Object> friends = new HashMap<>();
//                                            friends.put("No. of friends",fullName);

                                            AuthCredential credential = EmailAuthProvider.getCredential(emailID, newPassword);
                                            mAuth.getCurrentUser().linkWithCredential(credential)
                                                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<AuthResult> task) {
                                                            if (task.isSuccessful()) {
                                                                Log.d("Verify otp", "linkWithCredential:success");
                                                                FirebaseUser user = task.getResult().getUser();


                                                            } else{
                                                                Log.w("verify", "linkWithCredential:failure", task.getException());
                                                                Toast.makeText(VerifyOTP.this, "Failed to link with email", Toast.LENGTH_SHORT).show();
                                                            }
                                                        }
                                                    });

                                            Intent intent= new Intent(getApplicationContext(), HomeScreenActivity2.class);
                                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                                          intent.putExtra("uid",uid);
                                            startActivity(intent);
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(VerifyOTP.this,"Something went wrong /n Try again",Toast.LENGTH_SHORT).show();
                                            Intent intent= new Intent(getApplicationContext(),Signup.class);
                                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                            startActivity(intent);
                                        }
                                    });


                        } else {
                            Toast.makeText(VerifyOTP.this,task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                            // Sign in failed, display a message and update the UI
                            Log.w("Otp", "signInWithCredential:failure", task.getException());


                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                // The verification code entered was invalid
                                Toast.makeText(VerifyOTP.this, "The Otp entered was invalid", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
    }

    public void verifyOtp(View view){
        String code=pinView.getText().toString();

        if(code.isEmpty()){
            Toast.makeText(VerifyOTP.this, "Otp cannot be empty", Toast.LENGTH_SHORT).show();
            return;
        }

        if(!code.isEmpty()){
            progressBar.setVisibility(View.VISIBLE);
            verifyCode(code);
        }

    }
}