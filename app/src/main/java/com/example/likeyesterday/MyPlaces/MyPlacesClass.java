package com.example.likeyesterday.MyPlaces;

import android.os.Bundle;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.example.likeyesterday.R;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;

public class MyPlacesClass extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_my_places_class);

        TabLayout tabLayout=findViewById(R.id.addPlacesTabLayout);
        ViewPager viewPager=findViewById(R.id.addPlacesViewPager);

        AddFriendViewPagerAdapter addFriendViewPagerAdapter= new AddFriendViewPagerAdapter(getSupportFragmentManager());
        addFriendViewPagerAdapter.addFragment(new MyPlacesMapsFragment(),"Map");
        addFriendViewPagerAdapter.addFragment(new MyPlacesListFragment(),"Places List");

        viewPager.setAdapter(addFriendViewPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
    }

    class AddFriendViewPagerAdapter extends FragmentPagerAdapter {
        private ArrayList<Fragment> fragments;
        private ArrayList<String> titles;

        public AddFriendViewPagerAdapter(@NonNull FragmentManager fm) {
            super(fm);
            this.fragments=new ArrayList<>();
            this.titles=new ArrayList<>();
        }


        @NonNull
        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }

        public void addFragment(Fragment fragment, String title){
            fragments.add(fragment);
            titles.add(title);
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return titles.get(position);
        }
    }
}