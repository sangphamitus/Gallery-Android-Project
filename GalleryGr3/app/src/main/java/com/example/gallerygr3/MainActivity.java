package com.example.gallerygr3;


import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import android.Manifest;
import android.annotation.SuppressLint;
import android.graphics.Color;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.LinearLayout;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.nostra13.universalimageloader.core.DisplayImageOptions;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class MainActivity extends AppCompatActivity  implements MainCallBack {

    String currentDirectory=null;

    String SD;
    String DCIM;
    String Picture;
    ArrayList<String> folderPaths=new ArrayList<String>();
    public ArrayList<String> FileInPaths=new ArrayList<String>();
//    PhotosFragment photo;

    LinearLayout navbar;
    RelativeLayout chooseNavbar;
    RelativeLayout status;

    MainActivity context;
    FloatingActionButton deleteBtn;
    FloatingActionButton cancelBtn;
    FloatingActionButton selectAll;
    TextView informationSelected;



    String[] ImageExtensions = new String[] {
            ".jpg",
            ".png",
            ".gif",
            ".jpeg"
    };
    LinearLayout[] arrNavLinearLayouts = new LinearLayout[3];
    ImageView[] arrNavImageViews = new ImageView[3];
    TextView[] arrNavTextViews = new TextView[3];
    private int selectedTab = 0;
    int[] arrRoundLayout = new int[3];
    int[] arrIcon = new int[3];
    int[] arrSelectedIcon = new int[3];

    Class[] arrFrag = new Class[3];

    String deleteNotify="";

    public ArrayList<String> chooseToDeleteInList=new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        context= this;

        ActivityCompat.requestPermissions(MainActivity.this,
                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.CAMERA,
                        Manifest.permission.INTERNET
                }, 1);



      //  SD = Environment.getExternalStorageDirectory().getAbsolutePath();
        DCIM = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).getAbsolutePath();
        Picture= Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath();

     //   arrFrag[0] = ImageDisplay.class;
        arrFrag[1] = AlbumsFragment.class;
        arrFrag[2] = SettingsFragment.class;

        arrRoundLayout[0] = R.drawable.round_photos;
        arrRoundLayout[1] = R.drawable.round_albums;
        arrRoundLayout[2] = R.drawable.round_settings;

        navbar = (LinearLayout) findViewById(R.id.navbar);
        chooseNavbar =(RelativeLayout) findViewById(R.id.selectNavbar);
        status= (RelativeLayout) findViewById(R.id.status);

        deleteBtn=(FloatingActionButton) findViewById(R.id.deleteImageButton);
        cancelBtn=(FloatingActionButton) findViewById(R.id.clear);
        selectAll=(FloatingActionButton) findViewById(R.id.selectAll);

        informationSelected=(TextView)findViewById(R.id.infomationText);


        arrIcon[0] = R.drawable.ic_baseline_photo;
        arrIcon[1] = R.drawable.ic_baseline_photo_library;
        arrIcon[2] = R.drawable.ic_baseline_settings;

        deleteBtn.setOnClickListener(new View.OnClickListener()  {
            @Override
            public void onClick(View view) {
                showCustomDialogBox();
            }
        });
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ImageDisplay ic= ImageDisplay.newInstance();
                clearChooseToDeleteInList();
                ic.clearClicked();
            }
        });
        selectAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               ImageDisplay ic= ImageDisplay.newInstance();
               if(chooseToDeleteInList.size()==FileInPaths.size())
               {
                   chooseToDeleteInList.clear();

               }
               else
               {
                   chooseToDeleteInList=new ArrayList<String >(FileInPaths);

               }
                ic.selectAllClicked();
            }
        });

        arrSelectedIcon[0] = R.drawable.ic_baseline_photo_selected;
        arrSelectedIcon[1] = R.drawable.ic_baseline_photo_library_selected;
        arrSelectedIcon[2] = R.drawable.ic_baseline_settings_selected;

        arrNavLinearLayouts[0] = (LinearLayout) findViewById(R.id.photosLayout);
        arrNavLinearLayouts[1] = (LinearLayout) findViewById(R.id.albumsLayout);
        arrNavLinearLayouts[2] = (LinearLayout) findViewById(R.id.settingsLayout);

        arrNavImageViews[0] = (ImageView) findViewById(R.id.photos_img);
        arrNavImageViews[1] = (ImageView) findViewById(R.id.albums_img);
        arrNavImageViews[2] = (ImageView) findViewById(R.id.settings_img);

        arrNavTextViews[0] = (TextView) findViewById(R.id.photos_txt);
        arrNavTextViews[1] = (TextView) findViewById(R.id.albums_txt);
        arrNavTextViews[2] = (TextView) findViewById(R.id.settings_txt);

        arrNavLinearLayouts[0].setOnClickListener(new NavLinearLayouts(0));
        arrNavLinearLayouts[1].setOnClickListener(new NavLinearLayouts(1));
        arrNavLinearLayouts[2].setOnClickListener(new NavLinearLayouts(2));
