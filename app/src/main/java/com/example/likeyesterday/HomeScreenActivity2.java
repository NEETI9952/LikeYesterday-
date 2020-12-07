package com.example.likeyesterday;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.likeyesterday.LoginSignup.StartActivity;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

public class HomeScreenActivity2 extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout drawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen2);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer= findViewById(R.id.drawer_layout);

        NavigationView navigationview = findViewById(R.id.navigation);
        navigationview.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,drawer,toolbar,R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.setDrawerIndicatorEnabled(true);
        toggle.syncState();

        if(savedInstanceState==null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new ProfileFragment()).commit();
            navigationview.setCheckedItem(R.id.nav_profile);
        }


    }

    @Override
    public void onBackPressed(){
        if(drawer.isDrawerOpen(GravityCompat.START)){
            drawer.closeDrawer(GravityCompat.START);
        }
        else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.nav_home:
//                Intent intent= new Intent(this,play_my_playlist.class);
//                startActivity(intent);
                break;
            case R.id.nav_friends:
//                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new music_library()).commit();

                break;
            case R.id.nav_my_places:
//                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new my_favourites()).commit();
                break;
            case R.id.nav_email_us:
                Intent emailIntent= new Intent(Intent.ACTION_SEND);
                emailIntent.setData(Uri.parse("mailto:"));
                //mail to protocol that lets you mail using client installed on your device
                String[] to={"neetibisht919@gmail.com"};
                emailIntent.putExtra(Intent.EXTRA_EMAIL,to);
                emailIntent.putExtra(Intent.EXTRA_SUBJECT,"Like Yesterday App");
                emailIntent.setType("message/rfc822");
                //specification for email
                Intent chooser=Intent.createChooser(emailIntent,"Send Email");
                startActivity(chooser);
                break;
            case R.id.nav_share:
//                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new music_library()).commit();
                break;
            case R.id.nav_profile:
                 getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new ProfileFragment()).commit();
                break;
            case R.id.nav_logout:
                new AlertDialog.Builder(this)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setTitle("Confirm Log out")
                        .setMessage("Are you sure you want to log out?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(HomeScreenActivity2.this," Successfully Logged Out",Toast.LENGTH_SHORT).show();
                                FirebaseAuth.getInstance().signOut();
                                Intent intent= new Intent(HomeScreenActivity2.this,StartActivity.class);
                                Log.i("Item selected","Log out");
                                startActivity(intent);

                            }
                        })
                        .setNegativeButton("No",null)
                        .show();
                break;
        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}