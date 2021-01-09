package com.example.likeyesterday.MyFriends;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.MimeTypeMap;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
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
import com.google.android.material.chip.Chip;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

import static android.app.Activity.RESULT_OK;
import static com.example.likeyesterday.HomeScreenActivity2.CAMERA_PERM_CODE;
import static com.example.likeyesterday.HomeScreenActivity2.CAMERA_REQUEST_CODE;
import static com.example.likeyesterday.HomeScreenActivity2.GALLERY_REQUEST_CODE;
import static com.example.likeyesterday.MyFriends.FriendsListFragment.friendUid;
import static com.example.likeyesterday.ProfileFragment.currentUserDocumentReference;
import static com.example.likeyesterday.ProfileFragment.db;
import static com.example.likeyesterday.ProfileFragment.uid;

public class FriendPlacesMapFragment extends Fragment {

    public static StorageReference storageReference;
    private GoogleMap mMap;
    LocationManager locationManager;
    LocationListener locationListener;
    BitmapDescriptor customMarker;
    String finalAddress;
    Map<String,Object> myPlace;
    ImageView imageView;
    String currentPhotoPath;
    ProgressBar p1;
    private String imageUrl;

    CollectionReference friendPlacesCollection =currentUserDocumentReference.collection("FriendsList").document(friendUid).collection("Our Places");

    public void goToMap(Location location, String title,String snippet){
        if(location!=null){
            mMap.clear();
            addOldPlaces();
            LatLng userLocation= new LatLng(location.getLatitude(),location.getLongitude());
//            BitmapDescriptor customMarker = BitmapDescriptorFactory.fromResource(R.drawable.mapspin);
//            customMarker = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW);
            try{
                mMap.addMarker(new MarkerOptions().position(userLocation).title(title).icon(customMarker).snippet(snippet));
                mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLocation,15));
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }
//    public void addPhoto(){
//        Intent intent=new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//        startActivityForResult(intent,1);
//        Log.i("Image","add photo");
//    }


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

//            if (ContextCompat.checkSelfPermission(Objects.requireNonNull(getContext()), Manifest.permission.READ_EXTERNAL_STORAGE)==PackageManager.PERMISSION_GRANTED){
//                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                    addPhoto();
//                } else {
//                    Toast.makeText(getContext(), "You have disabled a required permission", Toast.LENGTH_LONG).show();
//                }
        }
    }


    private OnMapReadyCallback callback = new OnMapReadyCallback() {

        @Override
        public void onMapReady(GoogleMap googleMap) {
            mMap = googleMap;

            mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
                @Override
                public void onMapLongClick(LatLng latLng) {
                    Geocoder geocoder= new Geocoder(getContext(), Locale.getDefault());
                    String address ="";

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

                                        friendPlacesCollection
                                                .whereEqualTo("geopoint", geoPointMarker)
                                                .get()
                                                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                                    @Override
                                                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                                                        if (queryDocumentSnapshots.size() == 0) {
                                                            Toast.makeText(getContext(), "Place doesn't exist", Toast.LENGTH_SHORT).show();
                                                            Log.d("friend", "Place doesn't exist");
                                                            return;
                                                        }

                                                        for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                                                            String ourPlaceDocID = documentSnapshot.getId();
                                                            showAlertDialogForIncrease(geoPointMarker,ourPlaceDocID);
                                                            return;
                                                        }
                                                    }
                                                }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Toast.makeText(getContext(), "Something went wrong \n Please try again", Toast.LENGTH_SHORT).show();
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

            addOldPlaces();

//            friendPlacesCollection.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
//                @Override
//                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
//
//                    for(QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots){
//                        FirestoreRecyclerModelClass map= documentSnapshot.toObject(FirestoreRecyclerModelClass.class);
//                        String PlaceName=map.getPlaceName();
//                        GeoPoint geopoint=map.getgeopoint();
//                        String times=Integer.toString(map.getNoOfTimes());
//
////                        Location placeLocation= new Location(LocationManager.GPS_PROVIDER);
////                        placeLocation.setLatitude(geopoint.getLatitude());
////                        placeLocation.setLongitude(geopoint.getLongitude());
//                        LatLng placeLocation=new LatLng(geopoint.getLatitude(),geopoint.getLongitude());
//
//                        customMarker = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW);
//                        mMap.addMarker(new MarkerOptions().position(placeLocation).title(PlaceName).icon(customMarker).snippet(times));
////                        goToMap(placeLocation,PlaceName,"");
//                    }
//                }
//            }).addOnFailureListener(new OnFailureListener() {
//                @Override
//                public void onFailure(@NonNull Exception e) {
//                    Toast.makeText(getContext(),"Something went wrong "+ e.toString(),Toast.LENGTH_SHORT).show();
//
//                }
//            });
        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_friend_places_map, container, false);


    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SupportMapFragment mapFragment =
                (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.FriendPlacesmap);
        try{
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
        }}catch (Exception e){}
    }

