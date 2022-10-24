package com.example.gallerygr3;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.File;

public class viewPagerItem {
    String selectedName;
    Bitmap itemBitmap;
    public viewPagerItem(String selectedName) {
        this.selectedName = selectedName;

    }

    public String getSelectedName() {
        return selectedName;
    }

    public Bitmap getItemBitmap() {
        File imgFile= new File(selectedName);
        itemBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
        return itemBitmap;
    }
}
