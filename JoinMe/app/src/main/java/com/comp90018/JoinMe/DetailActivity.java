package com.comp90018.JoinMe;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Map;
import java.util.Objects;

import helper.HorizontalNumberPicker;
import object.Activity;

public class DetailActivity extends AppCompatActivity implements NavigationBarView.OnItemSelectedListener {
    NavigationBarView bottomNavigationView;

    TextView host, title, details, dateTime, autoEnabled, size;
    Button back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Bundle bundle = getIntent().getExtras();
        String activityInfo = bundle.getString("activityInfo");
        Activity activity = new Activity();
        activity.stringToActivity(activity, activityInfo.substring(1, activityInfo.length() - 1));

        bottomNavigationView = findViewById(R.id.bottomNavigationView);

        bottomNavigationView.setOnItemSelectedListener(this);
        bottomNavigationView.getMenu().findItem(R.id.activities).setChecked(true);

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
                startActivity(new Intent(DetailActivity.this, MainActivity.class));
            }
        });
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
