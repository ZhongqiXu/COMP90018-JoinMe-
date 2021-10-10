package com.comp90018.JoinMe;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import static androidx.constraintlayout.widget.ConstraintLayoutStates.TAG;

public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    EditText email,pwds;
    TextView forget,toRegister;
    Button login;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();
        email =findViewById(R.id.login_Email);
        pwds=findViewById(R.id.login_pwd);
        login=findViewById(R.id.btn_login);
        forget=findViewById(R.id.btn_fotget);
        toRegister=findViewById(R.id.btn_ToRegister);


        toRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this,RegisterActivity.class));
            }
        });

        forget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this,reSetActivity.class));
            }
        });


        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println(email.getText().toString());
                if (TextUtils.isEmpty(email.getText().toString()))
                    Toast.makeText(LoginActivity.this,"Please enter email.",Toast.LENGTH_SHORT).show();
                else if (TextUtils.isEmpty(pwds.getText().toString()))
                    Toast.makeText(LoginActivity.this,"Please enter password.",Toast.LENGTH_SHORT).show();
                else {
                    loginEvent();
                }
            }
        });

    }


    public void loginEvent(){
        mAuth.signInWithEmailAndPassword(email.getText().toString(), pwds.getText().toString())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            //Log.d(TAG, "signInWithEmail:success");

                            FirebaseUser user = mAuth.getCurrentUser();


                            Toast.makeText(LoginActivity.this, "Authentication success.",
                                    Toast.LENGTH_SHORT).show();
                            Intent intent= new Intent(LoginActivity.this,MeActivity.class);
                            intent.putExtra("uid",user.getUid());
                            startActivity(intent);

                        } else {
                            // If sign in fails, display a message to the user.
                            //Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(LoginActivity.this, task.getException().getMessage(),
                                    Toast.LENGTH_SHORT).show();

                        }
                    }
                });
    }
}