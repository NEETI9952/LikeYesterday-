package com.example.likeyesterday.MyPlaces;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.likeyesterday.FirestoreRecyclerModelClass;
import com.example.likeyesterday.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.Source;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

import static com.example.likeyesterday.ProfileFragment.currentUserDocumentReference;

public class MyPlacesMapsFragment extends Fragment{

    private GoogleMap mMap;
    LocationManager locationManager;
    LocationListener locationListener;
    BitmapDescriptor customMarker;
    String address, finalAddress;
    Location placeLocation;
    Map<String,Object> myPlace;


    CollectionReference myPlacesCollection =currentUserDocumentReference.collection("My Places");

    public void goToMap(Location location,String title,String snippet){
        if(location!=null){
            mMap.clear();
            LatLng userLocation= new LatLng(location.getLatitude(),location.getLongitude());
//            BitmapDescriptor customMarker = BitmapDescriptorFactory.fromResource(R.drawable.mapspin);
//            customMarker = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW);
            mMap.addMarker(new MarkerOptions().position(userLocation).title(title).icon(customMarker).snippet(snippet));
//            mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLocation,10));
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(grantResults.length>0 && grantResults[0]== PackageManager.PERMISSION_GRANTED){
            if(ContextCompat.checkSelfPermission(Objects.requireNonNull(getContext()), Manifest.permission.ACCESS_FINE_LOCATION)==PackageManager.PERMISSION_GRANTED){
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0,locationListener);
                Location lastKnownLocation= locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                customMarker = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE);
                goToMap(lastKnownLocation,"Your Location","");
            }
        }
    }

    private final OnMapReadyCallback callback = new OnMapReadyCallback() {

        @Override
        public void onMapReady(GoogleMap googleMap) {
            mMap = googleMap;
            mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
                @Override
                public void onMapLongClick(LatLng latLng) {
                    Geocoder geocoder= new Geocoder(getContext(), Locale.getDefault());
                    address ="";

                    try {
                        List<Address> addressList = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);
                        if(addressList!=null &&addressList.size()>0) {
                            Log.i("Address", addressList.get(0).getAddressLine(0));
//                            Toast.makeText(getContext(), addressList.get(0).getAddressLine(0), Toast.LENGTH_SHORT).show();
                            if (addressList.get(0).getAddressLine(0) != null) {
//                                address += addressList.get(0).getAddressLine(0);
                                if (addressList.get(0).getSubLocality() != null) {
                                    address += addressList.get(0).getSubLocality()+"," ;
                                }else {
                                        address += addressList.get(0).getAddressLine(0)+"," ;
                                }
                                if (addressList.get(0).getLocality() != null) {
                                    address += addressList.get(0).getLocality()+ ",";
                                }
                                if (addressList.get(0).getAdminArea() != null) {
                                    address += addressList.get(0).getAdminArea() + ",";
                                }
                                if (addressList.get(0).getCountryName() != null) {
                                    address += addressList.get(0).getCountryName()+"" ;
                                }
                                Log.i("Address", address);
                            }

                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    finalAddress = address;
                    showAlertDialogForPoint(latLng);
                }
            });

            mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {

                @Override
                public void onInfoWindowClick(Marker marker) {
                    final Marker selectedMarker=marker;

//                    mMap.setOnMapClickListener(null);
//                    mMap.setOnMarkerClickListener(null);
                    try {
                        new AlertDialog.Builder(getContext())
                                .setIcon(android.R.drawable.ic_dialog_map)
                                .setTitle("Increase no.of times visited")
                                .setMessage("You visited this place again" )
                                .setCancelable(false)
                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        double latitudeMarker = selectedMarker.getPosition().latitude;
                                        double longitudeMarker = selectedMarker.getPosition().longitude;
                                        GeoPoint geoPointMarker=new GeoPoint(latitudeMarker,longitudeMarker);

                                        myPlacesCollection
                                                .whereEqualTo("geopoint",geoPointMarker)
                                                .get()
                                                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                                    @Override
                                                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                                                        if(queryDocumentSnapshots.size()==0){
                                                            Toast.makeText(getContext(),"Place doesn't exist",Toast.LENGTH_SHORT).show();
                                                            Log.d("friend","Place doesn't exist");
                                                            return;
                                                        }

                                                        for(QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {

                                                            String myPlaceDocID = documentSnapshot.getId();

                                                            myPlacesCollection.document(myPlaceDocID).get()
                                                                    .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                                                        @Override
                                                                        public void onSuccess(DocumentSnapshot documentSnapshot) {

                                                                            if(documentSnapshot.exists()){
//                                                                                int times = (int) documentSnapshot.get("No.of times");
                                                                                FirestoreRecyclerModelClass mapMarker= documentSnapshot.toObject(FirestoreRecyclerModelClass.class);
                                                                                int times =mapMarker.getNoOfTimes()+1;
                                                                                myPlacesCollection.document(myPlaceDocID).update("NoOfTimes", times);
                                                                                marker.setSnippet(Integer.toString(times));
                                                                                Toast.makeText(getContext(),"Action Successful",Toast.LENGTH_SHORT).show();
                                                                            }

                                                                        }
                                                                    });
                                                        }
                                                    }
                                                }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Toast.makeText(getContext(),"Something went wrong \n Please try again",Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                    }
                                }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        }).show();

                    }
                    catch (Exception e){
                        e.printStackTrace();
                    }

                }
            });

