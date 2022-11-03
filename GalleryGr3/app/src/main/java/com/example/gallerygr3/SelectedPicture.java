package com.example.gallerygr3;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;

import android.os.Bundle;

import android.media.MediaPlayer;

import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class SelectedPicture extends AppCompatActivity implements ISelectedPicture {

    ViewPager2 viewPager2;
    ArrayList<viewPagerItem> listItem;
    String[] paths;
    String[] dates;
    int[] size;
    ArrayList<String> imagesPath;
    ArrayList<String> imagesDate;
    ArrayList<Integer> imagesSize;
    MediaPlayer mediaPlayer;

    ImageButton backBtn;
    ImageButton deleteBtn;
    ImageButton infoBtn;
    String currentSelectedName;
    int currentPosition;
    MainActivity main;

    RelativeLayout topNav;
    RelativeLayout bottomNav;

    boolean displayNavBars = true;
    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selected_picture);

        viewPager2=(ViewPager2)findViewById(R.id.main_viewPager) ;

        //xử lí nút back toàn màn hình
        backBtn=(ImageButton) findViewById(R.id.backButton);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SelectedPicture.super.onBackPressed();
            }
        });

        deleteBtn=(ImageButton) findViewById(R.id.deleteSingleBtn);
        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showCustomDialogBoxInSelectedPicture();
            }
        });

        infoBtn = (ImageButton)findViewById(R.id.infoBtn) ;
        infoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showCustomDialogBoxInformation();
            }
        });

        topNav = (RelativeLayout) findViewById(R.id.topNavSinglePic);
        bottomNav = (RelativeLayout) findViewById(R.id.bottomNavSinglePic);
        //get img and name data
        Intent intent = getIntent();
        if(intent.getExtras()!=null){

            //cut name
            imagesPath = intent.getStringArrayListExtra("images");
            imagesDate = intent.getStringArrayListExtra("dates");
            imagesSize = intent.getIntegerArrayListExtra("size");
            int pos = intent.getIntExtra("pos",0);


            paths = new String[imagesPath.size()];
            for(int i = 0; i< imagesPath.size(); i++){
                paths[i]= imagesPath.get(i);
            }

            dates= new String[imagesDate.size()];
            for(int i = 0; i< imagesDate.size(); i++){
                dates[i]=imagesDate.get(i);
            }

            size= new int[imagesSize.size()];
            for(int i = 0; i< imagesSize.size(); i++){
                size[i]=imagesSize.get(i);
            }


            listItem=new  ArrayList<viewPagerItem> ();
            for(int i = 0; i< imagesPath.size(); i++){
                viewPagerItem item = new viewPagerItem(paths[i]);
                listItem.add(item);
            }

            viewPagerAdapter aa=new viewPagerAdapter(listItem,this);

            viewPager2.setAdapter(aa);
            viewPager2.setCurrentItem(pos,false);
            viewPager2.setClipToPadding(false);
            viewPager2.setClipChildren(false);
            viewPager2.setOffscreenPageLimit(2);
            viewPager2.getChildAt(0).setOverScrollMode(View.OVER_SCROLL_NEVER);
//            viewPager2.playSoundEffect(R.raw.musicc);
//            viewPager2.playSoundEffect();
//            mediaPlayer= MediaPlayer.create(getApplicationContext(),R.raw.musicc);
//            mediaPlayer.start();

            viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                    super.onPageScrolled(position, positionOffset, positionOffsetPixels);
                    String temp=aa.getItem(position).getSelectedName();
                    setCurrentSelectedName(aa.getItem(position).getSelectedName());
                    setCurrentPosition(position);

                    aa.BackToInit();

                }
            });
        }
    }

    @Override
    public void preventSwipe() {
        viewPager2.setUserInputEnabled(false);
        return;
    }

    @Override
    public void allowSwipe() {
        viewPager2.setUserInputEnabled(true);
        return;
    }

    @Override
    public void setCurrentSelectedName(String name){
        this.currentSelectedName = name;
    }
    @Override
    public void setCurrentPosition(int pos){
        this.currentPosition = pos;
    }
    @Override
    public void removeImageUpdate(String input){
        //xoas trong selectedImage
        deleteArrayByPossision(paths, currentPosition);
        deleteArrayByPossision(dates, currentPosition);
//        deleteArrayByPossision(size, currentPosition);
        //xóa trong adepter
        listItem.remove(currentPosition);
        viewPagerAdapter aa=new viewPagerAdapter(listItem,this);
        viewPager2.setAdapter(aa);
        viewPager2.setCurrentItem(currentPosition ,false);
    }

    @Override
    public void showNav() {

        if(!displayNavBars){
            topNav.setVisibility(View.VISIBLE);
            bottomNav.setVisibility(View.VISIBLE);
            displayNavBars = true;
        }else {
            displayNavBars = false;
            bottomNav.setVisibility(View.INVISIBLE);
            topNav.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void hiddenNav() {
        return;
    }

    private void showCustomDialogBoxInSelectedPicture()
    {
        final Dialog customDialog = new Dialog( this );
        customDialog.setTitle("Delete confirm");

        customDialog.setContentView(R.layout.delete_dialog_notify);

        ((TextView) customDialog.findViewById(R.id.deleteNotify))
                .setText("Do you want to delete in your device ?");

        ((Button) customDialog.findViewById(R.id.cancelDelete))
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //donothing
                        customDialog.dismiss();
                    }
                });

        ((Button) customDialog.findViewById(R.id.confirmDelete))
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        ImageDisplay ic= ImageDisplay.newInstance();
                        ImageDelete.DeleteImage(currentSelectedName);
                        removeImageUpdate(currentSelectedName);
                        //cập nhật lại danh sách trong ImageDisplay
                        ic.deleteClicked(currentSelectedName);
                        customDialog.dismiss();
                    }
                });
        customDialog.show();
    }


    private void showCustomDialogBoxInformation()
    {
        final Dialog customDialog = new Dialog( this );
        customDialog.setTitle("Information of Picture");

        customDialog.setContentView(R.layout.infomation_picture_dialog);
//
        ((TextView) customDialog.findViewById(R.id.photoName))
                .setText(ImageDisplay.getDisplayName(paths[currentPosition]));
        ((TextView) customDialog.findViewById(R.id.photoPath))
                .setText(paths[currentPosition]);
        ((TextView) customDialog.findViewById(R.id.photoLastModified))
                .setText(dates[currentPosition]);
        ((TextView) customDialog.findViewById(R.id.photoSize))
                .setText(size[currentPosition]+" bytes");
//        Toast.makeText(this, imagesSize[currentPosition]+"", Toast.LENGTH_SHORT).show();
        ((Button) customDialog.findViewById(R.id.ok_button))
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //donothing
                        customDialog.dismiss();
                    }
                });

        customDialog.show();
    }
    public void deleteArrayByPossision(String[]arr, int pos){
        int size = arr.length;
        if(pos != arr.length - 1 ){
            for(int i=pos; i < size - 1; i++){
                arr[pos] = arr[pos+1];
            }
        }
    }
}