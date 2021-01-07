package com.example.likeyesterday.MyFriends;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.view.WindowManager;

import com.example.likeyesterday.R;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;

public class AddFriendsClass extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_add_friends_class);

        TabLayout tabLayout=findViewById(R.id.friendTabLayout);
        ViewPager viewPager=findViewById(R.id.friendViewPager);

        AddFriendViewPagerAdapter addFriendViewPagerAdapter= new AddFriendViewPagerAdapter(getSupportFragmentManager());
        addFriendViewPagerAdapter.addFragment(new AddFriendsFragment(),"Add Friends");
        addFriendViewPagerAdapter.addFragment(new RequestListFragment(),"Friend Requests");

        viewPager.setAdapter(addFriendViewPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
    }

    class AddFriendViewPagerAdapter extends FragmentPagerAdapter {
        private ArrayList<androidx.fragment.app.Fragment> fragments;
        private ArrayList<String> titles;

        public AddFriendViewPagerAdapter(@NonNull FragmentManager fm) {
            super(fm);
            this.fragments=new ArrayList<>();
            this.titles=new ArrayList<>();
        }


        @NonNull
        @Override
        public androidx.fragment.app.Fragment getItem(int position) {
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