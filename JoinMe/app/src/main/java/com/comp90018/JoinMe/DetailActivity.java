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
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

<<<<<<< Updated upstream
=======
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
<<<<<<< Updated upstream
>>>>>>> Stashed changes
=======
>>>>>>> Stashed changes
import java.util.Map;
import java.util.Objects;

import helper.HorizontalNumberPicker;
import object.Activity;

public class DetailActivity extends AppCompatActivity implements NavigationBarView.OnItemSelectedListener {
    NavigationBarView bottomNavigationView;

    TextView host, title, details, dateTime, autoEnabled, size, placeName;
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
        placeName = findViewById(R.id.activity_location_view);

        autoEnabled = findViewById(R.id.activity_autoJoin_view);
        size = findViewById(R.id.activity_size_view);

        title.setText(activity.getTitle());
        details.setText(activity.getDetails());
        dateTime.setText(activity.getDatetime());
        autoEnabled.setText(String.valueOf(activity.isAutoJoin()));
        placeName.setText(activity.getPlaceName());
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
<<<<<<< Updated upstream
=======
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
>>>>>>> Stashed changes
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
