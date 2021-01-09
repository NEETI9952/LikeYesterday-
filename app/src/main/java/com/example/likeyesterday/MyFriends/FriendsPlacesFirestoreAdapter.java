package com.example.likeyesterday.MyFriends;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.likeyesterday.FirestoreRecyclerModelClass;
import com.example.likeyesterday.MyPlaces.MyPacesFirestoreAdapter;
import com.example.likeyesterday.MyPlaces.MyPlaceMapActivity;
import com.example.likeyesterday.R;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentSnapshot;

import static com.example.likeyesterday.MyFriends.FriendsListFragment.hideProgressbar;
import static com.example.likeyesterday.MyFriends.FriendsListFragment.showProgressbar;
import static com.example.likeyesterday.MyPlaces.MyPlacesListFragment.PlaceName;
import static com.example.likeyesterday.MyPlaces.MyPlacesListFragment.geopoint;

public class FriendsPlacesFirestoreAdapter extends FirestoreRecyclerAdapter<FirestoreRecyclerModelClass,FriendsPlacesFirestoreAdapter.FriendsVH> {

    Context context;

    public FriendsPlacesFirestoreAdapter(Context context, @NonNull FirestoreRecyclerOptions<FirestoreRecyclerModelClass> options) {
        super(options);
        this.context = context;
    }

    @Override
    protected void onBindViewHolder(@NonNull FriendsPlacesFirestoreAdapter.FriendsVH friendsVH, int i, @NonNull FirestoreRecyclerModelClass firestoreRecyclerModelClass) {
        DocumentSnapshot documentSnapshot=getSnapshots().getSnapshot(friendsVH.getAdapterPosition());
//        firestoreRecyclerModelClass.setFriendUid(documentSnapshot.getId());

        friendsVH.placeNameTV.setText(firestoreRecyclerModelClass.getPlaceName());


        friendsVH.constraintLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PlaceName=String.valueOf(firestoreRecyclerModelClass.getPlaceName());
                geopoint=firestoreRecyclerModelClass.getgeopoint();
                Log.i("Places","location "+geopoint );
                double lat = geopoint.getLatitude();
                double lng = geopoint.getLongitude ();
//                LatLng latLng = new LatLng(lat, lng);
                Intent intent= new Intent(context, FriendPlaceFeed.class);
                intent.putExtra("lat",lat);
                intent.putExtra("lng",lng);
                intent.putExtra("Place Name",PlaceName);
                context.startActivity(intent);
            }
        });
    }

    @NonNull
    @Override
    public FriendsPlacesFirestoreAdapter.FriendsVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v;
        v = LayoutInflater.from(parent.getContext()).inflate(R.layout.my_places_list_row, parent, false);
        return new FriendsPlacesFirestoreAdapter.FriendsVH(v);
    }

    class FriendsVH extends RecyclerView.ViewHolder{
        TextView placeNameTV;
        CardView cardView;
        ConstraintLayout constraintLayout;


        public FriendsVH(@NonNull View itemView) {
            super(itemView);
            placeNameTV=itemView.findViewById(R.id.placeNameTextViewPlaceList);
            cardView=itemView.findViewById(R.id.cardView);
            constraintLayout=itemView.findViewById(R.id.constraintLayout);
            Log.i("tesingcount","asdfasdfas"+String.valueOf(getItemCount()));

        }
    }

    @Override
    public int getItemCount() {
        return super.getItemCount();
    }

    @Override
    public void onDataChanged() {
        super.onDataChanged();
        Log.i("testinglifecycle","reached ran");

        if(getItemCount()>=1){
            hideProgressbar();
        }
        else{
            showProgressbar();
        }
    }
}
