package com.example.gallerygr3;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;

import android.content.ClipData;
import android.content.Context;

import android.content.Intent;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;

import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.MotionEvent;

import android.graphics.ColorSpace;

import android.os.Bundle;


import android.media.MediaPlayer;

import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import java.io.File;
import java.io.FileOutputStream;
import java.lang.reflect.Array;
import java.util.ArrayList;

public class SelectedPicture extends AppCompatActivity implements ISelectedPicture {


    ViewPager2 viewPager2;
    ArrayList<viewPagerItem> listItem;
    String[] paths;
    String[] dates;
    String[] names;
    int[] size;
    ArrayList<String> imagesPath;
    ArrayList<String> imagesDate;
    ArrayList<Integer> imagesSize;
    MediaPlayer mediaPlayer;

    ImageButton backBtn;

    ImageButton shareBtn;

   


    ImageButton infoBtn;

    

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




        topNav = (RelativeLayout) findViewById(R.id.topNavSinglePic);
        bottomNav = (RelativeLayout) findViewById(R.id.bottomNavSinglePic);


        infoBtn = (ImageButton)findViewById(R.id.infoBtn) ;
        infoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showCustomDialogBoxInformation();
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
        //get img and name data

        Intent intent = getIntent();
        if(intent.getExtras()!=null){

            //cut name
            imagesPath = intent.getStringArrayListExtra("images");
            imagesDate = intent.getStringArrayListExtra("dates");
            imagesSize = intent.getIntegerArrayListExtra("size");
            int pos = intent.getIntExtra("pos",0);
            String selectedName = intent.getStringExtra("name");
            ArrayList<String> images = intent.getStringArrayListExtra("images");


            names= new String[images.size()];
            // fix name from data
            for(int i=0;i<images.size();i++){
                names[i]=images.get(i);
            }


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
            if(aa==null)
            aa=new viewPagerAdapter(listItem,this);

            viewPager2.setAdapter(aa);
            viewPager2.setCurrentItem(pos,false);
            viewPager2.setClipToPadding(false);
            viewPager2.setClipChildren(false);
            viewPager2.setOffscreenPageLimit(1);
            viewPager2.getChildAt(0).setOverScrollMode(View.OVER_SCROLL_NEVER);


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

            shareBtn=(ImageButton)findViewById(R.id.shareBtn);
            shareBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    ArrayList<String> listPaths= new ArrayList<String>();
                    listPaths.add(names[currentPosition]);
                    shareSingleImages(listPaths);
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

    public void shareSingleImages(ArrayList<String> paths){

        ArrayList<Bitmap> bitmaps = new ArrayList<Bitmap>();

        for(int i=0;i<paths.size();i++){
            bitmaps.add(BitmapFactory.decodeFile(paths.get(i)));
        }


        try {
            ArrayList<Uri> uris = new ArrayList<>();

            for(int i =0;i<paths.size();i++){
                File file = new File(paths.get(i));
                FileOutputStream fOut = new FileOutputStream(file);
                bitmaps.get(i).compress(Bitmap.CompressFormat.JPEG, 100, fOut);
                fOut.flush();
                fOut.close();
                file.setReadable(true,false);

                Uri uri = FileProvider.getUriForFile(getApplicationContext(),
                        "com.example.gallerygr3.provider", file);
                uris.add(uri);
            }
            Intent intent = null;

            if(paths.size()==1){
                intent = new Intent(Intent.ACTION_SEND);
            }else{
                intent = new Intent(Intent.ACTION_SEND_MULTIPLE);
            }

            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            if(paths.size()==1) {
                intent.putExtra(Intent.EXTRA_STREAM, uris.get(0));
            }else{
                intent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, uris);

            }

            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.setType("image/*");
            startActivity(Intent.createChooser(intent, "Share file via"));

        }
        catch (Exception e){
//            Toast.makeText(main, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
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
    public void deleteArrayByPossision(String[]arr, int pos){
        int size = arr.length;
        if(pos != arr.length - 1 ){
            for(int i=pos; i < size - 1; i++){
                arr[pos] = arr[pos+1];
            }
        }
    }
}


       