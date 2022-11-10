package com.example.gallerygr3;

import android.graphics.Bitmap;
import android.view.View;

public interface ZoomCallBack {
    public void BackToInit();
    public Bitmap RotateDegree(String currentImg,float degree,int pos);
    public void setImageView(String currentImg,int pos);
}
