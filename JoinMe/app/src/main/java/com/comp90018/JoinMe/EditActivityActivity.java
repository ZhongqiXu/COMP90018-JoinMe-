package com.comp90018.JoinMe;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
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

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import helper.HorizontalNumberPicker;
import object.Activity;

public class EditActivityActivity extends AppCompatActivity implements NavigationBarView.OnItemSelectedListener {

    NavigationBarView bottomNavigationView;
    EditText title, details;
    Button edit_confirm;

    private TextView datePicker, timePicker, location;
    private Button datePickerBtn, timePickerBtn, locationBtn;
    private TimePickerDialog.OnTimeSetListener onTimeSetListener;
    private DatePickerDialog.OnDateSetListener onDateSetListener;
    private ToggleButton autoJoinBtn;
    private boolean isAutoJoin;
    private HorizontalNumberPicker activitySize;

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;

    private String date;
    private String time;
    private String aid;

    private String locationName;
    private LatLng locationLatLng;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_activity);

        Bundle bundle = getIntent().getExtras();
        String activityInfo = bundle.getString("activityInfo");
        Activity activity = new Activity();
        activity.stringToActivity(activity, activityInfo.substring(1, activityInfo.length() - 1));

        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnItemSelectedListener(this);
        bottomNavigationView.getMenu().findItem(R.id.activities).setChecked(true);

        title=findViewById(R.id.activity_title);
        details=findViewById(R.id.activity_details);
        edit_confirm=findViewById(R.id.create_activity);

        datePicker = findViewById(R.id.activity_date);
        datePickerBtn = findViewById(R.id.activity_date_btn);
        timePicker = findViewById(R.id.activity_time);
        timePickerBtn = findViewById(R.id.activity_time_btn);
        location = findViewById(R.id.activity_location);
        locationBtn = findViewById(R.id.activity_location_btn);
        autoJoinBtn = (ToggleButton) findViewById(R.id.activity_autoJoin_btn);

        activitySize = findViewById(R.id.activity_size_btn);

        date = activity.getDatetime().split(" ")[0];
        time = activity.getDatetime().split(" ")[1];

        title.setText(activity.getTitle());
        datePicker.setText(date);
        timePicker.setText(time);
        location.setText(activity.getPlaceName());
        details.setText(activity.getDetails());
        activitySize.setValue(activity.getSize());
        aid = activity.getAid();

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference().child("activity").child(aid);


        // edit activity
        edit_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    EditActivity();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        });


        // set date
        datePickerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(
                        EditActivityActivity.this,
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
                        EditActivityActivity.this,
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

    // Edit activity
    public void EditActivity() throws ParseException {
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
            Toast.makeText(EditActivityActivity.this,"Please enter title.",Toast.LENGTH_SHORT).show();
        else if (TextUtils.isEmpty(date))
            Toast.makeText(EditActivityActivity.this,"Please enter date.",Toast.LENGTH_SHORT).show();
        else if (TextUtils.isEmpty(time))
            Toast.makeText(EditActivityActivity.this,"Please enter time.",Toast.LENGTH_SHORT).show();
        else if (TextUtils.isEmpty(locationName))
            Toast.makeText(EditActivityActivity.this,"Please enter location.",Toast.LENGTH_SHORT).show();
        else if (activitySize.getValue() <= 0)
            Toast.makeText(EditActivityActivity.this,"activity size must be more than 0.",Toast.LENGTH_SHORT).show();
        else{
            Activity newActivity = new Activity();

            // setup  attributes
            newActivity.setTitle(title.getText().toString());
            newActivity.setDatetime(dateTime);
            newActivity.setOwner(uid);
            newActivity.setDetails(details.getText().toString());
            newActivity.setSize(activitySize.getValue());
            newActivity.setAutoJoin(isAutoJoin);
            newActivity.setPlaceName(locationName);
            newActivity.setLatLng(locationLatLng);
            newActivity.setAid(aid);

            Map<String, Object> activityValues = newActivity.toMap();

            databaseReference.updateChildren(activityValues).addOnCompleteListener(
                    task -> {
                        Toast.makeText(EditActivityActivity.this, "Edit activity success.", Toast.LENGTH_SHORT).show();

                        Intent intent = new Intent(EditActivityActivity.this, MyActivityListActivity.class);
                        startActivity(intent);
                        finish();
                    }
            );
        }




    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        bottomNavigationView.getMenu().findItem(item.getItemId()).setChecked(true);
        switch (item.getItemId()) {
            case R.id.activities:
                startActivity(new Intent(this, MainActivity.class));
                break;
            case R.id.profile:
                startActivity(new Intent(this, MeActivity.class));
                break;
            case R.id.settings:
                startActivity(new Intent(this, SettingActivity.class));
                break;
            default:
                break;
        }
        return false;
    }
}
