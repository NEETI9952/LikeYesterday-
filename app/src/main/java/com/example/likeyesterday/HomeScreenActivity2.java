package com.example.likeyesterday;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.navigation.NavigationView;

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
//                Intent intent= new Intent(this,play_my_playlist.class);
//                startActivity(intent);
                break;
            case R.id.nav_share:
//                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new music_library()).commit();
                break;
            case R.id.nav_profile:
//                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new my_favourites()).commit();
                break;
            case R.id.nav_logout:
//                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new my_favourites()).commit();
                break;
        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}