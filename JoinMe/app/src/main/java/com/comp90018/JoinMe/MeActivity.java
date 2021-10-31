package com.comp90018.JoinMe;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;

public class MeActivity extends AppCompatActivity implements NavigationBarView.OnItemSelectedListener{
    NavigationBarView bottomNavigationView;
    private Button profile,my,join,logout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_me);

        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnItemSelectedListener(this);
        bottomNavigationView.getMenu().findItem(R.id.Me).setChecked(true);

        profile = findViewById(R.id.profile);
        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MeActivity.this, ProfileActivity.class));
            }
        });

        my = findViewById(R.id.my_activities);
        my.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MeActivity.this, MyActivityListActivity.class));
            }
        });

        join = findViewById(R.id.joined_Activities);
        join.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MeActivity.this, JoinedActivity.class));
            }
        });

        logout = findViewById(R.id.logout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(MeActivity.this, LoginActivity.class));
                finish();
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