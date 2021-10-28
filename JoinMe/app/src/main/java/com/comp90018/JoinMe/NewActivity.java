package com.comp90018.JoinMe;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

import java.text.ParseException;
import java.util.Calendar;

import helper.HorizontalNumberPicker;
import object.Activity;

public class NewActivity extends AppCompatActivity implements OnMapReadyCallback {

    EditText title, details;
    Button createActivity;

    private TextView datePicker, timePicker;
    private Button datePickerBtn, timePickerBtn, locationBtn;
    private TimePickerDialog.OnTimeSetListener onTimeSetListener;
    private DatePickerDialog.OnDateSetListener onDateSetListener;
    private ToggleButton autoJoinBtn;
    private boolean isAutoJoin;
    private HorizontalNumberPicker activitySize;

    private String date;
    private String time;

    static public String locationName;
    static public LatLng locationLatLng;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new);

        title = findViewById(R.id.activity_title);
        details = findViewById(R.id.activity_details);
        createActivity = findViewById(R.id.create_activity);

        datePicker = findViewById(R.id.activity_date);
        datePickerBtn = findViewById(R.id.activity_date_btn);
        timePicker = findViewById(R.id.activity_time);
        timePickerBtn = findViewById(R.id.activity_time_btn);
        locationBtn = findViewById(R.id.activity_location_btn);
        autoJoinBtn = (ToggleButton) findViewById(R.id.activity_autoJoin_btn);

        activitySize = findViewById(R.id.activity_size_btn);

        SupportMapFragment mapFragment = (SupportMapFragment)this.getSupportFragmentManager().findFragmentById(R.id.map_new);
        mapFragment.getMapAsync(this);


        // create new activity
        createActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    newActivity();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        });


        // set location
        locationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: replace with action direct to map
                Intent intent = new Intent(NewActivity.this, MapActivity.class);
                startActivity(intent);
                //location = null;
                //locationName =  "";
                //locationLatLng =  new LatLng(0, 0);
            }
        });
        // TODO: set onDataSetListener for location, require result of querying location

        // set date
        datePickerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(
                        NewActivity.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        onDateSetListener,
                        year, month, day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();

            }
        });
        // change text in date field
        onDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month = month + 1;
                date = dayOfMonth + "/" + month + "/" + year;
                datePicker.setText(date);
            }
        };

        // set time
        timePickerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                int hour = calendar.get(Calendar.HOUR_OF_DAY);
                int min = calendar.get(Calendar.MINUTE);

                TimePickerDialog dialog = new TimePickerDialog(
                        NewActivity.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        onTimeSetListener,
                        hour, min, true);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();

            }
        });
        // change text in time field
        onTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                time = hourOfDay + ":" + minute;
                timePicker.setText(time);
            }
        };

        // set enable auto join button
        autoJoinBtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                autoJoinBtn.setChecked(isChecked);
                isAutoJoin = isChecked;
            }
        });

    }

    // create activity
    public void newActivity() throws ParseException {
        String uid = "";
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();
        if (user != null) {
            uid = user.getUid();
        }

        String dateTime = date +  ' ' + time;

        FirebaseDatabase database = FirebaseDatabase.getInstance();

        // validate empty fields
        if (TextUtils.isEmpty(title.getText().toString()))
            Toast.makeText(NewActivity.this,"Please enter title.",Toast.LENGTH_SHORT).show();
        else if (TextUtils.isEmpty(date))
            Toast.makeText(NewActivity.this,"Please choose date.",Toast.LENGTH_SHORT).show();
        else if (TextUtils.isEmpty(time))
            Toast.makeText(NewActivity.this,"Please choose time.",Toast.LENGTH_SHORT).show();
        else if (locationLatLng == null)
            Toast.makeText(NewActivity.this,"Please choose location.",Toast.LENGTH_SHORT).show();
        else if (activitySize.getValue() <= 0)
            Toast.makeText(NewActivity.this,"activity size must be more than 0.",Toast.LENGTH_SHORT).show();
        else{
            String key = database.getReference("activity").push().getKey();
            Activity newActivity = new Activity();

            // setup  attributes

            newActivity.setAid(key);
            newActivity.setTitle(title.getText().toString());
            newActivity.setDatetime(dateTime);
            newActivity.setOwner(uid);
            newActivity.setDetails(details.getText().toString());
            newActivity.setSize(activitySize.getValue());
            newActivity.setAutoJoin(isAutoJoin);
            newActivity.setPlaceName(locationName);
            newActivity.setLatitude(locationLatLng.latitude);
            newActivity.setLongitude(locationLatLng.longitude);
            //newActivity.setLatLng(locationLatLng);

            FirebaseDatabase.getInstance().getReference().child("activity").child(key).setValue(newActivity).addOnCompleteListener(
                    task -> {
                        Toast.makeText(NewActivity.this, "Create activity success.", Toast.LENGTH_SHORT).show();

                        Intent intent = new Intent(NewActivity.this,MainActivity.class);
                        startActivity(intent);
                        finish();
                    }
            );
        }
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        if(locationLatLng != null){
            googleMap.addMarker(new MarkerOptions().position(locationLatLng).title(locationName));
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(locationLatLng,15));
            // Zoom in, animating the camera.
            googleMap.animateCamera(CameraUpdateFactory.zoomIn());
            // Zoom out to zoom level 10, animating with a duration of 2 seconds.
            googleMap.animateCamera(CameraUpdateFactory.zoomTo(15), 2000, null);

        }

    }
}
