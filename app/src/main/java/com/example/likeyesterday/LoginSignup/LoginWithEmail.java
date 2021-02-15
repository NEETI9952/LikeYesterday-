package com.example.likeyesterday.LoginSignup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.util.Pair;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.likeyesterday.HomeScreenActivity2;
import com.example.likeyesterday.MainActivity;
import com.example.likeyesterday.ProfileFragment;
import com.example.likeyesterday.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.hbb20.CountryCodePicker;

import static android.widget.Toast.LENGTH_SHORT;

public class LoginWithEmail extends AppCompatActivity implements View.OnKeyListener {
    ImageView backButton;
    Button createUser,login;
    TextView titleText;
    TextInputLayout emailID=null,password=null;
    CheckBox checkBox;

    public static FirebaseAuth mAuth = FirebaseAuth.getInstance();

    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {

        if(keyCode==KeyEvent.KEYCODE_ENTER && event.getAction()==KeyEvent.ACTION_DOWN){
            onLogin(v);
        }

        return false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_with_email);

        backButton=findViewById(R.id.loginBackButton);
        createUser=findViewById(R.id.loginCreateUser);
        login=findViewById(R.id.loginButton);
        titleText=findViewById(R.id.loginTextView);
        emailID=findViewById(R.id.loginEmailTextView);
        password=findViewById(R.id.loginpassword);
        checkBox=findViewById(R.id.rememberMeCheckBox);

        password.setOnKeyListener(this);

        if (mAuth.getCurrentUser() != null) {
            login();
        }
    }

    public void  callSignUpScreen(View view){

        Intent intent= new Intent(getApplicationContext(),Signup.class);

        Pair[] pairs= new Pair[4];
        pairs[0]= new Pair<View,String>(backButton,"back_image_transition");
        pairs[1]= new Pair<View,String>(titleText,"createAccount_text_transition");
        pairs[2]= new Pair<View,String>(login,"next_button_transition");
        pairs[3]= new Pair<View,String>(createUser,"login_button_transition");

        ActivityOptions options=ActivityOptions.makeSceneTransitionAnimation(LoginWithEmail.this,pairs);
        startActivity(intent,options.toBundle());

    }

    public void login() {
        //move to next activity
        Intent intent = new Intent(this, HomeScreenActivity2.class);
        startActivity(intent);
    }

    public void onLogin(View view){

        if(!validateFields()){
            return;
        }
        mAuth.signInWithEmailAndPassword(emailID.getEditText().getText().toString().trim(), password.getEditText().getText().toString().trim())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            login();
                        } else {
                            Toast.makeText(LoginWithEmail.this, "Login failed. Try Again", LENGTH_SHORT).show();
                            // If sign in fails, sign up.

                        }
                    }
                });

    }

    private boolean validateFields() {

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

    public void forgetPassword(View view){
        if(!emailID.getEditText().getText().toString().trim().isEmpty()){

            mAuth.sendPasswordResetEmail(emailID.getEditText().getText().toString().trim())
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(LoginWithEmail.this, "Email has been successfully sent", LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(LoginWithEmail.this, "Something went wrong. Please check your emailID", LENGTH_SHORT).show();
                            }
                        }
                    });
        }else{
            Toast.makeText(LoginWithEmail.this, "Please enter email address", LENGTH_SHORT).show();
        }

    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

}

//        InputMethodManager inputMethodManager=(InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
//        inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),0);
