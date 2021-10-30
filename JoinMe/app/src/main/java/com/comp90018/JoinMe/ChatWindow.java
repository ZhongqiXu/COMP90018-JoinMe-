package com.comp90018.JoinMe;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.UUID;

import object.Message;
import helper.MessageAdapter;

public class ChatWindow extends AppCompatActivity {
    EditText getMsg;
    CardView sendMsgCardV;
    ImageButton sendBtn, picBtn, cameraBtn;

    RecyclerView mmesagerecyclerview;
    ArrayList<Message> messageArrayList;
    MessageAdapter messageAdapter;

    Intent intent;
    private FirebaseAuth firebaseAuth;
    FirebaseDatabase firebaseDatabase;
    FirebaseFirestore firebaseFirestore;
    private StorageReference storageReference;
    FirebaseStorage firebaseStorage;

    Calendar calender;
    SimpleDateFormat simpleDateFormat;
    String mreceivername, mreceiveruid, msenderuid, senderroom, receiverroom, enteredmessage, currenttime, ImageUriAcessToken;

    private Uri imagepath;
    ActivityResultLauncher<Intent> startForResult = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult result) {
            if(result != null && result.getResultCode() == RESULT_OK){
                if(result.getData() != null){
                    imagepath = result.getData().getData();
                    Bitmap bitmap=null;
                    try{
                        bitmap=MediaStore.Images.Media.getBitmap(getContentResolver(), imagepath);
                    }catch (IOException e){
                        e.printStackTrace();
                    }
                    sendImageToStorage(bitmap);
                }
            }
        }
    });

    ActivityResultLauncher<Intent> startForResultCamera = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult result) {
            if(result.getResultCode() == RESULT_OK && result.getData() != null){
                Bundle bundle = result.getData().getExtras();
                Bitmap bitmap = (Bitmap) bundle.get("data");
                sendImageToStorage(bitmap);
            }
        }
    });


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_window);

        getMsg =findViewById(R.id.getmessage);
        sendMsgCardV =findViewById(R.id.cardviewofsendmessage);
        sendBtn =findViewById(R.id.imageviewsendmessage);
        picBtn = findViewById(R.id.picbtn);
        cameraBtn = findViewById(R.id.cameraBtn);

        messageArrayList =new ArrayList<>();
        mmesagerecyclerview = findViewById(R.id.recyclerviewofspecific);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setStackFromEnd(true);
        mmesagerecyclerview.setLayoutManager(linearLayoutManager);
        messageAdapter =new MessageAdapter(ChatWindow.this, messageArrayList);
        mmesagerecyclerview.setAdapter(messageAdapter);

        intent=getIntent();

        firebaseFirestore= FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();
        storageReference=firebaseStorage.getReference();
        calender= Calendar.getInstance();
        simpleDateFormat=new SimpleDateFormat("hh:mm a");

        msenderuid=firebaseAuth.getUid();
        mreceiveruid=getIntent().getStringExtra("receiveruid");
        mreceivername=getIntent().getStringExtra("name");
        senderroom=msenderuid+mreceiveruid;
        receiverroom=mreceiveruid+msenderuid;

        DatabaseReference databaseReference=firebaseDatabase.getReference().child("chats").child(senderroom);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                messageArrayList.clear();
                for (DataSnapshot snapshot1:snapshot.getChildren()){
                    Message message = snapshot1.getValue(Message.class);
                    messageArrayList.add(message);
                }
                messageAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) { }
        });

        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enteredmessage= getMsg.getText().toString();
                if (!enteredmessage.isEmpty()){
                    int time = (int)(calender.getTimeInMillis()/1000);
                    DocumentReference documentReference=firebaseFirestore.collection("Contact").document(senderroom);
                    DocumentReference documentReference2=firebaseFirestore.collection("Contact").document(receiverroom);
                    documentReference.update("time", time);
                    documentReference2.update("time", time);

//                    Date date = new Date();
                    currenttime=simpleDateFormat.format(calender.getTime());
                    Message message = new Message(enteredmessage, firebaseAuth.getUid(), time, currenttime, "String");
                    firebaseDatabase=FirebaseDatabase.getInstance();
                    firebaseDatabase.getReference().child("chats").child(senderroom).push().setValue(message).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            firebaseDatabase.getReference().child("chats").child(receiverroom).push().setValue(message).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    Toast.makeText(getApplicationContext(), "Sent ", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    });

                    getMsg.setText(null);

                }
            }
        });

        cameraBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(ChatWindow.this,
                        Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
                    ActivityCompat.requestPermissions(ChatWindow.this,
                            new String[]{
                                    Manifest.permission.CAMERA
                            },100);
                }

                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startForResultCamera.launch(intent);
            }
        });

        picBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
                startForResult.launch(intent);
            }
        });
    }

    private void sendImageToStorage(Bitmap bitmap){
        UUID uuid = UUID.randomUUID();
        StorageReference imageref=storageReference.child("images").child(senderroom).child(uuid.toString());

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
                        sendImageDatabase();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(), "URI get Failed", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(), "Image Not Uploaded", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void sendImageDatabase(){
        int time = (int)(calender.getTimeInMillis()/1000);
        DocumentReference documentReference=firebaseFirestore.collection("Contact").document(senderroom);
        DocumentReference documentReference2=firebaseFirestore.collection("Contact").document(receiverroom);
        documentReference.update("time", time);
        documentReference2.update("time", time);

        currenttime=simpleDateFormat.format(calender.getTime());
        Message message = new Message(ImageUriAcessToken, firebaseAuth.getUid(), time, currenttime, "Image");
        firebaseDatabase=FirebaseDatabase.getInstance();
        firebaseDatabase.getReference().child("chats").child(senderroom).push().setValue(message).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                firebaseDatabase.getReference().child("chats").child(receiverroom).push().setValue(message).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(getApplicationContext(), "Sent ", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }



    @Override
    public void onStart() {
        super.onStart();
        messageAdapter.notifyDataSetChanged();
    }

    @Override
    public void onStop() {
        super.onStop();
        if(messageAdapter != null){
            messageAdapter.notifyDataSetChanged();
        }
    }
}