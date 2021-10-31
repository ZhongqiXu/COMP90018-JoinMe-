package com.comp90018.JoinMe;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Objects;

import object.Activity;

public class MyActivityDetailActivity extends AppCompatActivity implements OnMapReadyCallback {

    TextView host, title, details, dateTime, autoEnabled, size;
    Button back;
    Bundle bundle;
    Button edit;
    private Button candidate_list;
    Activity activity = new Activity();
    HashMap activityInfo;
    private double latitude = 200, longitude = 100;
    private String placeName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_activity);

        bundle = getIntent().getExtras();
        activityInfo = (HashMap) bundle.get("activityInfo");
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
        placeName = activity.getPlaceName();
        latitude = activity.getLatitude();
        longitude = activity.getLongitude();

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

        back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MyActivityDetailActivity.this, MyActivityListActivity.class));
            }
        });


        candidate_list = findViewById(R.id.activity_List);
        candidate_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MyActivityDetailActivity.this, CandidateList.class);
                intent.putExtra("activityInfo", activityInfo);
                startActivity(intent);
            }
        });


    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.my_activity_menu,menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.delete:
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
                alertDialog
                        .setTitle("title")
                        .setMessage("Are you sure you want to delete this activity?")
                        .setPositiveButton("OK",
                                new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface dialog,
                                                        int which) {
                                        FirebaseDatabase.getInstance().getReference().child("activity").child(activity.getAid()).removeValue();
                                        Intent intent = new Intent(MyActivityDetailActivity.this,MyActivityListActivity.class);
                                        startActivity(intent);
                                        finish();
                                    }
                                })
                        .setNegativeButton("Cancel",
                                new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface dialog,
                                                        int which) {

                                    }
                                }).show();
                break;
            default:
                break;
        }
        return true;

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
