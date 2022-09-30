package com.example.gallerygr3;

import java.util.ArrayList;

public interface MainCallBack {
    public void setCurrentDirectory(String Dir);

    public String getSDDirectory();
    public String getCurrentDirectory();
    public void pushFolderPath (String inp );
    public void popFolderPath();
    public ArrayList<String> getFolderPath();
    public String getDCIMDirectory();
    public String getPictureDirectory();



}
