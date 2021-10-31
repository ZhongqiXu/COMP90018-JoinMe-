
package com.comp90018.JoinMe;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import object.Activity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ParticipantsList extends AppCompatActivity{

    private ListView participantListView;
    private DatabaseReference databaseReference;

    private List<String> participantIdList= new ArrayList<String>(1);
    private ArrayList<String> participantList = new ArrayList<String>(1);
    private ArrayAdapter<String> adapter;
    private String aId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {



        super.onCreate(savedInstanceState);
        setContentView(R.layout.participants_list);

        Bundle bundle = getIntent().getExtras();
        HashMap activityInfo = (HashMap) bundle.get("activityInfo");
        object.Activity activity = new Activity();
        activity.mapToActivity(activity, activityInfo);
        aId = activity.getAid();

        adapter = new ArrayAdapter<String>(ParticipantsList.this, R.layout.candidate_list_view, participantList);

        participantListView = (ListView) findViewById(R.id.list_view2);
        participantListView.setAdapter(adapter);

        if (activity.getParticipants() != null && !activity.getParticipants().isEmpty()) {
//             participantIdList = activity.getParticipants();

            databaseReference = FirebaseDatabase.getInstance().getReference().child("activity").child(aId).child("participants");
            databaseReference.addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot dataSnapshot, String s) {
                    @SuppressWarnings("unchecked")

                    String value = dataSnapshot.getValue(String.class);
                    participantIdList.add(value);
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

        System.out.println(participantIdList);

        databaseReference = FirebaseDatabase.getInstance().getReference().child("user");
        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, String s) {
                @SuppressWarnings("unchecked")
                Map<String, Object> map = (Map<String, Object>) dataSnapshot.getValue();

                for (int i = 0; i < participantIdList.size(); i++){
                    if (participantIdList.get(i).equals(map.get("uid"))){

                        participantList.add((String) map.get("userName"));

                    }

                    adapter.notifyDataSetChanged();

                }

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


