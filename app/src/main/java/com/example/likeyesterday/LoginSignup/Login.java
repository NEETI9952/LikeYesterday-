package com.example.likeyesterday.LoginSignup;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.likeyesterday.MainActivity;
import com.example.likeyesterday.R;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.hbb20.CountryCodePicker;

public class Login extends AppCompatActivity {
    ImageView backButton;
    Button createUser,getOtpButton;
    TextView titleText;
    TextInputLayout phoneNumber;
    CheckBox checkBox;
    CountryCodePicker countryCodePicker;
    private FirebaseFirestore db= FirebaseFirestore.getInstance();
    private CollectionReference collRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login);

        backButton=findViewById(R.id.loginBackButton);
        createUser=findViewById(R.id.loginCreateUser);
        getOtpButton=findViewById(R.id.loginGetOtpButton);
        titleText=findViewById(R.id.loginTextView);
        phoneNumber=findViewById(R.id.loginPhoneNumber);
        checkBox=findViewById(R.id.rememberMeCheckBox);
        countryCodePicker=findViewById(R.id.loginCcp);

    }

    public void callLoginWithEmailScreen(View view){

        Intent intent= new Intent(getApplicationContext(),LoginWithEmail.class);

        Pair[] pairs= new Pair[4];
        pairs[0]= new Pair<View,String>(backButton,"back_image_transition");
        pairs[1]= new Pair<View,String>(titleText,"createAccount_text_transition");
        pairs[2]= new Pair<View,String>(getOtpButton,"next_button_transition");
        pairs[3]= new Pair<View,String>(createUser,"login_button_transition");

        ActivityOptions options=ActivityOptions.makeSceneTransitionAnimation(Login.this,pairs);
        startActivity(intent,options.toBundle());

    }

    public void  callSignUpScreen(View view){

        Intent intent2= new Intent(getApplicationContext(),Signup.class);

        Pair[] pairs2= new Pair[4];
        pairs2[0]= new Pair<View,String>(backButton,"back_image_transition");
        pairs2[1]= new Pair<View,String>(titleText,"createAccount_text_transition");
        pairs2[2]= new Pair<View,String>(getOtpButton,"next_button_transition");
        pairs2[3]= new Pair<View,String>(createUser,"login_button_transition");

        ActivityOptions options2=ActivityOptions.makeSceneTransitionAnimation(Login.this,pairs2);
        startActivity(intent2,options2.toBundle());

    }


    public void getOtp(View view){

        if(!validateFields()){
            return;
        }
        String phoneNumberText=phoneNumber.getEditText().getText().toString().trim();
        String ccp = countryCodePicker.getSelectedCountryCode();
        String verificationPhoneNumber="+"+ccp+phoneNumber.toString();

        Intent intent = new Intent(getApplicationContext(), VerifyOTPLogin.class);
        intent.putExtra("PhoneNumber",verificationPhoneNumber);
        startActivity(intent);
    }

    private boolean validateFields() {
        return true;
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}