package com.example.gallerygr3;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;

public class SelectedPicture extends AppCompatActivity {

    ViewPager2 viewPager2;
    ArrayList<viewPagerItem> listItem;
    String[] names;
    ArrayList<String> images;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selected_picture);

        viewPager2=(ViewPager2)findViewById(R.id.main_viewPager) ;

        //get img and name data


        Intent intent = getIntent();
        if(intent.getExtras()!=null){

            //cut name
            String selectedName = intent.getStringExtra("name");
            ArrayList<String> images = intent.getStringArrayListExtra("images");

            names= new String[images.size()];
            // fix name from data
            for(int i=0;i<images.size();i++){
                names[i]=images.get(i);
            }

            listItem=new  ArrayList<viewPagerItem> ();
            for(int i=0;i<images.size();i++){
                viewPagerItem item = new viewPagerItem(names[i]);
                listItem.add(item);
            }

            viewPagerAdapter aa=new viewPagerAdapter(listItem);

            viewPager2.setAdapter(aa);
            viewPager2.setClipToPadding(false);
            viewPager2.setClipChildren(false);
            viewPager2.setOffscreenPageLimit(2);
            viewPager2.getChildAt(0).setOverScrollMode(View.OVER_SCROLL_NEVER);

        }

    }
}