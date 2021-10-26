package com.comp90018.JoinMe;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.IntentSender;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import object.Activity;

public class MarkOnMap extends AppCompatActivity implements OnMapReadyCallback {

    private DatabaseReference databaseReference;

    ArrayList<String> activityName = new ArrayList<>();
    ArrayList<String> owner = new ArrayList<>();
    ArrayList<String> location = new ArrayList<>();
    ArrayList<String> time = new ArrayList<>();
    ArrayList<String> details = new ArrayList<>();
    ArrayList<Double> lats = new ArrayList<>();
    ArrayList<Double> lngs = new ArrayList<>();

    private ArrayList<String> activityIdList = new ArrayList<String>();


    private final ArrayList<Activity> activities = new ArrayList<>();

    LatLng sydney = new LatLng(-34, 151);
    LatLng TamWorth = new LatLng(-31, 150.9);



    private FusedLocationProviderClient fusedLocationProviderClient;
    private Location mLastKnownLocation;
    private GoogleMap mMap;
    private LocationCallback locationCallback;

    private final float DEFAULT_ZOOM = 18;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mark_on_map);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map2);
        mapFragment.getMapAsync(this);

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(MarkOnMap.this);

        //latLngs.add(sydney);
        //latLngs.add(TamWorth);
        //activityName.add("activity1");
        //activityName.add("activity2");
        //owner.add("onwer1");
        //owner.add("owner2");
        //location.add("location1");
        //location.add("location2");

    }

    @SuppressLint("MissingPermission")
    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMyLocationEnabled(true);
        mMap.getUiSettings().setMyLocationButtonEnabled(true);
        mMap.getUiSettings().setZoomControlsEnabled(true);


        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(5000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest);

        SettingsClient settingsClient = LocationServices.getSettingsClient(MarkOnMap.this);
        Task<LocationSettingsResponse> task = settingsClient.checkLocationSettings(builder.build());

        task.addOnSuccessListener(MarkOnMap.this, new OnSuccessListener<LocationSettingsResponse>() {
            @Override
            public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
                getDeviceLocation();
            }
        });

        task.addOnFailureListener(MarkOnMap.this, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                if(e instanceof ResolvableApiException) {
                    ResolvableApiException resolvable = (ResolvableApiException) e;
                    try {
                        resolvable.startResolutionForResult(MarkOnMap.this, 51);
                    } catch (IntentSender.SendIntentException sendIntentException) {
                        sendIntentException.printStackTrace();
                    }
                }
            }
        });

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("activity");
        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, String s) {
                //activities.clear();
                Map<String, Object> map = (Map<String, Object>) snapshot.getValue();
                lats.add((Double) map.get("latitude"));
                lngs.add((Double) map.get("longitude"));
                activityName.add((String) map.get("title"));
                owner.add((String) map.get("owner"));
                location.add((String) map.get("placeName"));
                time.add((String) map.get("datetime"));
                details.add((String) map.get("details"));
                for (int i=0; i<lats.size(); i++) {
                    mMap.addMarker(new MarkerOptions().position(new LatLng(lats.get(i), lngs.get(i)))
                            .title(activityName.get(i))
                            .snippet("owner: " + owner.get(i) +
                                    "\nlocation: " + location.get(i) +
                                    "\ntime: " + time.get(i) +
                                    "\ndetails: " + details.get(i)));
                    //mMap.animateCamera(CameraUpdateFactory.zoomTo());
                }
            }
            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String s) {

            }
            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }
            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String s) {

            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        mMap.setInfoWindowAdapter(new infoWindowAdapter(MarkOnMap.this));

        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(@NonNull Marker marker) {
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(marker.getPosition(), DEFAULT_ZOOM));
                return false;
            }
        });

        mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(@NonNull Marker marker) {
                String title = marker.getTitle();
                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("activity");
                databaseReference.addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                        Map<String, Object> map = (Map<String, Object>) snapshot.getValue();
                        if(map.get("title").equals(title)) {
                            Intent intent = new Intent(MarkOnMap.this, DetailActivity.class);
                            HashMap activity = (HashMap) map;
                            intent.putExtra("activityInfo", activity);
                            startActivity(intent);
                        }
                    }
                    @Override
                    public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                    }
                    @Override
                    public void onChildRemoved(@NonNull DataSnapshot snapshot) {

                    }
                    @Override
                    public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            }
        });
    }

    @SuppressLint("MissingPermission")
    private void getDeviceLocation() {
        fusedLocationProviderClient.getLastLocation()
                .addOnCompleteListener(new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                        if(task.isSuccessful()) {
                            mLastKnownLocation = task.getResult();
                            if(mLastKnownLocation != null) {
                                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(mLastKnownLocation.getLatitude(), mLastKnownLocation.getLongitude()), DEFAULT_ZOOM));
                            } else {
                                final LocationRequest locationRequest = LocationRequest.create();
                                locationRequest.setInterval(10000);
                                locationRequest.setFastestInterval(5000);
                                locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
                                locationCallback = new LocationCallback() {
                                    @Override
                                    public void onLocationResult(LocationResult locationResult) {
                                        super.onLocationResult(locationResult);
                                        if(locationResult == null) {
                                            return;
                                        }
                                        mLastKnownLocation = locationResult.getLastLocation();
                                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(mLastKnownLocation.getLatitude(), mLastKnownLocation.getLongitude()), DEFAULT_ZOOM));
                                        fusedLocationProviderClient.removeLocationUpdates(locationCallback);
                                    }
                                };
                                fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, null);
                            }
                        } else {
                            Toast.makeText(MarkOnMap.this, "unable to get last location", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }
}