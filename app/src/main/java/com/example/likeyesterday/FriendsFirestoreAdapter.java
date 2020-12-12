package com.example.likeyesterday;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

public class FriendsFirestoreAdapter extends FirestoreRecyclerAdapter<FirestoreRecyclerModelClass,FriendsFirestoreAdapter.FriendsVH> {

    public FriendsFirestoreAdapter(@NonNull FirestoreRecyclerOptions<FirestoreRecyclerModelClass> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull FriendsFirestoreAdapter.FriendsVH friendsVH, int i, @NonNull FirestoreRecyclerModelClass firestoreRecyclerModelClass) {

        friendsVH.friendName.setText(firestoreRecyclerModelClass.getFullName());


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


        public FriendsVH(@NonNull View itemView) {
            super(itemView);
            friendName=itemView.findViewById(R.id.nameTextViewFriendsList);


        }
    }
}
