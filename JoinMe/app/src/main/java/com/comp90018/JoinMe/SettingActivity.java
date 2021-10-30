package com.comp90018.JoinMe;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
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

import helper.HorizontalNumberPicker;
import object.User;

public class SettingActivity extends AppCompatActivity {

    FirebaseAuth mAuth;
    TextView set_name,set_brief;
    Button set_btn_submit;
    RadioGroup genderSet;
    RadioButton btn_gender,btn_male,btn_female;
    private HorizontalNumberPicker age;
    ImageView camera;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

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

        age=findViewById(R.id.setting_age);
        set_name=findViewById(R.id.setting_name);

        genderSet=findViewById(R.id.radioSex);
        btn_male = findViewById(R.id.radioMale);
        btn_female = findViewById(R.id.radioFemale);

        set_brief = findViewById(R.id.setting_brief);
        set_btn_submit = findViewById(R.id.setting_submit);
        //added by chatting
        set_image=findViewById(R.id.getuserimage);
        getuserimageinimageview=findViewById(R.id.getuserimageinimageview);
        //camera
        camera=findViewById(R.id.camera);

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

                int selectId=genderSet.getCheckedRadioButtonId();
                btn_gender = (RadioButton) findViewById(selectId);

                HashMap updateUser = update(set_name.getText().toString(),String.valueOf(age.getValue()),
                        btn_gender.getText().toString(), set_brief.getText().toString());

                databaseReference.updateChildren(updateUser).addOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if (task.isSuccessful()){

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


        if (ContextCompat.checkSelfPermission(SettingActivity.this,
                Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(SettingActivity.this,
                    new String[]{
                            Manifest.permission.CAMERA
                    },100);
        }

        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // open camera
                Intent intent=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent,100);
            }
        });


    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100) {
            Bitmap captureImage = (Bitmap) data.getExtras().get("data");

            getuserimageinimageview.setImageBitmap(captureImage);
        }
    }




    public void bind(User user){
        set_name.setText(user.getUserName());

        if (user.getGender()==null){
            btn_female.setChecked(false);
            btn_male.setChecked(false);
        }
        else if (user.getGender().equals("Male")){
            btn_male.setChecked(true);
        }else if (user.getGender().equals("Female"))
            btn_female.setChecked(true);


        if (user.getBrief()==null){
            set_brief.setText("");
        }else
            set_brief.setText(user.getBrief());

        if (user.getAge()==null)
            age.setValue(0);
        else
            age.setValue(Integer.parseInt(user.getAge()));
    }

    public HashMap update(String name,String age,String gender,String brief){

        HashMap user = new HashMap();
        user.put("userName",name);
        user.put("age",age);
        user.put("gender",gender);
        user.put("brief",brief);

        return user;
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