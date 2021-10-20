package com.comp90018.JoinMe;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

public class ImageShowing extends AppCompatActivity {

    ImageView image;
    String uri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_showing);
        image=findViewById(R.id.image);

        uri=getIntent().getStringExtra("image");
        Picasso.get().load(uri).into(image);
    }
}