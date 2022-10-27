package com.example.gallerygr3;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public final class ImageDelete {
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
}
