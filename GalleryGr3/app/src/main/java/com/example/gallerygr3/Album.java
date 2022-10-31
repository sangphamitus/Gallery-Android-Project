package com.example.gallerygr3;

import java.io.Serializable;
import java.util.ArrayList;

public class Album{
    String name;
    ArrayList<String> imagePaths;
    public Album(String name,ArrayList<String> imagePaths) {
        this.name=name;
        this.imagePaths=imagePaths;
    }
}
