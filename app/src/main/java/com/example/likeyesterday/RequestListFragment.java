package com.example.likeyesterday;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class RequestListFragment extends Fragment {

    public static String friendUid;

    public static FirebaseFirestore db= FirebaseFirestore.getInstance();

    public static CollectionReference userColRef=db.collection("Users");

    private RecyclerView recyclerView;
    private FriendsFirestoreAdapter  friendsFirestoreAdapter;

    public static FirebaseAuth mAuth;
    public static String uid;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_friends_list, container, false);
        ViewGroup root=(ViewGroup) inflater.inflate(R.layout.fragment_requests_list, container, false);

        mAuth = FirebaseAuth.getInstance();
        uid = mAuth.getCurrentUser().getUid();

        recyclerView=root.findViewById(R.id.recyclerRequests);

        setRecyclerView();
        return  root;
    }

    private void setRecyclerView() {

        Query query=userColRef.document(uid).collection("Request List").orderBy("FullName", Query.Direction.ASCENDING);

        Log.i("Request List",uid);

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




}