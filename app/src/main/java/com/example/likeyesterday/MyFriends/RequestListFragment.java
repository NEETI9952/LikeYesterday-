package com.example.likeyesterday.MyFriends;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.likeyesterday.FirestoreRecyclerModelClass;
import com.example.likeyesterday.MyFriends.FriendsFirestoreAdapter;
import com.example.likeyesterday.R;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.Query;

import static com.example.likeyesterday.ProfileFragment.currentUserDocumentReference;

public class RequestListFragment extends Fragment {

    private RecyclerView recyclerView;
    private FriendsFirestoreAdapter friendsFirestoreAdapter;

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

        Query query=currentUserDocumentReference.collection("Request List").orderBy("FullName", Query.Direction.ASCENDING);

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