//    -----------------------------
    public void addOldPlaces(){
        friendPlacesCollection.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                for(QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots){
                    FirestoreRecyclerModelClass map= documentSnapshot.toObject(FirestoreRecyclerModelClass.class);
                    String PlaceName=map.getPlaceName();
                    GeoPoint geopoint=map.getgeopoint();
                    String times=Integer.toString(map.getNoOfTimes());

//                        Location placeLocation= new Location(LocationManager.GPS_PROVIDER);
//                        placeLocation.setLatitude(geopoint.getLatitude());
//                        placeLocation.setLongitude(geopoint.getLongitude());
                    LatLng placeLocation=new LatLng(geopoint.getLatitude(),geopoint.getLongitude());

                    customMarker = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW);
                    mMap.addMarker(new MarkerOptions().position(placeLocation).title(PlaceName).icon(customMarker).snippet(times));
//                        goToMap(placeLocation,PlaceName,"");
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getContext(),"Something went wrong "+ e.toString(),Toast.LENGTH_SHORT).show();

            }
        });
    }
//        -----------------------------

    private void showAlertDialogForPoint(final LatLng point) {
        // inflate message_item.xml view
        View  messageView = LayoutInflater.from(getContext()).inflate(R.layout.menu_friend_add_place, null);
        // Create alert dialog builder
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getContext());
        // set message_item.xml to AlertDialog builder
        alertDialogBuilder.setView(messageView);

        // Create alert dialog
        final AlertDialog alertDialog = alertDialogBuilder.create();

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
                        Drawable drawable=imageView.getDrawable();
                        if (drawable != getResources().getDrawable(R.drawable.ic_baseline_add_24)) {

                            myPlace = new HashMap<>();
                            myPlace.put("PlaceName",placeNameEditText);
                            GeoPoint placeGeopoint = new GeoPoint(point.latitude, point.longitude);
                            myPlace.put("geopoint",placeGeopoint);
                            myPlace.put("NoOfTimes",1);

                            friendPlacesCollection.document().set(myPlace).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {

                                    friendPlacesCollection
                                            .whereEqualTo("geopoint",placeGeopoint)
                                            .get()
                                            .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                                @Override
                                                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                                    for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {

                                                        String myPlaceDocID = documentSnapshot.getId();

                                                        HashMap<String,Object> ourPlaceInfo = new HashMap<>();
                                                        ourPlaceInfo.put("description",snippet);
                                                        ourPlaceInfo.put("Date",date);
                                                        ourPlaceInfo.put("Time",time);
                                                        ourPlaceInfo.put("Image",imageUrl);
                                                        ourPlaceInfo.put("sender",uid);

                                                        friendPlacesCollection.document(myPlaceDocID).collection("PlaceInfo").document().set(ourPlaceInfo).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                            @Override
                                                            public void onSuccess(Void aVoid) {

                                                                DocumentReference friendDocRef=db.collection("Users").document(friendUid);
                                                                friendDocRef.collection("FriendsList").document(uid).collection("Our Places").document().set(myPlace).addOnSuccessListener(new OnSuccessListener<Void>() {

                                                                    @Override
                                                                    public void onSuccess(Void aVoid) {

                                                                        friendDocRef.collection("FriendsList").document(uid).collection("Our Places")
                                                                                .whereEqualTo("geopoint", placeGeopoint)
                                                                                .get()
                                                                                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                                                                    @Override
                                                                                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                                                                                        String ourPlaceDocIDFriend="";
                                                                                        for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                                                                                            ourPlaceDocIDFriend = documentSnapshot.getId();
                                                                                        }
                                                                                        String finalOurPlaceDocIDFriend = ourPlaceDocIDFriend;

                                                                                        friendDocRef.collection("FriendsList").document(uid).collection("Our Places").document(finalOurPlaceDocIDFriend).collection("PlaceInfo").document().set(ourPlaceInfo).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                                            @Override
                                                                                            public void onSuccess(Void aVoid) {
                                                                                                imageUrl="";
                                                                                                Toast.makeText(getContext(),"Location Added",Toast.LENGTH_SHORT).show();
                                                                                                customMarker = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ROSE);
                                                                                                mMap.addMarker(new MarkerOptions().position(point).title(placeNameEditText).icon(customMarker).snippet("1"));
                                                                                            }
                                                                                        }).addOnFailureListener(new OnFailureListener() {
                                                                                            @Override
                                                                                            public void onFailure(@NonNull Exception e) {
                                                                                                Toast.makeText(getContext(), "Something went wrong \n Please try again", Toast.LENGTH_SHORT).show();
                                                                                            }
                                                                                        });
                                                                                    }
                                                                                }).addOnFailureListener(new OnFailureListener() {
                                                                            @Override
                                                                            public void onFailure(@NonNull Exception e) {
                                                                                Toast.makeText(getContext(), "Something went wrong \n Please try again", Toast.LENGTH_SHORT).show();
                                                                            }
                                                                        });
                                                                    }
                                                                }).addOnFailureListener(new OnFailureListener() {
                                                                    @Override
                                                                    public void onFailure(@NonNull Exception e) {
                                                                        Toast.makeText(getContext(), "Something went wrong \n Please try again", Toast.LENGTH_SHORT).show();
                                                                    }
                                                                });
