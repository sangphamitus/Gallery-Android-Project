package com.example.gallerygr3;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;

public class SelectedPicture extends AppCompatActivity {
    ImageView imageView;
    TextView textView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selected_picture);

        imageView = findViewById(R.id.imageView);
        textView =findViewById(R.id.tvName);

        Intent intent = getIntent();

        if(intent.getExtras()!=null){
            String selectedName = intent.getStringExtra("name");

//            int selectedImage = intent.getIntExtra("image", 0);

            int getPositionFolderName= selectedName.lastIndexOf("/");
            String name= selectedName.substring(getPositionFolderName + 1);

            textView.setText(name);

            File imgFile= new File(selectedName);
            Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
//            imageView.setImageResource(selectedImage);
            imageView.setImageBitmap(myBitmap);

        }
    }
}