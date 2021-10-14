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

public class NewActivity extends AppCompatActivity implements NavigationBarView.OnItemSelectedListener {

    NavigationBarView bottomNavigationView;
    EditText title, details;
    Button createActivity;

    private TextView datePicker, timePicker;
    private Button datePickerBtn, timePickerBtn;
    private TimePickerDialog.OnTimeSetListener onTimeSetListener;
    private DatePickerDialog.OnDateSetListener onDateSetListener;
    private ToggleButton autoJoinBtn;
    private boolean isAutoJoin;
    private HorizontalNumberPicker activitySize;

    private String date;
    private String time;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new);

        bottomNavigationView = findViewById(R.id.bottomNavigationView);

        bottomNavigationView.setOnItemSelectedListener(this);
        bottomNavigationView.getMenu().findItem(R.id.activities).setChecked(true);

        title=findViewById(R.id.activity_title);
        details=findViewById(R.id.activity_details);
        createActivity=findViewById(R.id.create_activity);

        datePicker = findViewById(R.id.activity_date);
        datePickerBtn = findViewById(R.id.activity_date_btn);
        timePicker = findViewById(R.id.activity_time);
        timePickerBtn = findViewById(R.id.activity_time_btn);
        autoJoinBtn = (ToggleButton) findViewById(R.id.activity_autoJoin_btn);

        activitySize = findViewById(R.id.activity_size_btn);


        createActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(title.getText().toString()))
                    Toast.makeText(NewActivity.this, "Please enter title.", Toast.LENGTH_SHORT).show();
                else {
                    try {
                        newActivity();
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

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
        onDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month = month + 1;
                date = dayOfMonth + "/" + month + "/" + year;
                datePicker.setText(date);
            }
        };

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

        onTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                time = hourOfDay + ":" + minute;
                timePicker.setText(time);
            }
        };

        autoJoinBtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                autoJoinBtn.setChecked(isChecked);
                isAutoJoin = isChecked;
            }
        });

    }

    public void newActivity() throws ParseException {
        String uid = "";
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();
        if (user != null) {
            uid = user.getUid();
        }

        String dateTime = date +  ' ' + time;

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        String key = database.getReference("activity").push().getKey();

        final Map<String, Object> dataMap = new HashMap<String, Object>();
        Activity newActivity = new Activity(title.getText().toString(), dateTime, uid,
                new ArrayList<>(0), details.getText().toString(), activitySize.getValue(), isAutoJoin);
        FirebaseDatabase.getInstance().getReference().child("activity").child(key).setValue(newActivity).addOnCompleteListener(
                task -> {
                    Toast.makeText(NewActivity.this, "Create activity success.", Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(NewActivity.this,MainActivity.class);
                    startActivity(intent);
                    finish();
                }
        );



    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        bottomNavigationView.getMenu().findItem(item.getItemId()).setChecked(true);
        switch (item.getItemId()) {
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