//            mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
//                public boolean onMarkerClick(Marker marker) {
//                    return false;
//                }
//            });

            locationManager=(LocationManager) getContext().getSystemService(Context.LOCATION_SERVICE);
            locationListener = new LocationListener() {
                @Override
                public void onLocationChanged(@NonNull Location location) {
                    customMarker = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE);
                    goToMap(location,"your location","");
                }

                @Override
                public void onStatusChanged(String provider, int status, Bundle extras) {

                }
            };

            if(ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION)==PackageManager.PERMISSION_GRANTED){
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0,locationListener);

                Location lastKnownLocation= locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

                customMarker = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE);
                goToMap(lastKnownLocation,"Your Location","");
            }else {
                ActivityCompat.requestPermissions(Objects.requireNonNull(getActivity()),new String[]{Manifest.permission.ACCESS_FINE_LOCATION},1);
            }

            myPlacesCollection.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                @Override
                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                    for(QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots){
                        FirestoreRecyclerModelClass map= documentSnapshot.toObject(FirestoreRecyclerModelClass.class);
                        String PlaceName=map.getPlaceName();
                        GeoPoint geopointPlace=map.getgeopoint();
                        String timesPlace=Integer.toString(map.getNoOfTimes());

                        LatLng placeLocation=new LatLng(geopointPlace.getLatitude(),geopointPlace.getLongitude());

                        customMarker = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW);
                        mMap.addMarker(new MarkerOptions().position(placeLocation).title(PlaceName).icon(customMarker).snippet(timesPlace));

                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getContext(),"Something went wrong "+ e.toString(),Toast.LENGTH_SHORT).show();

                }
            });
        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
//        ViewGroup root=(ViewGroup)inflater.inflate(R.layout.fragment_my_places_maps, container, false);
//        return root;
        return inflater.inflate(R.layout.fragment_my_places_maps, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.FriendPlacesmap);
        if (mapFragment != null) {
            mapFragment.getMapAsync(callback);
        }

        if(mMap!=null){
            mMap.clear();
            mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
            mMap.setOnMapLongClickListener((GoogleMap.OnMapLongClickListener) this);
            mMap.getUiSettings().setRotateGesturesEnabled(true);
            mMap.getUiSettings().setZoomGesturesEnabled(true);
            mMap.getUiSettings().setZoomControlsEnabled(true);
        }
    }

    private void showAlertDialogForPoint(final LatLng point) {
        // inflate message_item.xml view
        View  messageView = LayoutInflater.from(getContext()).
                inflate(R.layout.menu_add_place, null);
        // Create alert dialog builder
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getContext());
        // set message_item.xml to AlertDialog builder
        alertDialogBuilder.setView(messageView);

        // Create alert dialog
        final AlertDialog alertDialog = alertDialogBuilder.create();

//        ((EditText) alertDialog.findViewById(R.id.etTitle)).setText(finalAddress);

        // Configure dialog button (OK)
        alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "Add",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Define color of marker icon

                        // Extract content from alert dialog
                        String placeNameEditText = ((EditText) alertDialog.findViewById(R.id.etTitle)).getText().toString();
                        String snippet = ((EditText) alertDialog.findViewById(R.id.etSnippet)).getText().toString();
                        String date=((TextView) alertDialog.findViewById(R.id.etDate)).getText().toString();
                        String time=((TextView) alertDialog.findViewById(R.id.etTime)).getText().toString();

                        GeoPoint placeGeopoint = new GeoPoint(point.latitude, point.longitude);

                        myPlace = new HashMap<>();
                        myPlace.put("PlaceName",placeNameEditText);
                        myPlace.put("geopoint",placeGeopoint);
                        myPlace.put("NoOfTimes",1);

                        myPlacesCollection.document().set(myPlace).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                myPlacesCollection
                                        .whereEqualTo("geopoint",placeGeopoint)
                                        .get()
                                        .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                            @Override
                                            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                                for(QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {

                                                    String myPlaceDocID = documentSnapshot.getId();

                                                    HashMap<String,Object> myPlaceInfo = new HashMap<>();
                                                    myPlaceInfo.put("description",snippet);
                                                    myPlaceInfo.put("Date",date);
                                                    myPlaceInfo.put("Time",time);

                                                    myPlacesCollection.document(myPlaceDocID).collection("PlaceInfo").document().set(myPlaceInfo).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                        @Override
                                                        public void onSuccess(Void aVoid) {
                                                            customMarker = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ROSE);
                                                            mMap.addMarker(new MarkerOptions().position(point).title(placeNameEditText).icon(customMarker).snippet("1"));
                                                            Toast.makeText(getContext(),"Location Added",Toast.LENGTH_SHORT).show();

                                                        }
                                                    });
                                                }
                                            }
                                        });
                            }
                        });
                    }
                });

        // Configure dialog button (Cancel)
        alertDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) { dialog.cancel(); }
                });

        // Display the dialog
        alertDialog.show();
        EditText editTextTitle=alertDialog.findViewById(R.id.etTitle);
        editTextTitle.setText(finalAddress);

        TextView textViewDate=alertDialog.findViewById(R.id.etDate);
        TextView textViewTime=alertDialog.findViewById(R.id.etTime);
        SimpleDateFormat sdf=new SimpleDateFormat("dd-MM-yyyy",Locale.getDefault());
        SimpleDateFormat stf=new SimpleDateFormat("HH:mm:ss",Locale.getDefault());
        textViewDate.setText(sdf.format(new Date()));
        textViewTime.setText(stf.format(new Date()));
    }

}