//                                                                friendDocRef.collection("FriendsList").document(uid).collection("Our Places")
//                                                                        .whereEqualTo("geopoint", placeGeopoint)
//                                                                        .get()
//                                                                        .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
//                                                                            @Override
//                                                                            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
//                                                                                if (queryDocumentSnapshots.size() == 0) {
//
//                                                                                }
//
////                                                                                String ourPlaceDocIDFriendDoc="";
////                                                                                for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
////                                                                                    ourPlaceDocIDFriendDoc = documentSnapshot.getId();
////                                                                                }
////
////                                                                                String finalOurPlaceDocIDFriendDoc = ourPlaceDocIDFriendDoc;
////                                                                                friendDocRef.collection("FriendsList").document(uid).collection("Our Places").document(finalOurPlaceDocIDFriendDoc).get()
////                                                                                        .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
////                                                                                            @Override
////                                                                                            public void onSuccess(DocumentSnapshot documentSnapshot) {
////                                                                                                if (documentSnapshot.exists()) {
////                                                                                                    FirestoreRecyclerModelClass mapMarker = documentSnapshot.toObject(FirestoreRecyclerModelClass.class);
////                                                                                                    int times = mapMarker.getNoOfTimes() + 1;
////                                                                                                    friendDocRef.collection("FriendsList").document(uid).collection("Our Places").document(finalOurPlaceDocIDFriendDoc).update("NoOfTimes", times);
////                                                                                                }
////                                                                                            }
////                                                                                        });
////
////                                                                                friendDocRef.collection("FriendsList").document(uid).collection("Our Places").document(finalOurPlaceDocIDFriendDoc).collection("PlaceInfo").document().set(ourPlaceInfo).addOnSuccessListener(new OnSuccessListener<Void>() {
////                                                                                    @Override
////                                                                                    public void onSuccess(Void aVoid) {
////                                                                                        Toast.makeText(getContext(),"Location Added",Toast.LENGTH_SHORT).show();
////                                                                                        customMarker = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ROSE);
////                                                                                        mMap.addMarker(new MarkerOptions().position(point).title(placeNameEditText).icon(customMarker).snippet("1"));
////                                                                                    }
////                                                                                }).addOnFailureListener(new OnFailureListener() {
////                                                                                    @Override
////                                                                                    public void onFailure(@NonNull Exception e) {
////                                                                                        Toast.makeText(getContext(), "Something went wrong \n Please try again", Toast.LENGTH_SHORT).show();
////                                                                                    }
////                                                                                });
//
//                                                                            }
//                                                                        }).addOnFailureListener(new OnFailureListener() {
//                                                                    @Override
//                                                                    public void onFailure(@NonNull Exception e) {
//                                                                        Toast.makeText(getContext(), "Something went wrong \n Please try again", Toast.LENGTH_SHORT).show();
//                                                                    }
//                                                                });
                                                            }
                                                        });
                                                    }
                                                }
                                            });
                                }
                            });
                        } else {
                            Toast.makeText(getContext(),"Please select a picture",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
        // Configure dialog button (Cancel)
        alertDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) { dialog.cancel(); }
        });

        // Display the dialog
        alertDialog.show();
        EditText editText=alertDialog.findViewById(R.id.etTitle);
        editText.setText(finalAddress);

        TextView textViewDate=alertDialog.findViewById(R.id.etDate);
        TextView textViewTime=alertDialog.findViewById(R.id.etTime);
        SimpleDateFormat sdf=new SimpleDateFormat("dd-MM-yyyy",Locale.getDefault());
        SimpleDateFormat stf=new SimpleDateFormat("HH:mm:ss",Locale.getDefault());
        textViewDate.setText(sdf.format(new Date()));
        textViewTime.setText(stf.format(new Date()));

        FloatingActionButton addPhoto = alertDialog.findViewById(R.id.floatingActionButton);
        p1=alertDialog.findViewById(R.id.progressBar2);
        p1.setVisibility(View.INVISIBLE);
        imageView=alertDialog.findViewById(R.id.etImageView);

        addPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //add picture
