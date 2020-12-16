package com.example.likeyesterday.LoginSignup;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Pair;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import com.example.likeyesterday.HomeScreenActivity2;
import com.example.likeyesterday.R;

import static com.example.likeyesterday.LoginSignup.LoginWithEmail.mAuth;

public class StartActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_start);

        if (mAuth.getCurrentUser() != null) {
            login();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mAuth.getCurrentUser() != null) {
            login();
        }
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
        startActivity(intent);
    }

    public void login() {
        //move to next activity
        Intent intent = new Intent(this, HomeScreenActivity2.class);
        startActivity(intent);
    }

    private boolean doubleBackToExitPressedOnce;
    private Handler mHandler = new Handler();

    private final Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            doubleBackToExitPressedOnce = false;
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mHandler != null) { mHandler.removeCallbacks(mRunnable); }
    }

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            this.finishAffinity();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please press back again to exit", Toast.LENGTH_SHORT).show();

        mHandler.postDelayed(mRunnable, 2000);
    }
}