package com.example.likeyesterday.LoginSignup;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.likeyesterday.R;
import com.google.android.material.textfield.TextInputLayout;
import com.hbb20.CountryCodePicker;

public class SignUp3 extends AppCompatActivity {
    ImageView backButton;
    Button next,login;
    TextView titleText;
    TextInputLayout phoneNumber;
    CountryCodePicker countryCodePicker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_sign_up3);

        backButton=findViewById(R.id.signupBackButton);
        next=findViewById(R.id.signUpNextButton);
        login=findViewById(R.id.signUpLoginButton);
        titleText=findViewById(R.id.signUpTitle);
        phoneNumber=findViewById(R.id.signUpPhoneNo);
        countryCodePicker=findViewById(R.id.countryCodePicker);

        //countryCodePicker.registerCarrierNumberEditText(phoneNumber.getEditText());
    }

    public void  callOtpVerificationScreen(View view) {

        if(!validatePhoneNumber()){
            return;
        }

        String userPhoneNumber=phoneNumber.getEditText().getText().toString().trim();

        String verificationPhoneNumber="+91"+userPhoneNumber;
        Log.i("testnumber","here"+userPhoneNumber.toString());
//        Log.i("testnumber",countryCodePicker.getFullNumber()+userPhoneNumber.toString());

//        String fullName=getIntent().getStringExtra("Fullname");
//        String emailID=getIntent().getStringExtra("EmailID");
//        String username=getIntent().getStringExtra("Username");
//        String newPassword=getIntent().getStringExtra("Password");
//        String gender=getIntent().getStringExtra("Gender");
//        String dob=getIntent().getStringExtra("DOB");


        Intent intent = new Intent(getApplicationContext(), VerifyOTP.class);

//        intent.putExtra("Fullname",fullName);
//        intent.putExtra("EmailID",emailID);
//        intent.putExtra("Username",username);
//        intent.putExtra("Password",newPassword);
//        intent.putExtra("Gender",gender);
//        intent.putExtra("DOB",dob);

        intent.putExtra("PhoneNumber",verificationPhoneNumber);
        startActivity(intent);
    }

    public boolean validatePhoneNumber(){
        String val=phoneNumber.getEditText().getText().toString().trim();

        if (val.isEmpty()){
            phoneNumber.setError("Phone Number cannot be empty");
            return false;
        } else{
            phoneNumber.setError(null);
            phoneNumber.setErrorEnabled(false);
            return true;
        }

    }
}