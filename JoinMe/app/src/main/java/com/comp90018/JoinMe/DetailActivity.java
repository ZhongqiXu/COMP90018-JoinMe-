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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import object.Activity;

public class DetailActivity extends AppCompatActivity implements NavigationBarView.OnItemSelectedListener {
    NavigationBarView bottomNavigationView;

    private TextView host, title, details, dateTime, autoEnabled, size, placeName;
    private Button back;
    private Button join;
    private String owner;
    private List<String> participants = new ArrayList<>(1);
    private List<String> candidates = new ArrayList<>(1);
    private String currentUser, aId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Bundle bundle = getIntent().getExtras();
        HashMap activityInfo = (HashMap) bundle.get("activityInfo");

        Activity activity = new Activity();
        activity.mapToActivity(activity, activityInfo);

        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnItemSelectedListener(this);
        bottomNavigationView.getMenu().findItem(R.id.activities).setChecked(true);

        host = findViewById(R.id.activity_host);
        title = findViewById(R.id.activity_title);
        details = findViewById(R.id.activity_details);
        dateTime = findViewById(R.id.activity_date_view);
        placeName = findViewById(R.id.activity_location_view);

        autoEnabled = findViewById(R.id.activity_autoJoin_view);
        size = findViewById(R.id.activity_size_view);

        title.setText(activity.getTitle());
        details.setText(activity.getDetails());
        dateTime.setText(activity.getDatetime());
        autoEnabled.setText(String.valueOf(activity.isAutoJoin()));
        System.out.println("place:"+activity.getPlaceName());
        placeName.setText(activity.getPlaceName());
        size.setText(String.valueOf(activity.getSize()));

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

        back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DetailActivity.this, MainActivity.class));
            }
        });
        join = findViewById(R.id.join);
        join.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog alertDialog = new AlertDialog.Builder(DetailActivity.this).create();
                alertDialog.setTitle("You can not join this activity");
                if (currentUser.equals(owner)){
                    alertDialog.setMessage("You are the host of this activity, no need to join!");
                    alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "CLOSE",new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    alertDialog.show();
                }
                else if (participants != null && !participants.isEmpty() && participants.contains(currentUser)){
                    alertDialog.setMessage("You are already in this activity!");
                    alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "CLOSE",new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    alertDialog.show();
                }else if (candidates != null && !candidates.isEmpty() && candidates.contains(currentUser)){
                    alertDialog.setMessage("You are already in the waiting list!");
                    alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "CLOSE",new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    alertDialog.show();
                }
                else{
                    createAlertDialog();
                }
            }
        });
    }

    public void createAlertDialog() {
        AlertDialog alertDialog = new AlertDialog.Builder(DetailActivity.this).create();
        alertDialog.setTitle("Are you sure?");
        alertDialog.setMessage("Confirm to join this activity?");
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "YES",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        if (autoEnabled.getText().equals("false")){
                            candidates.add(currentUser);
                            FirebaseDatabase.getInstance().getReference().child("activity").child(aId).child("candidates")
                                    .setValue(candidates).addOnSuccessListener(new OnSuccessListener() {
                                @Override
                                public void onSuccess(Object o) {
                                    AlertDialog alertDialog = new AlertDialog.Builder(DetailActivity.this).create();
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
                                    AlertDialog alertDialog = new AlertDialog.Builder(DetailActivity.this).create();
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
