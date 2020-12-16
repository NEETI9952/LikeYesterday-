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

public class FriendsListFragment extends Fragment {

    public static final int PERMISSIONS_REQUEST_READ_CONTACTS = 1;
    FloatingActionButton add;

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
        ViewGroup root=(ViewGroup) inflater.inflate(R.layout.fragment_friends_list, container, false);

        mAuth = FirebaseAuth.getInstance();
        uid = mAuth.getCurrentUser().getUid();

        recyclerView=root.findViewById(R.id.recyclerRequests);

        add=(FloatingActionButton) root.findViewById(R.id.addFriendButton);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requestContactPermission();

            }
        });

        setRecyclerView();
        return  root;
    }

    private void setRecyclerView() {

        Query query=userColRef.document(uid).collection("FriendsList").orderBy("FullName", Query.Direction.ASCENDING);

        Log.i("Friend List",uid);

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

    public void requestContactPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(getContext(), android.Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), android.Manifest.permission.READ_CONTACTS)) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    builder.setTitle("Read Contacts permission");
                    builder.setPositiveButton(android.R.string.ok, null);
                    builder.setMessage("Please enable access to contacts.");
                    builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
                        @TargetApi(Build.VERSION_CODES.M)
                        @Override
                        public void onDismiss(DialogInterface dialog) {
                            requestPermissions(new String[]{android.Manifest.permission.READ_CONTACTS}, PERMISSIONS_REQUEST_READ_CONTACTS);
                        }
                    });
                    builder.show();
                } else {
                    ActivityCompat.requestPermissions(getActivity(), new String[]{android.Manifest.permission.READ_CONTACTS}, PERMISSIONS_REQUEST_READ_CONTACTS);
                }
            } else {
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_container, new AddFriendsFragment()).addToBackStack(null);
                transaction.commit();
            }
        } else {
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_container, new AddFriendsFragment()).addToBackStack(null);
            transaction.commit();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,@NonNull int[] grantResults) {
        switch (requestCode) {
            case PERMISSIONS_REQUEST_READ_CONTACTS: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    FragmentTransaction transaction = getFragmentManager().beginTransaction();
                    transaction.replace(R.id.fragment_container,new AddFriendsFragment()).addToBackStack(null);
                    transaction.commit();
                } else {
                    Toast.makeText(getContext(), "You have disabled a contacts permission", Toast.LENGTH_LONG).show();
                }
                return;
            }
        }
    }


}