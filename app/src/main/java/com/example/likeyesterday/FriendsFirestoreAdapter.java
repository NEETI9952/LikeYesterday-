package com.example.likeyesterday;

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

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentSnapshot;

import static com.example.likeyesterday.FriendsListFragment.friendUid;

public class FriendsFirestoreAdapter extends FirestoreRecyclerAdapter<FirestoreRecyclerModelClass,FriendsFirestoreAdapter.FriendsVH> {

    public FriendsFirestoreAdapter(@NonNull FirestoreRecyclerOptions<FirestoreRecyclerModelClass> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull FriendsFirestoreAdapter.FriendsVH friendsVH, int i, @NonNull FirestoreRecyclerModelClass firestoreRecyclerModelClass) {
        DocumentSnapshot documentSnapshot=getSnapshots().getSnapshot(friendsVH.getAdapterPosition());
        firestoreRecyclerModelClass.setFriendUid(documentSnapshot.getId());

        friendsVH.friendName.setText(firestoreRecyclerModelClass.getFullName());

        friendsVH.constraintLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                friendUid=String.valueOf(firestoreRecyclerModelClass.getFriendUid());
                Log.i("friendid","friendid "+friendUid );
//                Intent intent = new Intent(view.getContext(), HomeScreenActivity2.class);
//                view.getContext().startActivity(intent);

            }
        });


    }

    @NonNull
    @Override
    public FriendsFirestoreAdapter.FriendsVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v;

        v = LayoutInflater.from(parent.getContext()).inflate(R.layout.friendslistitem, parent, false);
//            return new workOrderVH(v);
        return new FriendsVH(v);
    }

    class FriendsVH extends RecyclerView.ViewHolder{

        TextView friendName;
        CardView cardView;
        ConstraintLayout constraintLayout;


        public FriendsVH(@NonNull View itemView) {
            super(itemView);
            friendName=itemView.findViewById(R.id.nameTextViewFriendsList);
            cardView=itemView.findViewById(R.id.cardView);
            constraintLayout=itemView.findViewById(R.id.constraintLayout);


        }
    }
}
