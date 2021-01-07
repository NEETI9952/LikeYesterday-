package com.example.likeyesterday.MyFriends;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.likeyesterday.R;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.example.likeyesterday.ProfileFragment.currentUserDocumentReference;


public class FriendFragment extends Fragment {

    CircleImageView friendProfilePictureChat;
    TextView friendNameChatTextView;
    Toolbar toolbar;

    String friendUid;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        ViewGroup root= (ViewGroup) inflater.inflate(R.layout.fragment_friend, container, false);
        friendProfilePictureChat=root.findViewById(R.id.friendProfilePictureChat);
        friendNameChatTextView=root.findViewById(R.id.friendNameChatTextView);
        toolbar= root.findViewById(R.id.friendToolbar);
        friendNameChatTextView.setText(FriendsListFragment.friendName);
        friendUid=FriendsListFragment.friendUid;

//        Bundle args = getArguments();
//        if (args != null) {
//            friendUid=args.getString("friendUid");
//            friendName=args.getString("friendName");
//            Log.i("friend","friendName "+friendName);
//            Log.i("friend","friendUid "+friendUid);
//        }
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("");
        return root;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        MenuInflater menuInflater= ((AppCompatActivity)getActivity()).getMenuInflater();
        menuInflater.inflate(R.menu.friend_chat_menu,menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){

            case R.id.menu_remove_friend:
                new AlertDialog.Builder(getContext())
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setTitle("Confirm Unfriend")
                        .setMessage("Are you sure you want to unfriend this user ?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                currentUserDocumentReference.collection("FriendsList").document(friendUid).delete();
                                Toast.makeText(getContext(),"User has been removed from your friend list",Toast.LENGTH_SHORT).show();
                                getFragmentManager().beginTransaction().replace(R.id.fragment_container,new FriendsListFragment()).commit();
                            }
                        })
                        .setNegativeButton("No",null)
                        .show();
                return true;

            case R.id.menu_delete_chat:
                new AlertDialog.Builder(getContext())
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setTitle("Clear Chat")
                        .setMessage("Are you sure you want to clear chat with this user ?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(getContext(),"Chat deleted",Toast.LENGTH_SHORT).show();
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