//                if(ContextCompat.checkSelfPermission(getContext(),Manifest.permission.READ_EXTERNAL_STORAGE)!=PackageManager.PERMISSION_GRANTED){
//                    requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},1);
//                    Log.i("Image","request permission on click");
//                }else {
//                    addPhoto();
//                }
               galleryCameraDialogBox();
            }
        });
    }

    private void galleryCameraDialogBox(){
        MaterialAlertDialogBuilder builder=new MaterialAlertDialogBuilder(getContext());
        builder.setIcon(R.drawable.addimage);
//                builder.setBackground(getResources().getDrawable(R.drawable.alertdiabogbg,null));
        builder.setMessage("Choose image source...").setPositiveButton("Camera", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                cameraPermission();
            }
        }).setNegativeButton("Gallery", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(gallery, GALLERY_REQUEST_CODE);
            }
        });
        builder.show();
    }



    private void showAlertDialogForIncrease(GeoPoint gp,String ourPlaceDocID) {
        // inflate message_item.xml view
        View msgView = LayoutInflater.from(getContext()).
                inflate(R.layout.menu_increase_friend_place, null);
        // Create alert dialog builder
        AlertDialog.Builder alertDialogBuilderr = new AlertDialog.Builder(getContext());
        // set message_item.xml to AlertDialog builder
        alertDialogBuilderr.setView(msgView);

        // Create alert dialog
        final AlertDialog alertDialogg = alertDialogBuilderr.create();

        // Configure dialog button (OK)
        alertDialogg.setButton(DialogInterface.BUTTON_POSITIVE, "Save",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Define color of marker icon

                        // Extract content from alert dialog
                        String description = ((EditText) alertDialogg.findViewById(R.id.etDescriptionAdd)).getText().toString();
                        String date = ((TextView) alertDialogg.findViewById(R.id.etDateAdd)).getText().toString();
                        String time = ((TextView) alertDialogg.findViewById(R.id.etTimeAdd)).getText().toString();
                        Drawable drawable = imageView.getDrawable();

                        if (drawable != getResources().getDrawable(R.drawable.ic_baseline_add_24)) {
                            friendPlacesCollection.document(ourPlaceDocID).get()
                                    .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                        @Override
                                        public void onSuccess(DocumentSnapshot documentSnapshot) {

                                            if (documentSnapshot.exists()) {
                                                FirestoreRecyclerModelClass mapMarker = documentSnapshot.toObject(FirestoreRecyclerModelClass.class);
                                                int times = mapMarker.getNoOfTimes() + 1;
                                                friendPlacesCollection.document(ourPlaceDocID).update("NoOfTimes", times);

                                                HashMap<String, Object> ourPlaceInfo = new HashMap<>();
                                                ourPlaceInfo.put("description", description);
                                                ourPlaceInfo.put("Date", date);
                                                ourPlaceInfo.put("Time", time);
                                                ourPlaceInfo.put("Image",imageUrl);
                                                ourPlaceInfo.put("sender",uid);

                                                friendPlacesCollection.document(ourPlaceDocID).collection("PlaceInfo").document().set(ourPlaceInfo).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void aVoid) {

                                                        DocumentReference friendDocRef=db.collection("Users").document(friendUid);
                                                        friendDocRef.collection("FriendsList").document(uid).collection("Our Places")
                                                                .whereEqualTo("geopoint", gp)
                                                                .get()
                                                                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                                                    @Override
                                                                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                                                                        String ourPlaceInfoDocIDFriend = "";
                                                                        for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                                                                            ourPlaceInfoDocIDFriend = documentSnapshot.getId();
                                                                        }
                                                                        String finalOurPlaceInfoDocIDFriend = ourPlaceInfoDocIDFriend;
                                                                        friendDocRef.collection("FriendsList").document(uid).collection("Our Places").document(finalOurPlaceInfoDocIDFriend).get()
                                                                                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                                                                    @Override
                                                                                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                                                                                        if (documentSnapshot.exists()) {
                                                                                            FirestoreRecyclerModelClass mapMarker = documentSnapshot.toObject(FirestoreRecyclerModelClass.class);
                                                                                            int times = mapMarker.getNoOfTimes() + 1;
                                                                                            friendDocRef.collection("FriendsList").document(uid).collection("Our Places").document(finalOurPlaceInfoDocIDFriend).update("NoOfTimes", times);
                                                                                        }
                                                                                    }
                                                                                });
                                                                        friendDocRef.collection("FriendsList").document(uid).collection("Our Places").document(finalOurPlaceInfoDocIDFriend).collection("PlaceInfo").document().set(ourPlaceInfo).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                            @Override
                                                                            public void onSuccess(Void aVoid) {
                                                                                imageUrl="";
                                                                                Toast.makeText(getContext(), "Action Successful", Toast.LENGTH_SHORT).show();
                                                                            }
                                                                        }).addOnFailureListener(new OnFailureListener() {
                                                                            @Override
                                                                            public void onFailure(@NonNull Exception e) {
                                                                                Toast.makeText(getContext(), "Something went wrong \n Please try again", Toast.LENGTH_SHORT).show();
                                                                            }
                                                                        });

                                                                    }
                                                                });
                                                    }
                                                });
                                            }
                                        }
                                    });
                        } else{
                        Toast.makeText(getContext(), "Please select a picture", Toast.LENGTH_SHORT).show();
                        }
                }
        });
        // Configure dialog button (Cancel)
        alertDialogg.setButton(DialogInterface.BUTTON_NEGATIVE,"Cancel", new DialogInterface.OnClickListener() {
            public void onClick (DialogInterface dialog,int id){
                dialog.cancel();
            }
        });

        // Display the dialog
        alertDialogg.show();

        friendPlacesCollection.document(ourPlaceDocID).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {

                        if (documentSnapshot.exists()) {
                            FirestoreRecyclerModelClass classPlace = documentSnapshot.toObject(FirestoreRecyclerModelClass.class);
                            String NoOfTimes = String.valueOf(classPlace.getNoOfTimes());
                            Chip chip=alertDialogg.findViewById(R.id.chip4Add);
                            chip.setText("No. Of Times:"+NoOfTimes);

                            TextView textViewPlace=alertDialogg.findViewById(R.id.etTitleAdd);
                            String title=classPlace.getPlaceName();
                            textViewPlace.setText(title);
                        }
                    }
                });
        TextView textViewDateAdd = alertDialogg.findViewById(R.id.etDateAdd);
        TextView textViewTimeAdd = alertDialogg.findViewById(R.id.etTimeAdd);
        SimpleDateFormat sdfAdd = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
        SimpleDateFormat stfAdd = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
        textViewDateAdd.setText(sdfAdd.format(new Date()));
        textViewTimeAdd.setText(stfAdd.format(new Date()));

        FloatingActionButton addPhoto = alertDialogg.findViewById(R.id.floatingActionButtonAdd);
        p1=alertDialogg.findViewById(R.id.progressBar3);
        p1.setVisibility(View.INVISIBLE);
        imageView=alertDialogg.findViewById(R.id.etImageView);

        FloatingActionButton addPictureButton =alertDialogg.findViewById(R.id.floatingActionButtonAdd);
        imageView=alertDialogg.findViewById(R.id.etImageView);

        addPictureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //add picture
