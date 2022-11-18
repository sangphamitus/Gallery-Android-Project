package com.example.gallerygr3;

import android.app.Activity;
import android.graphics.Bitmap;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

public final class ImageDelete extends Activity {
    public static boolean DeleteImage(String[] ListImage){
        boolean running=true;
        for (String file : ListImage)
        {
            File fdel=new File(file);
            if(fdel.exists()){

                try
                {
                    fdel.delete();
                }
                catch (Error e)
                {
                    running =false;
                    return running;
                }
            }
        }
        return running;
    }


    //xử lí single image
    public static boolean DeleteImage(String image){
        boolean running=true;
        File fdel=new File(image);
        if(fdel.exists()){

            try
            {
                fdel.delete();
            }
            catch (Error e)
            {
                running =false;
                return running;
            }
        }
        return running;
    }
    public static String saveImage(Bitmap finalBitmap, String imagePath) {

        File myFile = new File(imagePath);

        int i= 0;
        String[] delim=imagePath.split("\\.");
        String temp=delim[0];
        while( myFile.exists())
        {
            temp+="_"+i;
            i++;
            myFile = new File(temp+"."+delim[1]);
            temp=delim[0];
        }
        try {
            FileOutputStream out = new FileOutputStream(myFile);
            finalBitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.flush();
            out.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return temp;
    }

}
