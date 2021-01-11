package com.example.likeyesterday.MyFriends;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.likeyesterday.FirestoreRecyclerModelClass;
import com.example.likeyesterday.MyFriends.FriendsFirestoreAdapter;
import com.example.likeyesterday.R;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import static com.example.likeyesterday.MyFriends.FriendsListFragment.progressBar;
import static com.example.likeyesterday.MyFriends.FriendsListFragment.emptyListIV;

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
        emptyListIV=root.findViewById(R.id.imageViewRequestsListEmpty);
        emptyListIV.setImageResource(R.drawable.undraw_empty_xct9);
        progressBar=root.findViewById(R.id.progressBarRequestsList);

        setRecyclerView();
        return  root;
    }

    private void setRecyclerView() {

        Query query=currentUserDocumentReference.collection("Request List").orderBy("FullName", Query.Direction.ASCENDING);
        Log.i("Request List",uid);

//        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                Log.i("testingCountof","Request="+task.getResult().size());
//                if(task.getResult().isEmpty()){
//                    progressBar.setVisibility(View.INVISIBLE);
//
//                    emptyListIV.setVisibility(View.VISIBLE);
//                }
//            }
//        });

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


//        friendsFirestoreAdapter.stopListening();
//        progressBar=getView().findViewById(R.id.progressBarRequestsList);
//        progressBar.setVisibility(View.INVISIBLE);
//        Log.i("testinglifecycle","progress bar is not visible on stop request list ");
//        emptyListIV=getView().findViewById(R.id.imageViewRequestsListEmpty);
//
//        emptyListIV.setImageResource(R.drawable.undraw_empty_xct9);
//        emptyListIV.setVisibility(View.INVISIBLE);
//        Log.i("testinglifecycle","imageview is not visible ib stio reuest list");
//
//        Log.i("testinglifecycle","reached ran stop request list");
    }
    @Override
    public void onResume() {
        super.onResume();

//        if(friendsFirestoreAdapter.getItemCount()>=1){
////            hideProgressbar();
////            hideImageView();
//            Log.i("testinglifecycle","item count more than 1 on resume");
//        }else{
//            if(emptyListIV.getVisibility()==View.VISIBLE){
//                Log.i("testinglifecycle","imageview is visible on resume");
//
//            }
//            progressBar=getView().findViewById(R.id.progressBarRequestsList);
//            progressBar.setVisibility(View.VISIBLE);
//            emptyListIV.setVisibility(View.INVISIBLE);
//            Log.i("testinglifecycle","imageview is not visible on resume");
//            Log.i("testinglifecycle","progress bar is  visible on resume");
//            Log.i("testinglifecycle","item count less than 1 on resume");
////
//        }
//////        progressBar=getView().findViewById(R.id.progressBarFriendsList);
////        progressBar.setVisibility(View.VISIBLE);
//        Log.i("testinglifecycle","reached ran Onresume");
    }




}