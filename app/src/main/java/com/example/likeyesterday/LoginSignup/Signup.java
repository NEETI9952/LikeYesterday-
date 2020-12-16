package com.example.likeyesterday.LoginSignup;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.likeyesterday.HomeScreenActivity2;
import com.example.likeyesterday.R;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Objects;

import static com.example.likeyesterday.LoginSignup.LoginWithEmail.mAuth;

public class Signup extends AppCompatActivity {
    ImageView backButton;
    Button next,login;
    TextView titleText;
    TextInputLayout username,emailID,fullName,newPassword,confirmPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_signup);

        if (mAuth.getCurrentUser() != null) {
            login();
        }

        backButton=findViewById(R.id.signupBackButton);
        next=findViewById(R.id.signUpNextButton);
        login=findViewById(R.id.signUpLoginButton);
        titleText=findViewById(R.id.signUpTitle);

        fullName=findViewById(R.id.signUpFullNameTextView);
        username=findViewById(R.id.signUpUsernameTextView);
        emailID=findViewById(R.id.signUpEmailTextView);
        newPassword=findViewById(R.id.signUpNewPassword);
        confirmPassword=findViewById(R.id.signUpConfirmPassword);
    }


    public void login() {
        //move to next activity
        Intent intent = new Intent(this, HomeScreenActivity2.class);
        startActivity(intent);
    }

    public void  callSignUpScreen2(View view){

        if(!validateFullName()| !validateUsername()| !validateEmail()|!validatePassword()|!confirmPassword()){
            return;
        }

        Intent intent= new Intent(getApplicationContext(),SignUp2.class);

        intent.putExtra("Fullname",fullName.getEditText().getText().toString().trim());
        intent.putExtra("EmailID",emailID.getEditText().getText().toString().trim());
        intent.putExtra("Username",username.getEditText().getText().toString().trim());
        intent.putExtra("Password",newPassword.getEditText().getText().toString().trim());

        Pair[] pairs= new Pair[4];
        pairs[0]= new Pair<View,String>(backButton,"back_image_transition");
        pairs[1]= new Pair<View,String>(titleText,"createAccount_text_transition");
        pairs[2]= new Pair<View,String>(login,"login_button_transition");
        pairs[3]= new Pair<View,String>(next,"next_button_transition");

        ActivityOptions options=ActivityOptions.makeSceneTransitionAnimation(Signup.this,pairs);
        startActivity(intent,options.toBundle());


    }

    public void  callLogin(View view){

        Intent intentLogin= new Intent(getApplicationContext(),Login.class);

        Pair[] pairs= new Pair[4];
        pairs[0]= new Pair<View,String>(backButton,"back_image_transition");
        pairs[1]= new Pair<View,String>(titleText,"createAccount_text_transition");
        pairs[2]= new Pair<View,String>(login,"login_button_transition");
        pairs[3]= new Pair<View,String>(next,"next_button_transition");

        ActivityOptions options=ActivityOptions.makeSceneTransitionAnimation(Signup.this,pairs);
        startActivity(intentLogin,options.toBundle());

    }

    private boolean validateFullName(){
        String val= fullName.getEditText().getText().toString().trim();

        if (val.isEmpty()){
            fullName.setError("Field cannot be empty");
            return false;
        }else{
            fullName.setError(null);
            fullName.setErrorEnabled(false);
            return true;
        }
    }

    private boolean validateUsername(){
        String val= username.getEditText().getText().toString().trim();
        String checkSpaces = "\\A\\w{1,20}\\z";

        if (val.isEmpty()){
            username.setError("Field cannot be empty");
            return false;
        }else if(val.length()>20){
            username.setError("Username cannot be more than 20 letters");
            return false;
        }else if(!val.matches(checkSpaces)){
            username.setError("Username cannot have white spaces");
            return false;
        } else{
            username.setError(null);
            username.setErrorEnabled(false);
            return true;
        }
    }

    private boolean validateEmail(){
        String val= emailID.getEditText().getText().toString().trim();
        String checkEmail = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

        if (val.isEmpty()){
            emailID.setError("Field cannot be empty");
            return false;
        }else if(!val.matches(checkEmail)){
            emailID.setError("Invalid Email");
            return false;
        } else{
            emailID.setError(null);
            emailID.setErrorEnabled(false);
            return true;
        }
    }

    private boolean validatePassword(){
        String val= newPassword.getEditText().getText().toString().trim();
        String checkPassword = "^"+
                "(?=.*[0-9])"+
                "(?=.*[a-zA-Z])"+
                "(?=.*[@#$&!^+=%])"+
                "(?=.*[?=\\S+$])"+
                ".{5,}"+"$";

        if (val.isEmpty()){
            newPassword.setError("Field cannot be empty");
            return false;
        }else if(!val.matches(checkPassword)){
            newPassword.setError("Password must contain at least 4 characters, a special character and a  number");
            return false;
        } else{
            newPassword.setError(null);
            newPassword.setErrorEnabled(false);
            return true;
        }
    }

    private boolean confirmPassword(){
        String val = confirmPassword.getEditText().getText().toString().trim();
        String confirm = newPassword.getEditText().getText().toString().trim();

        if (val.isEmpty()){
            confirmPassword.setError("Field cannot be empty");
            return false;
        }else if(!val.matches(confirm)){
            confirmPassword.setError("Password doesn't match!");
            return false;
        } else{
            confirmPassword.setError(null);
            confirmPassword.setErrorEnabled(false);
            return true;
        }
    }


}

