package com.example.likeyesterday.MyFriends;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;
import com.example.likeyesterday.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import java.util.ArrayList;
import java.util.List;

import static com.example.likeyesterday.MyFriends.FriendsListFragment.friendUid;
import static com.example.likeyesterday.ProfileFragment.currentUserDocumentReference;

public class FriendPlaceFeed extends AppCompatActivity {
    FeedAdapter feedAdapter;
    List<FeedItem> feedList;
    RecyclerView recyclerView;

    Toolbar toolbar;
    TextView placeNameFeedTextView;
    String ourPlaceDocID="";
    GeoPoint geoPointMarker;

    CollectionReference friendPlacesCollection ;
    CollectionReference placeInfoCollection;


//    @Override
//    protected void onStart() {
//        super.onStart();
//        if(ourPlaceDocID!=""){
//            Log.i("Feed os",friendUid);
//            Log.i("Feed os",ourPlaceDocID);
//            ListenerRegistration noteListener = placeInfoCollection.addSnapshotListener(this, new EventListener<QuerySnapshot>() {
//                @Override
//                public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
//                    if (error != null) {
//                        Toast.makeText(FriendPlaceFeed.this, "Error!", Toast.LENGTH_SHORT).show();
//                        Log.d("TAG", error.toString());
//                        return;
//                    }
//                    setRecyclerView();
//                }
//            });
//        }
//
//        if(feedAdapter!=null){
//            feedAdapter.notifyDataSetChanged();
//        }
//    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_place_feed);

        Log.i("Feed",friendUid);


        Intent intent= getIntent();
        String place=intent.getStringExtra("Place Name");
        double lng = intent.getDoubleExtra("lng",0);
        double lat= intent.getDoubleExtra("lat",0);
        geoPointMarker=new GeoPoint(lat,lng);
        getFeed(geoPointMarker);

        toolbar= findViewById(R.id.friendToolbar);
        placeNameFeedTextView =findViewById(R.id.placeNameFeedTextView);
        placeNameFeedTextView.setText(place);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");

        recyclerView =findViewById(R.id.feedRecyclerView);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);

    }

    public void getFeed(GeoPoint geoPoint){

        friendPlacesCollection =currentUserDocumentReference.collection("FriendsList").document(friendUid).collection("Our Places");

        friendPlacesCollection
                .whereEqualTo("geopoint",geoPoint)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                        if (queryDocumentSnapshots.size() == 0) {
                            Toast.makeText(FriendPlaceFeed.this, "Place doesn't exist", Toast.LENGTH_SHORT).show();
                            Log.d("friend", "Place doesn't exist");
                            return;
                        }

                        for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                            ourPlaceDocID = documentSnapshot.getId();
                        }
                        Log.i("Feed",friendUid);
                        Log.i("Feed",ourPlaceDocID);

                        feedList=new ArrayList<>();
                        Log.i("Feed","kkkkkkkkkkkkkkkk");

                        placeInfoCollection= friendPlacesCollection.document(ourPlaceDocID).collection("PlaceInfo");
                        placeInfoCollection.get()
                                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                    @Override
                                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                                        for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots){
                                            FeedItem feedItem=documentSnapshot.toObject(FeedItem.class);
                                            feedList.add(feedItem);
                                        }

                                        Log.i("Feed","kkkkkkkkkkkkkkkk");
                                        Log.i("Feed","kkkkkkkkkkkkkkkk"+feedList.toString());

                                        feedAdapter= new FeedAdapter(FriendPlaceFeed.this,feedList);
                                        recyclerView.setAdapter(feedAdapter);
                                        feedAdapter.notifyDataSetChanged();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(FriendPlaceFeed.this, "Something went wrong \n Please try again", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(FriendPlaceFeed.this, "Something went wrong \n Please try again", Toast.LENGTH_SHORT).show();
            }
        });
    }
}