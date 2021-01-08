package com.example.likeyesterday.MyFriends;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.example.likeyesterday.R;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.example.likeyesterday.ProfileFragment.currentUserDocumentReference;
import static com.example.likeyesterday.MyFriends.FriendsListFragment.friendUid;

public class FriendActivity extends AppCompatActivity {

    CircleImageView friendProfilePictureChat;
    TextView friendNameChatTextView;
    Toolbar toolbar;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_friend);
//        setHasOptionsMenu(true);
        friendProfilePictureChat=findViewById(R.id.friendProfilePictureChat);
        friendNameChatTextView=findViewById(R.id.friendNameChatTextView);
        toolbar= findViewById(R.id.friendToolbar);
        friendNameChatTextView.setText(FriendsListFragment.friendName);

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");

        TabLayout tabLayout=findViewById(R.id.friendTabLayout);
        ViewPager viewPager=findViewById(R.id.friendViewPager);

        FriendActivity.AddFriendViewPagerAdapter addFriendViewPagerAdapter= new FriendActivity.AddFriendViewPagerAdapter(getSupportFragmentManager());
        addFriendViewPagerAdapter.addFragment(new FriendPlacesMapFragment(),"Map");
        addFriendViewPagerAdapter.addFragment(new FriendPlcesListFragment(),"Places");

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater= getMenuInflater();
        menuInflater.inflate(R.menu.friend_chat_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){

            case R.id.menu_remove_friend:
                new AlertDialog.Builder(this)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setTitle("Confirm Unfriend")
                        .setMessage("Are you sure you want to unfriend this user ?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                currentUserDocumentReference.collection("FriendsList").document(friendUid).delete();
                                Toast.makeText(FriendActivity.this,"User has been removed from your friend list",Toast.LENGTH_SHORT).show();
                                //back
                            }
                        })
                        .setNegativeButton("No",null)
                        .show();
                return true;

            case R.id.menu_delete_chat:
                new AlertDialog.Builder(this)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setTitle("Clear Chat")
                        .setMessage("Are you sure you want to clear chat with this user ?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(FriendActivity.this,"Chat deleted",Toast.LENGTH_SHORT).show();
//                                getFragmentManager().beginTransaction().replace(R.id.fragment_container,new ).commit();
                            }
                        })
                        .setNegativeButton("No",null)
                        .show();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
}