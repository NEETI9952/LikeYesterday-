package com.example.likeyesterday.LoginSignup;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import com.example.likeyesterday.R;
import java.util.Calendar;

public class SignUp2 extends AppCompatActivity {
    ImageView backButton;
    Button next,login;
    TextView titleText;
    RadioGroup radioGroup;
    RadioButton radioButtonGender;
    DatePicker datePicker;
    String gender,dob;
    int day,month,year;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_sign_up2);

        backButton=findViewById(R.id.signup2BackButton);
        next=findViewById(R.id.signUp2NextButton);
        login=findViewById(R.id.signUp2LoginButton);
        titleText=findViewById(R.id.signUp2Title);

        radioGroup=findViewById(R.id.radioGroup);
        datePicker=findViewById(R.id.datePicker);

    }

    public void  callSignUpScreen3(View view) {

        if (!validateAge()|!validateGender()){
            return;
        }

        radioButtonGender=findViewById(radioGroup.getCheckedRadioButtonId());
        gender=radioButtonGender.getText().toString();

        day=datePicker.getDayOfMonth();
        month=datePicker.getMonth();
        year=datePicker.getYear();
        dob=day+"/"+"/"+month+"/"+year;

        String fullName=getIntent().getStringExtra("Fullname");
        String emailID=getIntent().getStringExtra("EmailID");
        String username=getIntent().getStringExtra("Username");
        String newPassword=getIntent().getStringExtra("Password");


        Intent intent = new Intent(getApplicationContext(), SignUp3.class);

        intent.putExtra("Fullname",fullName);
        intent.putExtra("EmailID",emailID);
        intent.putExtra("Username",username);
        intent.putExtra("Password",newPassword);
        intent.putExtra("Gender",gender);
        intent.putExtra("DOB",dob);

        Pair[] pairs = new Pair[4];
        pairs[0] = new Pair<View, String>(backButton, "back_image_transition");
        pairs[1] = new Pair<View, String>(titleText, "createAccount_text_transition");
        pairs[2] = new Pair<View, String>(login, "login_button_transition");
        pairs[3] = new Pair<View, String>(next, "next_button_transition");

        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(SignUp2.this, pairs);
        startActivity(intent, options.toBundle());
    }

    public void  callLogin(View view){

        Intent intentLogin= new Intent(getApplicationContext(),Login.class);

        Pair[] pairs= new Pair[4];
        pairs[0]= new Pair<View,String>(backButton,"back_image_transition");
        pairs[1]= new Pair<View,String>(titleText,"createAccount_text_transition");
        pairs[2]= new Pair<View,String>(login,"login_button_transition");
        pairs[3]= new Pair<View,String>(next,"next_button_transition");

        ActivityOptions options=ActivityOptions.makeSceneTransitionAnimation(SignUp2.this,pairs);
        startActivity(intentLogin,options.toBundle());
    }

    private boolean validateGender(){
        if(radioGroup.getCheckedRadioButtonId()==-1){
            Toast.makeText(this,"Please select a gender",Toast.LENGTH_SHORT).show();
            return false;
        }else{
            return true;
        }
    }

    private boolean validateAge(){
        int currentYear= Calendar.getInstance().get(Calendar.YEAR);
        int userDOB=datePicker.getYear();
        int isDOBValid=currentYear-userDOB;

        if(isDOBValid<1){
            Toast.makeText(this,"Invalid Age",Toast.LENGTH_SHORT).show();
            return false;
        }else if(isDOBValid>110 | isDOBValid<12){
            Toast.makeText(this,"You are not eligible to apply",Toast.LENGTH_SHORT).show();
            return false;
        }else{
            return true;
        }
    }

}