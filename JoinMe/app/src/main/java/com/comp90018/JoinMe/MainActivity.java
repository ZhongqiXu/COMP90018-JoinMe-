package com.comp90018.JoinMe;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
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
import java.util.HashMap;
import java.util.Map;

import helper.MyListAdapter;

public class MainActivity extends AppCompatActivity implements NavigationBarView.OnItemSelectedListener {

    NavigationBarView bottomNavigationView;

    private ListView activityListView;
    private DatabaseReference databaseReference;

    private ArrayList<String> activityList = new ArrayList<String>();
    private ArrayList<String> activityListDetail = new ArrayList<>();
    private ArrayList<String> activityIdList = new ArrayList<String>();
    private ArrayAdapter<String> adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        // using toolbar as ActionBar
        setSupportActionBar(toolbar);

        ImageView imageView = (ImageView) findViewById(R.id.imageView);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override

            // incrementing the value of textView
            public void onClick( View view ) {
                startActivity(new Intent(MainActivity.this, MapActivity.class));
            }
        });

        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnItemSelectedListener(this);
        bottomNavigationView.getMenu().findItem(R.id.activities).setChecked(true);

        MyListAdapter adapter = new MyListAdapter(MainActivity.this,  activityList, activityListDetail);

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
                            HashMap activity = (HashMap) task.getResult().getValue();
                            intent.putExtra("activityInfo", activity);
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
                activityListDetail.add("[" + (String) map.get("datetime") + "] " + (String) map.get("details"));
                activityIdList.add((String) dataSnapshot.getKey());
                setListViewHeightBasedOnChildren(activityListView);

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

    public void setListViewHeightBasedOnChildren(ListView activityListView) {
        ListAdapter listAdapter = activityListView.getAdapter();
        if (listAdapter == null) {
            //pre-condition
            return;
        }

        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, activityListView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = activityListView.getLayoutParams();
        params.height = totalHeight + (activityListView.getDividerHeight() * (listAdapter.getCount() - 1))
                + bottomNavigationView.getHeight();
        activityListView.setLayoutParams(params);
        activityListView.requestLayout();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        bottomNavigationView.getMenu().findItem(item.getItemId()).setChecked(true);
        switch (item.getItemId()) {
            case R.id.Me:
                startActivity(new Intent(this, MeActivity.class));
                break;
            case R.id.mapView:
                startActivity(new Intent(this, MarkOnMap.class));
                break;
            case R.id.chats:
                startActivity(new Intent(this, ChatActivity.class));
                break;
            default:
                break;
        }
        return false;
    }


}