package com.example.gallerygr3;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.io.File;

public class viewPagerItem {
    String selectedName;
    Bitmap itemBitmap;
    File img;
    public viewPagerItem(String selectedName) {
        this.selectedName = selectedName;
        img= new File(selectedName);

//        ImageLoader.getInstance().
//                displayImage(String.valueOf(Uri.parse("file://"+imgFile.getAbsolutePath().toString())),viewHolder.imageView);


        itemBitmap = BitmapFactory.decodeFile(img.getAbsolutePath());
    }

    public String getSelectedName() {
        return selectedName;
    }

    public Bitmap getItemBitmap() {
        return itemBitmap;
    }


    public File getimgFile() {
        return img;
    }
}
