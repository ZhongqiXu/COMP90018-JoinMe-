package com.comp90018.JoinMe;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.HashMap;

public class MeActivity extends AppCompatActivity {


    TextView uid,userName,welname;
    FirebaseAuth mAuth;
    DatabaseReference base;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        mAuth = FirebaseAuth.getInstance();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_me);

        welname = findViewById(R.id.wel_name);

        String key = getIntent().getStringExtra("uid");

        welname.setText(key);

        System.out.println(key);
        System.out.println("sfsfsfsfs");

        String uid = mAuth.getCurrentUser().getUid();
        System.out.println(uid);

        System.out.println(uid.equals("key"));

        // 查询demo
        base = FirebaseDatabase.getInstance().getReference("user");

        Query query = base.equalTo(uid);
        System.out.println(query);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.me_menu,menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.edit:
                Toast.makeText(this, "点击了第一个菜单", Toast.LENGTH_SHORT).show();
                break;
            case R.id.logout:
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(MeActivity.this,LoginActivity.class));
                finish();
                break;
            default:
                break;
        }
        return true;

    }


}