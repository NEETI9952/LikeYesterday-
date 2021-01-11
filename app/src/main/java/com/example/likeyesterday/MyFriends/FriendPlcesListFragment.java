package com.example.likeyesterday.MyFriends;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.example.likeyesterday.FirestoreRecyclerModelClass;
import com.example.likeyesterday.R;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Objects;

import static com.example.likeyesterday.MyFriends.FriendsListFragment.friendUid;
import static com.example.likeyesterday.ProfileFragment.currentUserDocumentReference;
import static com.example.likeyesterday.MyFriends.FriendsListFragment.progressBar;
import static com.example.likeyesterday.MyFriends.FriendsListFragment.emptyListIV;

public class FriendPlcesListFragment extends Fragment {

    public static String PlaceName;
    public static GeoPoint geopoint;

    static ArrayList<String> places = new ArrayList<String>();
    static ArrayList<LatLng> locations = new ArrayList<LatLng>();

    private RecyclerView recyclerView;
    private FriendsPlacesFirestoreAdapter friendsPlacesFirestoreAdapter;
    int count;

    private Query query;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        ViewGroup root=(ViewGroup)inflater.inflate(R.layout.fragment_friend_plces_list, container, false);
        recyclerView=root.findViewById(R.id.placesList);
        emptyListIV=root.findViewById(R.id.imageViewAddFriendsPlacesListEmpty);
        emptyListIV.setImageResource(R.drawable.undraw_empty_xct9);
        progressBar=root.findViewById(R.id.progressBarFriendsPlacesList);

        setRecyclerView();


        return root;
    }

    private void setRecyclerView() {

       Query query= (Query) currentUserDocumentReference.collection("FriendsList").document(friendUid).collection("Our Places");

//        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                Log.i("testingCountof","no of places="+task.getResult().size());
//                if(task.getResult().isEmpty()){
//                    progressBar.setVisibility(View.INVISIBLE);
//                    emptyListIV.setVisibility(View.VISIBLE);
//                }
//            }
//        });


        FirestoreRecyclerOptions<FirestoreRecyclerModelClass> options=new FirestoreRecyclerOptions.Builder<FirestoreRecyclerModelClass>().setQuery(query,FirestoreRecyclerModelClass.class).build();
        friendsPlacesFirestoreAdapter=new FriendsPlacesFirestoreAdapter(getContext(),options);


        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(friendsPlacesFirestoreAdapter);
        Log.i("tesingcount",String.valueOf(friendsPlacesFirestoreAdapter.getItemCount()));

    }



    @Override
    public void onStart() {
        super.onStart();
        friendsPlacesFirestoreAdapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        friendsPlacesFirestoreAdapter.stopListening();
//        progressBar= Objects.requireNonNull(getView()).findViewById(R.id.progressBarFriendsPlacesList);
//        progressBar.setVisibility(View.INVISIBLE);
//        Log.i("testinglifecycle","progress bar is not visible");
//        emptyListIV=getView().findViewById(R.id.imageViewAddFriendsPlacesListEmpty);
//        emptyListIV.setImageResource(R.drawable.undraw_empty_xct9);
//        emptyListIV.setVisibility(View.INVISIBLE);
//        Log.i("testinglifecycle","imageview is not visible");
//        Log.i("testinglifecycle","reached ran stop");
    }
    @Override
    public void onResume() {
        super.onResume();
//        if(friendsPlacesFirestoreAdapter.getItemCount()>=1){
////            hideProgressbar();
////            hideImageView();
//            Log.i("testinglifecycle","item count more than 1 on resume");
//        }else{
//            if(emptyListIV.getVisibility()==View.VISIBLE){
//                Log.i("testinglifecycle","imageview is visible on resume");
//
//            }
//            progressBar=getView().findViewById(R.id.progressBarFriendsPlacesList);
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