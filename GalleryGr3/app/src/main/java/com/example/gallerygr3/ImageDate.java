package com.example.gallerygr3;

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

    @Override
    public int compareTo(ImageDate imageDate) {
        return this.date.compareTo(imageDate.date);
    }

}
