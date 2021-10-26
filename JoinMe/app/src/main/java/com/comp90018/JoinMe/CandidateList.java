package com.comp90018.JoinMe;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;

import java.util.ArrayList;
import java.util.Map;

public class CandidateList extends AppCompatActivity {
    NavigationBarView bottomNavigationView;

    private List<Map<String, Object>> mData;
    private int flag;

    private ListView candidateListView;
    private DatabaseReference databaseReference;
    private ArrayList<String> candidateList = new ArrayList<String>();
    private ArrayList<String> candidateIdList = new ArrayList<String>();
    private ArrayAdapter<String> adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.candidate_list);

        adapter = new ArrayAdapter<String>(CandidateList.this, R.layout.candidate_list_view, candidateList);

        candidateListView = (ListView) findViewById(R.id.candidate_list);
        candidateListView.setAdapter(adapter);


        databaseReference = FirebaseDatabase.getInstance().getReference().child("activity");
        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, String s) {
//                 TODO change to candidatelist (now use activity title )
                @SuppressWarnings("unchecked")
                Map<String, Object> map = (Map<String, Object>) dataSnapshot.getValue();
                candidateList.add((String) map.get("details"));
                candidateIdList.add((String) dataSnapshot.getKey());

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



}
