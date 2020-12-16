package com.example.likeyesterday;

import android.content.Context;
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

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

import static com.example.likeyesterday.FriendsListFragment.friendUid;

public class FriendsFirestoreAdapter extends FirestoreRecyclerAdapter<FirestoreRecyclerModelClass,FriendsFirestoreAdapter.FriendsVH> {

    FirebaseFirestore db= FirebaseFirestore.getInstance();
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    String uid = user.getUid();
    Context context;
    String currentUserName;
    String currentUserPhoneNumber;

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
                Log.i("friendid","friendid "+friendUid );

                DocumentReference oldDocID = db.collection("Users").document(uid).collection("FriendsList").document(friendUid);
                oldDocID.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()){
                            DocumentSnapshot documentOld=task.getResult();

                            if (documentOld.exists()){
                                FragmentManager fragmentManager=((AppCompatActivity)context).getSupportFragmentManager();
                                fragmentManager.beginTransaction().replace(R.id.fragment_container,new FriendFragment()).commit();

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

                                                CollectionReference friendListReference = db.collection("Users").document(uid).collection("FriendsList");

                                                friendListReference.document(friendUid).set(friend).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void aVoid) {
                                                        DocumentReference currentUserDocumentReference = db.collection("Users").document(uid);
                                                        currentUserDocumentReference.get()
                                                                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                                                    @Override
                                                                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                                                                        if (documentSnapshot.exists()) {

                                                                            int noOfFriends = Integer.parseInt(documentSnapshot.get("Number of friends").toString());
                                                                            currentUserDocumentReference.update("Number of friends", noOfFriends + 1);
                                                                            int noOfRequests = Integer.parseInt(documentSnapshot.get("Number of requests").toString());
                                                                            currentUserDocumentReference.update("Number of requests", noOfRequests - 1);

                                                                            CollectionReference requestListReference = db.collection("Users").document(uid).collection("Request List");
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
//            return new workOrderVH(v);
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
