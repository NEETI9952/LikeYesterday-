package com.example.likeyesterday.MyPlaces;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.likeyesterday.FirestoreRecyclerModelClass;
import com.example.likeyesterday.R;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.Query;
import java.util.ArrayList;
import static com.example.likeyesterday.ProfileFragment.currentUserDocumentReference;


public class MyPlacesListFragment extends Fragment {

    public static String PlaceName;
    public static GeoPoint geopoint;

    static ArrayList<String> places = new ArrayList<String>();
    static ArrayList<LatLng> locations = new ArrayList<LatLng>();

    private RecyclerView recyclerView;
    private MyPacesFirestoreAdapter placesFirestoreAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        ViewGroup root=(ViewGroup) inflater.inflate(R.layout.fragment_my_places_list, container, false);
        recyclerView=root.findViewById(R.id.placesList);

        setRecyclerView();
        return root;

    }

    private void setRecyclerView() {

        Query query=currentUserDocumentReference.collection("My Places");

        FirestoreRecyclerOptions<FirestoreRecyclerModelClass> options=new FirestoreRecyclerOptions.Builder<FirestoreRecyclerModelClass>().setQuery(query,FirestoreRecyclerModelClass.class).build();
        placesFirestoreAdapter=new MyPacesFirestoreAdapter(getContext(),options);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(placesFirestoreAdapter);
    }

    @Override
    public void onStart() {
        super.onStart();
        placesFirestoreAdapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
       placesFirestoreAdapter.stopListening();
    }
}