//                if(ContextCompat.checkSelfPermission(getContext(),Manifest.permission.READ_EXTERNAL_STORAGE)!=PackageManager.PERMISSION_GRANTED){
//                    requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},1);
//                    Log.i("Image","request permission on click");
//                }else {
//                    addPhoto();
//                }
                galleryCameraDialogBox();
            }
        });
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CAMERA_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {

                File f = new File(currentPhotoPath);
//                Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                Uri contentUri = Uri.fromFile(f);
//                mediaScanIntent.setData(contentUri);
//                this.sendBroadcast(mediaScanIntent);
//                selectedImageFront.setImageURI(Uri.fromFile(f));

                Log.i("testimageupload", Uri.fromFile(f).toString());
//                issueImage.setImageURI(Uri.fromFile(f));
//                issueImage.setTag("uploaded");
                uploadImageToFirebase(f.getName(),contentUri);
            }}


        /////////////Gallery

        if (requestCode == GALLERY_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {

                Uri contentUri = data.getData();
                String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
                String imageFileName = "JPEG_" + timeStamp + "." + getFileExt(contentUri);
                Log.d("testimage", "onActivityResult: Gallery Image Uri:  " + imageFileName);
//                selectedImageFront.setImageURI(Uri.fromFile(f));
//                        Log.i("testimageupload", Uri.fromFile(f).toString());
//                issueImage.setImageURI(contentUri);
//                issueImage.setTag("uploaded");
                uploadImageToFirebase(imageFileName,contentUri);
            }}
    }

    public void cameraPermission() {
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CAMERA}, CAMERA_PERM_CODE);
            if(ContextCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED){
                dispatchTakePictureIntent();
            }
        } else {
            Log.i("testimageupload", "2");
            dispatchTakePictureIntent();
        }
    }


    private void dispatchTakePictureIntent () {
        Log.i("testimageupload", "3");
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        Log.i("testimageupload", "4");
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            Log.i("testimageupload", "5");
            try {
                photoFile = createImageFile();
                Log.i("testimageupload", "6");
            } catch (IOException ex) {
                // Error occurred while creating the File
                Log.i("testimageupload", "7");

            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Log.i("testimageupload", "8");
                Uri photoURI = FileProvider.getUriForFile(getContext(),
                        "com.example.android.fileprovider",
                        photoFile);
                Log.i("testimageupload", "9");
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, CAMERA_REQUEST_CODE);
            }
        }
    }
    private String getFileExt(Uri contentUri) {
//        private String getFileExt(Uri contentUri) {
        ContentResolver c = getActivity().getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(c.getType(contentUri));
//        }
    }
    private File createImageFile () throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_"+uid;
//        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File storageDir = getContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );
        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }

    private void uploadImageToFirebase(String name, Uri contentUri) {
        p1.setVisibility(View.VISIBLE);
        getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        Log.i("TestingImageUpload",name.toString());
        storageReference = FirebaseStorage.getInstance().getReference();
        final StorageReference imageStorageReference = storageReference.child("PlaceImages/"+ name);
        imageStorageReference.putFile(contentUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                Log.i("testimageupload", "image uploaded to firebase storage");
                imageStorageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Log.i("testimageupload", "DownloadedUri: " + uri);

                        imageUrl=uri.toString();

                        LoadImage();
                        getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.i("testimageupload", "Downloading Uri failed");
                        Log.i("testimageupload", "Dow: "+ e);
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.i("testimageupload", "image not uploaded to firebase storage");
                Log.i("testimageupload", "Dow: "+ e);
            }
        });
    }


    public void LoadImage(){
        try {
            Picasso.get().load(imageUrl).into(imageView, new Callback() {
                @Override
                public void onSuccess() {
                    p1.setVisibility(View.GONE);
                }

                @Override
                public void onError(Exception e) {
                }
            });
        }catch(Exception e){

        }
    }
}