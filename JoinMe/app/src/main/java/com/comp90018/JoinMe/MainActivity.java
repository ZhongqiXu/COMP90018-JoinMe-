package com.comp90018.JoinMe;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements NavigationBarView.OnItemSelectedListener {

    NavigationBarView bottomNavigationView;
    Button manageActivity;

    private ListView activityListView;
    private DatabaseReference databaseReference;

    private ArrayList<String> activityList = new ArrayList<String>();
    private ArrayList<String> activityIdList = new ArrayList<String>();
    private ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        manageActivity = findViewById(R.id.newActivity);

        manageActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, NewActivity.class));
            }
        });

        bottomNavigationView = findViewById(R.id.bottomNavigationView);

        bottomNavigationView.setOnItemSelectedListener(this);
        bottomNavigationView.getMenu().findItem(R.id.activities).setChecked(true);

        adapter = new ArrayAdapter<String>(MainActivity.this, R.layout.activity_list_view, activityList);

        activityListView = (ListView) findViewById(R.id.activity_List);
        activityListView.setAdapter(adapter);
        activityListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                FirebaseDatabase.getInstance().getReference().child("activity").child(activityIdList.get(position))
                        .get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DataSnapshot> task) {
                        if (!task.isSuccessful()) {
                            Log.e("firebase", "Error getting data", task.getException());
                        }
                        else {
                            Intent intent = new Intent(MainActivity.this, DetailActivity.class);
                            intent.putExtra("activityInfo", task.getResult().getValue().toString());
                            startActivity(intent);
                        }
                    }
                });
            }
        });

        databaseReference = FirebaseDatabase.getInstance().getReference().child("activity");
        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, String s) {
                @SuppressWarnings("unchecked")
                Map<String, Object> map = (Map<String, Object>) dataSnapshot.getValue();
                activityList.add((String) map.get("title"));
                activityIdList.add((String) dataSnapshot.getKey());

                adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



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