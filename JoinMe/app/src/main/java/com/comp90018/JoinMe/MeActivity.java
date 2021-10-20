package com.comp90018.JoinMe;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Map;

import object.User;

public class MeActivity extends AppCompatActivity implements NavigationBarView.OnItemSelectedListener {


    TextView userName,welname,gender,age,brief,email;
    FirebaseAuth mAuth;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    NavigationBarView bottomNavigationView;

    private Button button_map;



    @Override
    protected void onCreate(Bundle savedInstanceState) {

        mAuth = FirebaseAuth.getInstance();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_me);

        welname = findViewById(R.id.wel_name);
        userName = findViewById(R.id.ds_name);
        gender = findViewById(R.id.ds_gender);
        email = findViewById(R.id.ds_email);
        brief = findViewById(R.id.ds_brief);
        age = findViewById(R.id.ds_age);

        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnItemSelectedListener(this);
        bottomNavigationView.getMenu().findItem(R.id.profile).setChecked(true);

        final String uid = mAuth.getUid();

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference().child("user").child(uid);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull  DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);
                System.out.println(user);
                welname.setText(user.getUserName());
                bind(user);
                Toast.makeText(MeActivity.this,"seucc",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(@NonNull  DatabaseError error) {
                Toast.makeText(MeActivity.this,error.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });

        // put the button into the navigation bar latter
        button_map = findViewById(R.id.button_map);
        button_map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MeActivity.this, MarkOnMap.class));
            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.me_menu,menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.edit:
                startActivity(new Intent(MeActivity.this, SettingActivity.class));
                break;
            case R.id.logout:
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(MeActivity.this, LoginActivity.class));
                finish();
                break;
            case R.id.myActivities:
                startActivity(new Intent(MeActivity.this, MyActivityListActivity.class));
                break;
            default:
                break;
        }
        return true;

    }

    public void bind(User user){
        userName.setText(user.getUserName());
        gender.setText(user.getGender());
        email.setText(user.getEmail());
        brief.setText(user.getBrief());
        age.setText(user.getAge());
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        bottomNavigationView.getMenu().findItem(item.getItemId()).setChecked(true);
        switch (item.getItemId()) {
            case R.id.activities:
                startActivity(new Intent(this, MainActivity.class));
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