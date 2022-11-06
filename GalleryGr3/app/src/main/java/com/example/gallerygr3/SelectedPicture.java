package com.example.gallerygr3;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.WallpaperManager;
import android.content.Intent;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import android.media.MediaPlayer;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
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
    LinearLayout subInfo;
    Button changeWallpaper;
    Button changeWallpaperLock;
    Button changeFileName;

    ImageButton backBtn;
    ImageButton deleteBtn;
    ImageButton infoBtn;
    ImageButton moreBtn;
    String currentSelectedName;
    int currentPosition;
    MainActivity main;

    RelativeLayout topNav;
    RelativeLayout bottomNav;

    boolean displayNavBars = true;
    boolean displaySubBar = false;
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
                showCustomDialogBoxDelete();
            }
        });

        infoBtn = (ImageButton)findViewById(R.id.infoBtn) ;
        infoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showCustomDialogBoxInformation();
            }
        });
        subInfo = (LinearLayout)findViewById(R.id.subInfo);
        moreBtn = (ImageButton)findViewById(R.id.moreBtn);
        moreBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(displaySubBar){
                    subInfo.setVisibility(View.INVISIBLE);
                    displaySubBar=false;
                }
                else{
                    subInfo.setVisibility(View.VISIBLE);
                    displaySubBar=true;
                }
            }
        });


       //SUBNAV
        WallpaperManager wallpaperManager = WallpaperManager.getInstance(getApplicationContext());
        changeWallpaper = (Button)findViewById(R.id.changeWallpaper);
        changeWallpaperLock = (Button)findViewById(R.id.changeWallpaperLock);
        changeFileName = (Button)findViewById(R.id.changeNameFile);

        changeWallpaper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    //set màn hình chinhs
                    wallpaperManager.setBitmap(getItemBitmap(currentSelectedName));
                    showDialogSuccessChange("Change Wallpaper Successfully");
                } catch (IOException e) {
                    e.printStackTrace();
                }
                    subInfo.setVisibility(View.INVISIBLE);
            }
        });
        changeWallpaperLock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    //set màn hình khóa
                    wallpaperManager.setBitmap(getItemBitmap(currentSelectedName), null, false, WallpaperManager.FLAG_LOCK);
                    showDialogSuccessChange("Change Lock sceen Wallpaper Successfully");
                } catch (IOException e) {
                    e.printStackTrace();
                }
                subInfo.setVisibility(View.INVISIBLE);
            }
        });
        changeFileName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                displaySubBar=false;
                subInfo.setVisibility(View.INVISIBLE);
                showDialogRename();
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

            listItem= new  ArrayList<viewPagerItem> ();
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
        deleteStringArrayByPossision(paths, currentPosition);
        deleteStringArrayByPossision(dates, currentPosition);
        deleteIntergerArrayByPossision(size, currentPosition);
        //xóa trong adepter
        listItem.remove(currentPosition);
        viewPagerAdapter aa=new viewPagerAdapter(listItem,this);
        viewPager2.setAdapter(aa);
        viewPager2.setCurrentItem(currentPosition ,false);
    }
    public void renameImageUpdate(String input){
        //sửa lại tên local trong adepter
        paths[currentPosition] = paths[currentPosition].substring(0, paths[currentPosition].lastIndexOf("/")+1) + input;
    }

    @Override
    public void showNav() {

        if(!displayNavBars){
            topNav.setVisibility(View.VISIBLE);
            bottomNav.setVisibility(View.VISIBLE);
            displayNavBars = true;
        }else {
            displayNavBars = false;
            displaySubBar = false;
            bottomNav.setVisibility(View.INVISIBLE);
            topNav.setVisibility(View.INVISIBLE);
            subInfo.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void hiddenNav() {
        return;
    }

    private void showCustomDialogBoxDelete()
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
                .setText(shortenName(ImageDisplay.getDisplayName(paths[currentPosition])));
        ((TextView) customDialog.findViewById(R.id.photoPath))
                .setText(paths[currentPosition]);
        ((TextView) customDialog.findViewById(R.id.photoLastModified))
                .setText(dates[currentPosition]);
        ((TextView) customDialog.findViewById(R.id.photoSize))
                .setText(size[currentPosition]*1.0/1024+" kb");
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

    private void showDialogSuccessChange(String message)
    {
        final Dialog customDialog = new Dialog( this );
        customDialog.setTitle("Message");

        customDialog.setContentView(R.layout.show_success_dialog);
//
        ((TextView) customDialog.findViewById(R.id.messageShow))
                .setText(message);

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
    private void showDialogRename()
    {
        final Dialog customDialog = new Dialog( this );
        customDialog.setTitle("Change Wallpaper");

        customDialog.setContentView(R.layout.edit_text_dialog);


        ((Button) customDialog.findViewById(R.id.ok_button))
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //thực hiện đổi tên tại đây

                        ImageDisplay ic= ImageDisplay.newInstance();
                        //ham nay lam sau chu khong bo
                        //cập nhật lại danh sách trong ImageDisplay
                        EditText editText = customDialog.findViewById(R.id.editChangeFileName);
                        String fileExtension=paths[currentPosition].substring(paths[currentPosition].lastIndexOf("."));
                        while ( fileExtension.charAt(fileExtension.length() - 1) == '\n') {
                            fileExtension = fileExtension.substring(0, fileExtension.length() - 1);
                        }
                        String newName = editText.getText()+fileExtension;

                        customDialog.dismiss();
                        ic.renameClicked(ImageDisplay.getDisplayName(paths[currentPosition]), newName);
                        renameImageUpdate(newName);

                        showDialogSuccessChange("File name change successfully !");
                    }
                });

        customDialog.show();
    }
    public void deleteStringArrayByPossision(String[]arr, int pos){
        int size = arr.length;
        if(pos != arr.length - 1 ){
            for(int i=pos; i < size - 1; i++){
                arr[pos] = arr[pos+1];
            }
        }
    }public void deleteIntergerArrayByPossision(int[]arr, int pos){
        int size = arr.length;
        if(pos != arr.length - 1 ){
            for(int i=pos; i < size - 1; i++){
                arr[pos] = arr[pos+1];
            }
        }
    }
    public Bitmap getItemBitmap(String selectedName) {
        File imgFile= new File(selectedName);
        return BitmapFactory.decodeFile(imgFile.getAbsolutePath());
    }
    public String shortenName(String name){
        String[] ArrayName= name.split("\\.");
        String displayName="";

        if (ArrayName[0].length() > 25)
        {
            displayName = ArrayName[0].substring(0, 10);
            displayName+="...";
            displayName += ArrayName[0].substring(ArrayName[0].length()-10);
        }
        else
        {
            displayName = ArrayName[0];
        }
        displayName+="."+ArrayName[1];
        return displayName;
    }
}