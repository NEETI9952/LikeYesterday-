package com.example.likeyesterday.LoginSignup;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import com.example.likeyesterday.R;

public class StartActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_start);

    }
    public void goToLoginScreen(View v){
        Intent intent= new Intent(getApplicationContext(),Login.class);

        Pair[] pairs = new Pair[1];
        pairs[0]=new Pair<View,String>(findViewById(R.id.loginButton),"loginTransition");

        ActivityOptions options=ActivityOptions.makeSceneTransitionAnimation(StartActivity.this,pairs);
        startActivity(intent,options.toBundle());
    }

    public void  callSignUpScreen(View view){

        Intent intent= new Intent(getApplicationContext(),Signup.class);

//        Pair[] pairs= new Pair[4];
//        pairs[0]= new Pair<View,String>(backButton,"back_image_transition");
//        pairs[1]= new Pair<View,String>(titleText,"createAccount_text_transition");
//        pairs[2]= new Pair<View,String>(login,"next_button_transition");
//        pairs[3]= new Pair<View,String>(createUser,"login_button_transition");
//
//        ActivityOptions options=ActivityOptions.makeSceneTransitionAnimation(Login.this,pairs);
//        startActivity(intent,options.toBundle());
        startActivity(intent);
    }

}