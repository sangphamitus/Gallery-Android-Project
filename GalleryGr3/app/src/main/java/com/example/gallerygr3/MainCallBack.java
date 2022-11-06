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

    public ArrayList<String> getFileinDir();
    public void removeImageUpdate(String[] input);
    public void removeImageUpdate(String input);

    public void renameImageUpdate(String oldNam, String newName);


    public void Holding(boolean isHolding);
    public void SelectedTextChange();
    public ArrayList<String> chooseToDeleteInList();
    public ArrayList<String> adjustChooseToDeleteInList(String ListInp,String type );
    public void clearChooseToDeleteInList();
    public void addImageUpdate(String[] input);


}
