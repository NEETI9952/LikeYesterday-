package com.example.likeyesterday.MyFriends;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.likeyesterday.FirestoreRecyclerModelClass;
import com.example.likeyesterday.R;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.HashMap;
import java.util.Map;

import static com.example.likeyesterday.MyFriends.FriendsListFragment.friendName;
import static com.example.likeyesterday.MyFriends.FriendsListFragment.friendUid;
import static com.example.likeyesterday.ProfileFragment.currentUserDocumentReference;
import static com.example.likeyesterday.ProfileFragment.db;

public class FriendsFirestoreAdapter extends FirestoreRecyclerAdapter<FirestoreRecyclerModelClass,FriendsFirestoreAdapter.FriendsVH> {

    Context context;

    public FriendsFirestoreAdapter(Context context,@NonNull FirestoreRecyclerOptions<FirestoreRecyclerModelClass> options) {
        super(options);
        this.context = context;
    }

    @Override
    protected void onBindViewHolder(@NonNull FriendsFirestoreAdapter.FriendsVH friendsVH, int i, @NonNull FirestoreRecyclerModelClass firestoreRecyclerModelClass) {
        DocumentSnapshot documentSnapshot=getSnapshots().getSnapshot(friendsVH.getAdapterPosition());
        firestoreRecyclerModelClass.setFriendUid(documentSnapshot.getId());

        friendsVH.friendName.setText(firestoreRecyclerModelClass.getFullName());
        friendsVH.friendPhoneNumber.setText(firestoreRecyclerModelClass.getPhoneNumber());

        friendsVH.constraintLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                friendUid=String.valueOf(firestoreRecyclerModelClass.getFriendUid());
                friendName=String.valueOf(firestoreRecyclerModelClass.getFullName());


                Log.i("friendid","friendid "+friendUid );

                DocumentReference oldDocID = currentUserDocumentReference.collection("FriendsList").document(friendUid);
                oldDocID.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()){
                            DocumentSnapshot documentOld=task.getResult();

                            if (documentOld.exists()){
                                FriendFragment fragmentFriend= new FriendFragment();
//                                Bundle args = new Bundle();
//                                args.putString("friendUid",friendUid);
//                                args.putString("friendName",friendName);
//                                Log.i("friend","friendName "+friendName);
//                                fragmentFriend.setArguments(args);
//                                String backStateName = ((AppCompatActivity)context).getSupportFragmentManager().ge.getClass().getName();
                                Intent intent=new Intent(context,FriendActivity.class);
                                context.startActivity(intent);
//                                FragmentManager fragmentManager=((AppCompatActivity)context).getSupportFragmentManager();
//                                fragmentManager.beginTransaction().replace(R.id.fragment_container,new FriendFragment()).addToBackStack(null).commit();

                            }else {

                                DocumentReference docID = db.collection("Users").document(friendUid);
                                docID.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                        if (task.isSuccessful()) {
                                            DocumentSnapshot document = task.getResult();

                                            if (document.exists()) {
                                                String friendFullName = document.get("Full Name").toString();
                                                String friendPhoneNumber = document.get("Phone Number").toString();

                                                Map<String, Object> friend = new HashMap<>();
                                                friend.put("FullName", friendFullName);
                                                friend.put("PhoneNumber", friendPhoneNumber);

                                                CollectionReference friendListReference = currentUserDocumentReference.collection("FriendsList");
                                                friendListReference.document(friendUid).set(friend).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void aVoid) {
                                                        currentUserDocumentReference.get()
                                                                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                                                    @Override
                                                                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                                                                        if (documentSnapshot.exists()) {

//                                                                            int noOfFriends = Integer.parseInt(documentSnapshot.get("Number of friends").toString());
//                                                                            currentUserDocumentReference.update("Number of friends", noOfFriends + 1);
//                                                                            int noOfRequests = Integer.parseInt(documentSnapshot.get("Number of requests").toString());
//                                                                            currentUserDocumentReference.update("Number of requests", noOfRequests - 1);

                                                                            CollectionReference requestListReference = currentUserDocumentReference.collection("Request List");
                                                                            requestListReference.document(friendUid).delete();
                                                                            Toast.makeText(context, "User added to your friend list", Toast.LENGTH_SHORT).show();
                                                                        }
                                                                    }
                                                                })
                                                                .addOnFailureListener(new OnFailureListener() {
                                                                    @Override
                                                                    public void onFailure(@NonNull Exception e) {
                                                                        Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT).show();
                                                                        Log.d("Add friend", e.toString());
                                                                    }
                                                                });
                                                    }
                                                });
                                            }
                                        }
                                    }
                                });
                            }
                        }else {
                        Log.d("Friend", "Failed with: ", task.getException());
                        }
                    }
                });
            }
        });
    }

    @NonNull
    @Override
    public FriendsFirestoreAdapter.FriendsVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v;
        v = LayoutInflater.from(parent.getContext()).inflate(R.layout.friendslistitem, parent, false);
        return new FriendsVH(v);
    }

    class FriendsVH extends RecyclerView.ViewHolder{

        TextView friendName;
        TextView friendPhoneNumber;
        CardView cardView;
        ConstraintLayout constraintLayout;


        public FriendsVH(@NonNull View itemView) {
            super(itemView);
            friendName=itemView.findViewById(R.id.nameTextViewFriendsList);
            friendPhoneNumber=itemView.findViewById(R.id.phoneNumberTextViewFriendsList);
            cardView=itemView.findViewById(R.id.cardView);
            constraintLayout=itemView.findViewById(R.id.constraintLayout);


        }
    }
}
