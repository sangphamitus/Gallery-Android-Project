package com.example.gallerygr3;

public interface ISelectedPicture {
    public void preventSwipe();
    public void allowSwipe();

    public void setCurrentSelectedName(String name);
    public void setCurrentPosition(int pos);

    public void removeImageUpdate(String input);
}
