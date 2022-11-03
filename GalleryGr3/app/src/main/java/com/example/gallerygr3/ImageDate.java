package com.example.gallerygr3;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class ImageDate implements Comparable<ImageDate>{
    private String image;
    private Date date;

    public ImageDate(String image, Date date) {
        this.image = image;
        this.date = date;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String dayToString(){
        DateFormat dateFormat;
        dateFormat = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");
        return dateFormat.format(this.date);
    }
    @Override
    public int compareTo(ImageDate imageDate) {
        return this.date.compareTo(imageDate.date);
    }


}
