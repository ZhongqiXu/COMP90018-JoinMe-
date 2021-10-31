package com.comp90018.JoinMe;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import object.Activity;

public class MyActivityDetailActivity extends AppCompatActivity implements OnMapReadyCallback {

    private TextView host, title, details, dateTime, autoEnabled, size;
    private Button candidate_list;
    private String owner, placeName;
    private List<String> participants = new ArrayList<>(1);
    private List<String> candidates = new ArrayList<>(1);
    private String currentUser, aId;
    private double latitude = 200, longitude = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_activity);

        Bundle bundle = getIntent().getExtras();
        HashMap activityInfo = (HashMap) bundle.get("activityInfo");

        Activity activity = new Activity();
        activity.mapToActivity(activity, activityInfo);

        host = findViewById(R.id.activity_host);
        title = findViewById(R.id.activity_title);
        details = findViewById(R.id.activity_details);
        dateTime = findViewById(R.id.activity_date_view);

        autoEnabled = findViewById(R.id.activity_autoJoin_view);
        size = findViewById(R.id.activity_size_view);

        title.setText(activity.getTitle());
        details.setText(activity.getDetails());
        dateTime.setText(activity.getDatetime());
        autoEnabled.setText(String.valueOf(activity.isAutoJoin()));
        size.setText(String.valueOf(activity.getSize()));

        latitude = activity.getLatitude();
        longitude = activity.getLongitude();
        placeName = activity.getPlaceName();

        SupportMapFragment mapFragment = (SupportMapFragment)this.getSupportFragmentManager().findFragmentById(R.id.map_detail);
        mapFragment.getMapAsync(this);

        owner = activity.getOwner();
        if (activity.getParticipants() != null && !activity.getParticipants().isEmpty()) {
            participants = activity.getParticipants();
        }
        if (activity.getCandidates() != null && !activity.getCandidates().isEmpty()) {
            candidates = activity.getCandidates();
        }
        currentUser = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
        aId = activity.getAid();


        FirebaseDatabase.getInstance().getReference().child("user").child(activity.getOwner()).child("userName")
                .get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e("firebase", "Error getting data", task.getException());
                }
                else {
                    host.setText(String.valueOf(Objects.requireNonNull(task.getResult()).getValue()));
                }
            }
        });

        candidate_list = findViewById(R.id.candidate_list);
        candidate_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MyActivityDetailActivity.this, CandidateList.class);
                intent.putExtra("activityInfo", activityInfo);
                startActivity(intent);
            }
        });
    }

    public void createAlertDialog() {
        AlertDialog alertDialog = new AlertDialog.Builder(MyActivityDetailActivity.this).create();
        alertDialog.setTitle("Are you sure?");
        alertDialog.setMessage("Confirm to join this activity?");
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "YES",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        //added for chatting
                        Calendar calender = Calendar.getInstance();
                        int time = (int)(calender.getTimeInMillis()/1000);
                        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
                        DocumentReference dfCurrent = firebaseFirestore.collection("User").document(currentUser);
                        dfCurrent.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if (task.isSuccessful()) {
                                    DocumentSnapshot document = task.getResult();
                                    if (document != null) {
                                        DocumentReference documentReference=firebaseFirestore.collection("Contact").document(owner+currentUser);
                                        String imageCurrent = document.getString("image");
                                        String nameCurrent = document.getString("name");
                                        Map<String, Object> contact = new HashMap<>();
                                        contact.put("name", nameCurrent);
                                        contact.put("image", imageCurrent);
                                        contact.put("uid", owner);
                                        contact.put("time", time);
                                        contact.put("uid_contacter", currentUser);
                                        documentReference.set(contact);
                                    } else {
                                        Log.d("LOGGER", "No such document");
                                    }
                                } else {
                                    Log.d("LOGGER", "get failed with ", task.getException());
                                }
                            }
                        });
                        DocumentReference dfOwner = firebaseFirestore.collection("User").document(owner);
                        dfOwner.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if (task.isSuccessful()) {
                                    DocumentSnapshot document = task.getResult();
                                    if (document != null) {
                                        DocumentReference documentReference=firebaseFirestore.collection("Contact").document(currentUser+owner);
                                        String imageOwner = document.getString("image");
                                        String nameOwner = document.getString("name");
                                        Map<String, Object> contact = new HashMap<>();
                                        contact.put("name", nameOwner);
                                        contact.put("image", imageOwner);
                                        contact.put("uid", currentUser);
                                        contact.put("time", time);
                                        contact.put("uid_contacter", owner);
                                        documentReference.set(contact);
                                    } else {
                                        Log.d("LOGGER", "No such document");
                                    }
                                } else {
                                    Log.d("LOGGER", "get failed with ", task.getException());
                                }
                            }
                        });

                        if (autoEnabled.getText().equals("false")){
                            candidates.add(currentUser);
                            FirebaseDatabase.getInstance().getReference().child("activity").child(aId).child("candidates")
                                    .setValue(candidates).addOnSuccessListener(new OnSuccessListener() {
                                @Override
                                public void onSuccess(Object o) {
                                    AlertDialog alertDialog = new AlertDialog.Builder(MyActivityDetailActivity.this).create();
                                    alertDialog.setTitle("Please wait");
                                    alertDialog.setMessage("This activity needs confirmation from host, please wait for response");
                                    alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "CLOSE",new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
                                        }
                                    });
                                    alertDialog.show();
                                }
                            });
                        }else{
                            participants.add(currentUser);
                            FirebaseDatabase.getInstance().getReference().child("activity").child(aId).child("participants")
                                    .setValue(participants).addOnSuccessListener(new OnSuccessListener() {
                                @Override
                                public void onSuccess(Object o) {
                                    AlertDialog alertDialog = new AlertDialog.Builder(MyActivityDetailActivity.this).create();
                                    alertDialog.setTitle("Confirmation of your join");
                                    alertDialog.setMessage("Join activity successfully!");
                                    alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "CLOSE",new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
                                        }
                                    });
                                    alertDialog.show();
                                }
                            });
                        }
                    }
                });
        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "NO",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        alertDialog.show();
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        if(longitude >= -180 && longitude <= 180 && latitude >= -90 && latitude <= 90){
            LatLng locationLatLng = new LatLng(latitude, longitude);
            googleMap.addMarker(new MarkerOptions().position(locationLatLng).title(placeName));
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(locationLatLng,15));
            // Zoom in, animating the camera.
            googleMap.animateCamera(CameraUpdateFactory.zoomIn());
            // Zoom out to zoom level 10, animating with a duration of 2 seconds.
            googleMap.animateCamera(CameraUpdateFactory.zoomTo(15), 2000, null);
        }

    }
}