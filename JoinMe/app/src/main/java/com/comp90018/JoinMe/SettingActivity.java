package com.comp90018.JoinMe;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

import object.User;

public class SettingActivity extends AppCompatActivity implements NavigationBarView.OnItemSelectedListener {

    FirebaseAuth mAuth;
    TextView set_name,set_gender,set_age,set_brief;
    Button set_btn_submit;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    NavigationBarView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        set_age=findViewById(R.id.setting_age);
        set_name=findViewById(R.id.setting_name);
        set_gender=findViewById(R.id.setting_gender);
        set_brief = findViewById(R.id.setting_brief);
        set_btn_submit = findViewById(R.id.setting_submit);

        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnItemSelectedListener(this);
        bottomNavigationView.getMenu().findItem(R.id.settings).setChecked(true);

        mAuth = FirebaseAuth.getInstance();
        final String uid = mAuth.getUid();

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference().child("user").child(uid);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);
                System.out.println(user);
                bind(user);
                Toast.makeText(SettingActivity.this,"seucc",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(SettingActivity.this,error.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });

        set_btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HashMap updateUser = update(set_name.getText().toString(),set_age.getText().toString(),
                        set_gender.getText().toString(), set_brief.getText().toString());

                databaseReference.updateChildren(updateUser).addOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if (task.isSuccessful()){
                            Toast.makeText(SettingActivity.this,"xixiix",Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(SettingActivity.this,MeActivity.class));
                            finish();
                        }
                        else
                            Toast.makeText(SettingActivity.this,task.getException().getMessage(),
                                    Toast.LENGTH_SHORT).show();

                    }
                });
            }
        });

    }


    public void bind(User user){
        set_name.setText(user.getUserName());
        set_gender.setText(user.getGender());
        set_brief.setText(user.getBrief());
        set_age.setText(user.getAge());
    }

    public HashMap update(String name,String age,String gender,String brief){

        HashMap user = new HashMap();
        user.put("userName",name);
        user.put("age",age);
        user.put("gender",gender);
        user.put("brief",brief);

        return user;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        bottomNavigationView.getMenu().findItem(item.getItemId()).setChecked(true);
        switch (item.getItemId()) {
            case R.id.profile:
                startActivity(new Intent(this, MeActivity.class));
                break;
            case R.id.activities:
                startActivity(new Intent(this, MainActivity.class));
                break;
            default:
                break;
        }
        return false;
    }
}