package com.example.likeyesterday.LoginSignup;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.chaos.view.PinView;
import com.example.likeyesterday.HomeScreenActivity2;
import com.example.likeyesterday.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
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
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class LoginVerifyOTP extends AppCompatActivity {
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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_verify_otp_login);

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
                    Toast.makeText(LoginVerifyOTP.this, e.getMessage(), Toast.LENGTH_LONG).show();
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
                .addOnCompleteListener(LoginVerifyOTP.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("Otp", "signInWithCredential:success");
                            FirebaseUser user = task.getResult().getUser();

                            long creationTimestamp = user.getMetadata().getCreationTimestamp();
                            long lastSignInTimestamp = user.getMetadata().getLastSignInTimestamp();
                            if (creationTimestamp==lastSignInTimestamp) {
                                Log.i("Verify","check unsuccesfuffffffl");
                                Toast.makeText(LoginVerifyOTP.this, "User does not exist", Toast.LENGTH_LONG).show();
                                user.delete()
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    Log.d("otp", "User account deleted.");
                                                }
                                            }
                                        });

                            }else {
                                Intent intent= new Intent(getApplicationContext(), HomeScreenActivity2.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                            }

                        } else {
                            Toast.makeText(LoginVerifyOTP.this,task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                            // Sign in failed, display a message and update the UI
                            Log.w("Otp", "signInWithCredential:failure", task.getException());


                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                // The verification code entered was invalid
                                Toast.makeText(LoginVerifyOTP.this, "The Otp entered was invalid", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
    }

    public void verifyOtp(View view){
        String code=pinView.getText().toString();

        if(code.isEmpty()){
            Toast.makeText(LoginVerifyOTP.this, "Otp cannot be empty", Toast.LENGTH_SHORT).show();
            return;
        }

        if(!code.isEmpty()){
            progressBar.setVisibility(View.VISIBLE);
            verifyCode(code);
        }

    }
}