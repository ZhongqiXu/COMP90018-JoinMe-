package com.comp90018.JoinMe;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
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
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

import object.User;

public class RegisterActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    EditText Email,pwds,userName;
    TextView toLogin;
    Button register;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();
        Email=findViewById(R.id.regst_Email);
        pwds=findViewById(R.id.regst_pwd);
        userName=findViewById(R.id.regst_userName);
        toLogin=findViewById(R.id.btn_ToLogin);
        register=findViewById(R.id.btn_register);

        toLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RegisterActivity.this,LoginActivity.class));
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(Email.getText().toString()))
                    Toast.makeText(RegisterActivity.this,"Please enter email.",Toast.LENGTH_SHORT).show();
                else if (TextUtils.isEmpty(pwds.getText().toString()))
                    Toast.makeText(RegisterActivity.this,"Please enter password.",Toast.LENGTH_SHORT).show();
                else if (TextUtils.isEmpty(userName.getText().toString()))
                    Toast.makeText(RegisterActivity.this,"Please enter username.",Toast.LENGTH_SHORT).show();
                else
                    loginEvent(Email.getText().toString(), pwds.getText().toString(), userName.getText().toString());
            }
        });
    }

    public void loginEvent(String email,String password,String name){
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            //Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser fireUser = mAuth.getCurrentUser();

                            final String uid = fireUser.getUid();
                            User user=new User();
                            user.setUid(uid);
                            user.setEmail(email);
                            user.setUserName(name);
                            System.out.println(uid);

                            String defaultImage = "https://firebasestorage.googleapis.com/v0/b/joinme-37c01.appspot.com/o/images%2Fdefault%2Fdefaultprofile.png?alt=media&token=a9444751-2027-4908-abe2-7e78b88cd21e";                            FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
                            DocumentReference documentReference=firebaseFirestore.collection("User").document(mAuth.getUid());
                            Map<String, Object> userdata = new HashMap<>();
                            userdata.put("name", userName.getText().toString());
                            userdata.put("image", defaultImage);
                            userdata.put("uid", uid);
                            documentReference.set(userdata);

                            FirebaseDatabase.getInstance().getReference().child("user").child(uid).setValue(user);
                            Toast.makeText(RegisterActivity.this, "Authentication success.",
                                    Toast.LENGTH_SHORT).show();

                            Intent intent = new Intent(RegisterActivity.this,MainActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            // If sign in fails, display a message to the user.
                            //Log.w(TAG, "createUserWithEmail:failure", task.getException().getMessage());
                            Toast.makeText(RegisterActivity.this, "Authentication failed."+task.getException().getMessage(),
                                    Toast.LENGTH_SHORT).show();
                            System.out.println("注册失败");

                        }
                    }
                });
    }
}