//
        setCurrentDirectory(Picture);


    }




    private void showCustomDialogBox()
    {
        final Dialog customDialog = new Dialog( context );
        customDialog.setTitle("Delete confirm");

        customDialog.setContentView(R.layout.delete_dialog_notify);

        ((TextView) customDialog.findViewById(R.id.deleteNotify))
                .setText("Do you want to delete "+deleteNotify+" image(s) permanently in your device ?");

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

                        String[] select = chooseToDeleteInList.toArray(new String[chooseToDeleteInList.size()]);
                        // String[] select= (String[]) selectedImages.toArray();
                        ImageDelete.DeleteImage(select);
                        removeImageUpdate(select);
                        clearChooseToDeleteInList();
                        ic.deleteClicked();

                        customDialog.dismiss();
                    }
                });

        customDialog.show();
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            // If request is cancelled, the result arrays are empty.
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                readFolder(Picture);
                readFolder(DCIM);
//                getSupportFragmentManager().beginTransaction()
//             .setReorderingAllowed(true)
//               .replace(R.id.fragment_container, arrFrag[0], null)
//                .commit();
                getSupportFragmentManager().beginTransaction()
                        .setReorderingAllowed(true)
                        .replace(R.id.fragment_container, ImageDisplay.newInstance(), null)
                        .commit();
                Toast.makeText(MainActivity.this, "Permission granted!", Toast.LENGTH_SHORT).show();

            } else {
                Toast.makeText(MainActivity.this, "Permission denied!", Toast.LENGTH_SHORT).show();
                finish();

            }
        }
    }

    @Override
    public void setCurrentDirectory(String Dir) {
        currentDirectory = Dir;

        folderPaths.add(Dir);

        Toast.makeText(this, "Change Dir: " + Dir, Toast.LENGTH_SHORT).show();

    }

    @Override
    public void removeImageUpdate(String[] input)
    {
        for (String name:input)
        {
            FileInPaths.remove(name);

        }

    }
    @Override
    public void addImageUpdate(String[] input)
    {
        for (String name:input)
        {
            FileInPaths.add(name);

        }

    }
    @Override
    public void Holding(boolean isHolding)
    {
        if(isHolding)
        {
            chooseNavbar.setVisibility(View.VISIBLE);
            navbar.setVisibility(View.INVISIBLE);
            status.setVisibility(View.VISIBLE);
        }
        else {
            chooseNavbar.setVisibility(View.INVISIBLE);
            navbar.setVisibility(View.VISIBLE);
            status.setVisibility(View.INVISIBLE);
        }
    }
    @Override
    public void SelectedTextChange(){

        deleteNotify=chooseToDeleteInList.size()+"";
        informationSelected.setText(chooseToDeleteInList.size()+" images selected");
    }

    @Override
    public ArrayList<String> chooseToDeleteInList() {
        return chooseToDeleteInList;
    }
    @Override
    public void clearChooseToDeleteInList()
    {
        chooseToDeleteInList.clear();
    }
    @Override
    public ArrayList<String> adjustChooseToDeleteInList(String ListInp,String type )
    {
        switch (type)
        {
            case "choose":

                    if(!chooseToDeleteInList.contains(ListInp))
                    {
                        chooseToDeleteInList.add(ListInp);
                    }

                break;
            case "unchoose":

                    if(chooseToDeleteInList.contains(ListInp))
                    {
                        chooseToDeleteInList.remove(ListInp);
                    }

                break;

        }
        return chooseToDeleteInList;
    }

    private void readFolder(String Dir) {

            File sdFile = new File(Dir);
            File[] foldersSD = sdFile.listFiles();

            try {
                for (File file : foldersSD) {
                    if (file.isDirectory()) {
                        //get absolute
                        //do nothing
                        readFolder(file.getAbsolutePath());

                    } else {
                        for (String extension : ImageExtensions) {

                            if (file.getAbsolutePath().toLowerCase().endsWith(extension)) {
                                // addImageView(file.getAbsolutePath());
                                FileInPaths.add(file.getAbsolutePath());

                                break;
                            }

                        }
                    }
                }

            } catch (Exception e) {
                //do nothing
            }

        }



    @Override
    public String getSDDirectory() {
        return SD;
    }

    @Override
    public String getCurrentDirectory() {
        return currentDirectory;
    }

    @Override
    public void pushFolderPath(String inp) {
        folderPaths.add(inp);
    }

    @Override
    public void popFolderPath() {
        folderPaths.remove(folderPaths.size()-1 );
        currentDirectory = folderPaths.get(folderPaths.size()-1);
    }

    @Override
    public ArrayList<String> getFolderPath() {
        return folderPaths;
    }

    @Override
    public String getDCIMDirectory() {
        return DCIM;
    }

    @Override
    public String getPictureDirectory() {
        return Picture;
    }

    @Override
    public ArrayList<String> getFileinDir() {
        return FileInPaths;
    }

    protected class NavLinearLayouts implements View.OnClickListener {
        public int thisIndex;

        NavLinearLayouts(int index) {
            thisIndex = index;
        }

        @SuppressLint("ResourceAsColor")
        @Override
        public void onClick(View view) {
            if (selectedTab != thisIndex) {

                //go to current fragment
                selectedTab = thisIndex;
                if(thisIndex!=0)
                {

                getSupportFragmentManager().beginTransaction()
                        .setReorderingAllowed(true)
                        .replace(R.id.fragment_container, arrFrag[thisIndex], null)
                        .commit();
                }
                else
                {
                    getSupportFragmentManager().beginTransaction()
                            .setReorderingAllowed(true)
                            .replace(R.id.fragment_container, ImageDisplay.newInstance(), null)
                            .commit();
                }
                // change others icon
                for (int i = 0; i < 3; i++) {
                    if (i != thisIndex) {
                        arrNavTextViews[i].setVisibility(View.GONE);
                        arrNavImageViews[i].setImageResource(arrIcon[i]);
                        arrNavLinearLayouts[i].setBackgroundColor(getResources().getColor(R.color.white, null));
                        arrNavImageViews[i].setColorFilter(R.color.black);
                    }
                    else
                    {
                        arrNavImageViews[i].setColorFilter(Color.argb(255, 255, 255, 255));
                    }

                    //change this icon
                    arrNavTextViews[thisIndex].setVisibility(View.VISIBLE);
                    arrNavImageViews[thisIndex].setImageResource(arrSelectedIcon[thisIndex]);
                    arrNavLinearLayouts[thisIndex].setBackgroundResource(arrRoundLayout[thisIndex]);

                    //animation
                    ScaleAnimation scaleAnimation = new ScaleAnimation(0.8f, 1.0f, 1f, 1f, Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 0.0f);
                    scaleAnimation.setDuration(200);
                    scaleAnimation.setFillAfter(true);
                    arrNavLinearLayouts[thisIndex].startAnimation(scaleAnimation);
                }


            }
        }

    }
}