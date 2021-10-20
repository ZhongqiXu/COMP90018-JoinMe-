package com.comp90018.JoinMe;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import object.User;

public class SettingActivity extends AppCompatActivity implements NavigationBarView.OnItemSelectedListener {

    FirebaseAuth mAuth;
    TextView set_name,set_gender,set_age,set_brief;
    Button set_btn_submit;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    NavigationBarView bottomNavigationView;

    //added by chatting
    private CardView set_image;
    private ImageView getuserimageinimageview;
    private Uri imagepath;
    private FirebaseStorage firebaseStorage;
    private StorageReference storageReference;
    private String ImageUriAcessToken;
    private FirebaseFirestore firebaseFirestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        set_age=findViewById(R.id.setting_age);
        set_name=findViewById(R.id.setting_name);
        set_gender=findViewById(R.id.setting_gender);
        set_brief = findViewById(R.id.setting_brief);
        set_btn_submit = findViewById(R.id.setting_submit);
        //added by chatting
        set_image=findViewById(R.id.getuserimage);
        getuserimageinimageview=findViewById(R.id.getuserimageinimageview);

        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnItemSelectedListener(this);
        bottomNavigationView.getMenu().findItem(R.id.settings).setChecked(true);

        mAuth = FirebaseAuth.getInstance();
        final String uid = mAuth.getUid();
        //added by chatting
        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference();
        firebaseFirestore=FirebaseFirestore.getInstance();


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
                //added by chatting
                if(imagepath!=null){
                    sendImagetoStorage();
                }
            }
        });

        //added by chatting
        ActivityResultLauncher<Intent> startForResult = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                if(result != null && result.getResultCode() == RESULT_OK){
                    if(result.getData() != null){
                        imagepath = result.getData().getData();
                        getuserimageinimageview.setImageURI(imagepath);
                    }

                }
            }
        });

        DocumentReference documentReference = firebaseFirestore.collection("User").document(mAuth.getUid());
        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        String uri = (String) document.getString("image");
                        Picasso.get().load(uri).into(getuserimageinimageview);
                    }
                }
            }
        });

        set_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
                startForResult.launch(intent);
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

    //added by chatting
    private void sendImagetoStorage(){
        StorageReference imageref=storageReference.child("images").child(mAuth.getUid()).child("Profile Pic");
        Bitmap bitmap=null;
        try{
            bitmap=MediaStore.Images.Media.getBitmap(getContentResolver(), imagepath);
        }catch (IOException e){
            e.printStackTrace();
        }

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 25, byteArrayOutputStream);
        byte[] data=byteArrayOutputStream.toByteArray();

        UploadTask uploadTask=imageref.putBytes(data);

        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                imageref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        ImageUriAcessToken = uri.toString();
                        sendDataTocloudFirestore();
                    }
                });
            }
        });
    }

    private void sendDataTocloudFirestore() {

        DocumentReference documentReference=firebaseFirestore.collection("User").document(mAuth.getUid());
        Map<String, Object> userdata = new HashMap<>();
        userdata.put("name", set_name.getText().toString());
        userdata.put("image", ImageUriAcessToken);
        userdata.put("uid", mAuth.getUid());
        documentReference.set(userdata);

        CollectionReference query = firebaseFirestore.collection("Contact");
        query.whereEqualTo("uid_contacter", mAuth.getUid()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Map<Object, String> map = new HashMap<>();
                        map.put("image", ImageUriAcessToken);
                        query.document(document.getId()).set(map, SetOptions.merge());
                    }
                }
            }
        });

    }
}