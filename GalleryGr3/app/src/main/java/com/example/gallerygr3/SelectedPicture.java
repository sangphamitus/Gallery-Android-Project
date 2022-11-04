package com.example.gallerygr3;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import android.graphics.Color;
import android.graphics.ColorSpace;
import android.os.Bundle;
import android.view.MotionEvent;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.provider.MediaStore;

import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;

public class SelectedPicture extends AppCompatActivity implements ISelectedPicture {


    ViewPager2 viewPager2;
    ArrayList<viewPagerItem> listItem;
    String[] names;
    ArrayList<String> images;
    MediaPlayer mediaPlayer;

    ImageButton backBtn;
    ImageButton deleteBtn,editBtn;
    public ImageButton rotateBtn;

    String currentSelectedName=null;
    int currentPosition=-1;
    MainActivity main;
    viewPagerAdapter aa =null;

    Boolean haveRotate=false;
    RelativeLayout topNav;
    RelativeLayout bottomNav;

    Bitmap rotateImage=null;
    String imageRotated=null;






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

        editBtn=(ImageButton) findViewById(R.id.editBtn);
       editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "edit", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(getApplicationContext(), EditImage.class);
                intent.putExtra("imgPath", currentSelectedName);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                someActivityResultLauncher.launch(intent);


            }
        });
        rotateBtn=(ImageButton) findViewById(R.id.rotateBtn);
        rotateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                viewPagerItem out= aa.getItem(currentPosition);
//
//                File imgFile= new File(currentSelectedName);
//                Bitmap imageShoot= BitmapFactory.decodeFile(imgFile.getAbsolutePath());
//                imageShoot=ImageUltility.rotateImage(imageShoot,90);
//                aa.SetBitmap(imageShoot,currentPosition);
                Toast.makeText(getApplicationContext(), ""+currentPosition, Toast.LENGTH_SHORT).show();
                haveRotate=true;
                imageRotated=currentSelectedName;
                rotateImage=aa.RotateDegree(currentSelectedName,90,currentPosition);
            }
        });

        topNav = (RelativeLayout) findViewById(R.id.topNavSinglePic);
        bottomNav = (RelativeLayout) findViewById(R.id.bottomNavSinglePic);

        //PHẦN NÀY ĐỂ ẨN HIỆN TASK BAR TRONG SINGLE IMG - CHUA LAM DUOC

//        viewPager2.getChildAt(viewPager2.getCurrentItem()).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
////                if (displayNavBars ==false){
//////                    topNav.setVisibility(View.VISIBLE);
//////                    topNav.setVisibility(View.VISIBLE);
////                    topNav.setBackgroundColor(Color.RED);
////                    displayNavBars = true;
////
////                }
////                else if(displayNavBars ==true){
////                    displayNavBars = false;
////                    topNav.setVisibility(View.INVISIBLE);
////                    topNav.setVisibility(View.INVISIBLE);
////                }
//                Toast.makeText(getApplicationContext(), "123", Toast.LENGTH_SHORT).show();
//            }
//        });
        //get img and name data
        Intent intent = getIntent();
        if(intent.getExtras()!=null){

            //cut name
            String selectedName = intent.getStringExtra("name");
            ArrayList<String> images = intent.getStringArrayListExtra("images");
            int pos = intent.getIntExtra("pos",0);

            names= new String[images.size()];
            // fix name from data
            for(int i=0;i<images.size();i++){
                names[i]=images.get(i);
            }

            listItem=new  ArrayList<viewPagerItem> ();
            for(int i=0;i<images.size();i++){
                viewPagerItem item = new viewPagerItem(names[i]);
                listItem.add(item);
            }
            if(aa==null)
            aa=new viewPagerAdapter(listItem,this);

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

                    if(haveRotate && position!=currentPosition)
                    {
                        showCustomDialogBoxInRotatePicture(rotateImage,imageRotated);
                      aa.RotateDegree(currentSelectedName,0,currentPosition);

                    }
                    String temp=aa.getItem(position).getSelectedName();
                    setCurrentSelectedName(aa.getItem(position).getSelectedName());
                    setCurrentPosition(position);

                    aa.BackToInit();

                }
            });
        }
    }
    ActivityResultLauncher<Intent> someActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        // There are no request codes

                        aa.notifyDataSetChanged();
                    }
                }
            });
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
        listItem.remove(currentPosition);
//         aa=new viewPagerAdapter(listItem,this);
//        viewPager2.setAdapter(aa);
//
        viewPager2.setCurrentItem(currentPosition ,false);
        aa.notifyDataSetChanged();

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
//        displayNavBars = false;
//        topNav.setVisibility(View.INVISIBLE);
//        topNav.setVisibility(View.INVISIBLE);

        return;
    }

    @Override
    public void notifyChanged() {
        aa.notifyDataSetChanged();
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
    private void showCustomDialogBoxInRotatePicture(Bitmap rotateImage2,String imageRotated2)
    {
        final Dialog customDialog = new Dialog( this );
        customDialog.setTitle("Change confirm");

        customDialog.setContentView(R.layout.delete_dialog_notify);

        ((TextView) customDialog.findViewById(R.id.deleteNotify))
                .setText("Do you want to save your change ?");
        ((TextView) customDialog.findViewById(R.id.titleBox))
                .setText("Change");

        ((Button) customDialog.findViewById(R.id.cancelDelete))
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //donothing
                        customDialog.dismiss();
                    }
                });

        ((Button) customDialog.findViewById(R.id.confirmDelete))
                .setText("Change");
        ((Button) customDialog.findViewById(R.id.confirmDelete))

                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if( rotateImage2!=null && imageRotated2!=null)
                        ImageDelete.saveImage(rotateImage2,imageRotated2);
                        aa.notifyDataSetChanged();

                        Toast.makeText(getApplicationContext(), "Changed", Toast.LENGTH_SHORT).show();
                        customDialog.dismiss();
                    }
                });
        haveRotate=false;
        imageRotated=null;
        rotateImage=null;
        customDialog.show();
    }
}