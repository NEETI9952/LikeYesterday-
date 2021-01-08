package com.example.likeyesterday.MyFriends;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.likeyesterday.FirestoreRecyclerModelClass;
import com.example.likeyesterday.R;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import static com.example.likeyesterday.ProfileFragment.currentUserDocumentReference;
import static com.example.likeyesterday.ProfileFragment.uid;

public class FriendsListFragment extends Fragment {

    public static final int PERMISSIONS_REQUEST_READ_CONTACTS = 1;
    FloatingActionButton add;

    public static String friendUid="";
    public static String friendName;

    private RecyclerView recyclerView;
    private FriendsFirestoreAdapter friendsFirestoreAdapter;
    private ProgressBar progressBar;
    private ImageView emptyListIV;

//    public static String uid;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        ViewGroup root=(ViewGroup) inflater.inflate(R.layout.fragment_friends_list, container, false);
        recyclerView=root.findViewById(R.id.recyclerRequests);
        emptyListIV=root.findViewById(R.id.imageViewAddFriendsListEmpty);
        progressBar=root.findViewById(R.id.progressBarFriendsList);
        add=root.findViewById(R.id.addFriendButton);


        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requestContactPermission();
            }
        });

        setRecyclerView();
        return  root;
    }

    private void setRecyclerView() {

        Query query=currentUserDocumentReference.collection("FriendsList");
        progressBar.setVisibility(View.INVISIBLE);
        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                Log.i("testingCountofplaces","no of friends="+task.getResult().size());
                if(task.getResult().isEmpty()){
                    emptyListIV.setImageResource(R.drawable.undraw_empty_xct9);
                    emptyListIV.setVisibility(View.VISIBLE);
                }
            }
        });
        FirestoreRecyclerOptions<FirestoreRecyclerModelClass> options=new FirestoreRecyclerOptions.Builder<FirestoreRecyclerModelClass>().setQuery(query,FirestoreRecyclerModelClass.class).build();
        friendsFirestoreAdapter=new FriendsFirestoreAdapter(getContext(),options);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(friendsFirestoreAdapter);
    }

    @Override
    public void onStart() {
        super.onStart();
        friendsFirestoreAdapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        friendsFirestoreAdapter.stopListening();
    }

    public void requestContactPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(getContext(), android.Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), android.Manifest.permission.READ_CONTACTS)) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    builder.setTitle("Read Contacts permission");
                    builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent= new Intent(getActivity(), AddFriendsClass.class);
                            startActivity(intent);
                            ((Activity) getActivity()).overridePendingTransition(0, 0);
                        }
                    });
                    builder.setMessage("Please enable access to contacts.");
                    builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
                        @TargetApi(Build.VERSION_CODES.M)
                        @Override
                        public void onDismiss(DialogInterface dialog) {
                            requestPermissions(new String[]{android.Manifest.permission.READ_CONTACTS}, PERMISSIONS_REQUEST_READ_CONTACTS);
                        }
                    });
                    builder.show();
                } else {
                    ActivityCompat.requestPermissions(getActivity(), new String[]{android.Manifest.permission.READ_CONTACTS}, PERMISSIONS_REQUEST_READ_CONTACTS);
                }
            } else {
                Intent intent= new Intent(getActivity(), AddFriendsClass.class);
                startActivity(intent);
                ((Activity) getActivity()).overridePendingTransition(0, 0);
//                FragmentTransaction transaction = getFragmentManager().beginTransaction();
//                transaction.replace(R.id.fragment_container, new AddFriendsFragment()).addToBackStack(null);
//                transaction.commit();
            }
        } else {
//            FragmentTransaction transaction = getFragmentManager().beginTransaction();
//            transaction.replace(R.id.fragment_container, new AddFriendsFragment()).addToBackStack(null);
//            transaction.commit();
            Intent intent= new Intent(getActivity(),AddFriendsClass.class);
            startActivity(intent);
            ((Activity) getActivity()).overridePendingTransition(0, 0);

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,@NonNull int[] grantResults) {
        switch (requestCode) {
            case PERMISSIONS_REQUEST_READ_CONTACTS: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                    FragmentTransaction transaction = getFragmentManager().beginTransaction();
//                    transaction.replace(R.id.fragment_container,new AddFriendsFragment()).addToBackStack(null);
//                    transaction.commit();
                    Intent intent= new Intent(getActivity(), AddFriendsClass.class);
                    startActivity(intent);
                    ((Activity) getActivity()).overridePendingTransition(0, 0);
                } else {
                    Toast.makeText(getContext(), "You have disabled a contacts permission", Toast.LENGTH_LONG).show();
                }
            }
        }
    }


}