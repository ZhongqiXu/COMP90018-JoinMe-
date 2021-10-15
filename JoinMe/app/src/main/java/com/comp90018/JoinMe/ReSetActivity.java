package com.comp90018.JoinMe;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ReSetActivity extends AppCompatActivity {

    Button  button;
    TextView re_email;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_re_set);

        button=findViewById(R.id.btn_reset);
        re_email=findViewById(R.id.reset_Email);



        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email = re_email.getText().toString();
                System.out.println("email is "+email);

                if (TextUtils.isEmpty(email))
                    Toast.makeText(ReSetActivity.this,"Please enter email.",Toast.LENGTH_SHORT).show();
                else {
                    FirebaseAuth.getInstance().sendPasswordResetEmail(email).
                            addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()){
                                        Toast.makeText(ReSetActivity.this,"success",Toast.LENGTH_SHORT);
                                        startActivity(new Intent(ReSetActivity.this,LoginActivity.class));

                                    }
                                    else
                                        Toast.makeText(ReSetActivity.this,"fails",Toast.LENGTH_SHORT);
                                }
                            });
                }
            }
        });
    }
}