package com.example.gallerygr3;


import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;

import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;



public class MainActivity extends AppCompatActivity  implements MainCallBack {

    Button backBtn;
    Context context;
    String currentDirectory;
    ArrayList<String> folderPaths=new ArrayList<String>();
    ArrayList<String> DirInPaths;
    ArrayList<String> FileInPaths;
    RecyclerView folderPictureReview;
    RecyclerView imageReview;
    String DCIM;
    String Picture;
    String SD;
    Spinner chooseRoot;

    String[] ImageExtensions = new String[] {
            ".jpg",
            ".png",
            ".gif",
            ".jpeg"
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        DCIM= Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).getAbsolutePath();
        Picture =Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath();
        SD=  Environment.getExternalStorageDirectory().getAbsolutePath();
        String[] items={DCIM,Picture,SD};
        ActivityCompat.requestPermissions(MainActivity.this,
                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                     }, 1);
        chooseRoot =(Spinner) findViewById(R.id.chooseRootDir);
        chooseRoot.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1,items));
        chooseRoot.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                setCurrentDirectory(items[i]);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                    //do nothing
            }
        });


        backBtn =(Button) findViewById(R.id.backButton);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(folderPaths.size()==1)
                {
                    Toast.makeText(MainActivity.this,"cannot back directory",Toast.LENGTH_SHORT).show();
                }
                else
                {
                    folderPaths.remove(folderPaths.size()-1 );
                    setCurrentDirectory(folderPaths.get(folderPaths.size()-1));
                }
            }
        });

        //loadFolder();

    }
    private void initiateApp()
    {

        context=MainActivity.this;
        folderPictureReview = (RecyclerView) findViewById(R.id.folderPictureReview);
        imageReview =(RecyclerView) findViewById(R.id.imagePictureView);
        setCurrentDirectory(DCIM);

    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            // If request is cancelled, the result arrays are empty.
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                initiateApp();
                Toast.makeText(MainActivity.this, "Permission granted!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(MainActivity.this, "Permission denied!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void setCurrentDirectory(String Dir)
    {
        currentDirectory=Dir;
        folderPaths.add(currentDirectory);
        Toast.makeText(this,"Change Dir: "+Dir,Toast.LENGTH_SHORT).show();
        setTitleFolder();
        readFolder();
    }
    private void setTitleFolder()
    {

        int getPositionFolderName=  currentDirectory.lastIndexOf("/");
        String name= currentDirectory.substring(getPositionFolderName + 1);
        TextView curDir=(TextView) findViewById(R.id.currentDir);

        curDir.setText(name);
        Context context =getApplicationContext();
        Toast.makeText(context,"Dir:"+name,Toast.LENGTH_SHORT);
    }
    private void readFolder()
    {
        String Dir=currentDirectory;

        File sdFile= new File(Dir);
        File[] foldersSD= sdFile.listFiles();

        try
        {

            DirInPaths=new ArrayList<String>();
            for (File folder:foldersSD)
            {
                boolean flag=false;
                if( folder.isDirectory())
                {
                    //get absolute
                    DirInPaths.add(folder.getAbsolutePath());

                }
            }

        }
        catch(Exception e)
        {
            finish();
        }
        loadFolders();
        readImagesInFolder();
    }

    private void readImagesInFolder()
    {
        try
        {
            String Dir=currentDirectory;
            setTitleFolder();
            File folder=new File(Dir);
            File[] allFiles=folder.listFiles();

            FileInPaths= new ArrayList<String>();
            for(File file:allFiles)
            {

                for(String extension:ImageExtensions)
                {
                    if (file.getAbsolutePath().toLowerCase().endsWith(extension))
                    {
                       // addImageView(file.getAbsolutePath());
                        FileInPaths.add(file.getAbsolutePath());

                        break;
                    }

                }
                if(FileInPaths.size()>10)
                {
                    break;
                }
            }
        }
        catch (Exception e)
        {
            Log.e("Error",e.getMessage());
        }
        if(FileInPaths.size()>0)
        {
            loadImages();
        }

    }

//    private void addButtonView(String file)
//    {
//        GridLayout linearLayout =  (GridLayout) findViewById(R.id.listFile);
//
//        Button tv = new Button(this);
//        tv.setText(file);
//        tv.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams .MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
//        tv.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                currentDirectory=file;
//                setTitleFolder();
//                readFolder();
//
//            }
//        });
//        linearLayout.addView(tv);
//
//    }
//
//    private void addImageView(String file)
//    {
//        LinearLayout linearLayout =  (LinearLayout) findViewById(R.id.listFile);
//
//        TextView tv = new TextView(this);
//        tv.setText(file);
//        tv.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams .MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
//
//        linearLayout.addView(tv);
//
//    }

        private void loadFolders()
        {
            FolderAdapter folderAdapter =new FolderAdapter(context,DirInPaths);
            folderPictureReview.setAdapter(folderAdapter);
            folderPictureReview.setLayoutManager(new GridLayoutManager(context,2));
        }
        private  void loadImages()
        {
            ArrayList<String> inp= new ArrayList<String>();
            for (int i=0;i<(FileInPaths.size()>10?10:FileInPaths.size());i++ )
            {
                inp.add(FileInPaths.get(i));
            }
            ImageAdapter imgAdapter =new ImageAdapter(context,inp);
            imageReview.setAdapter(imgAdapter);
            imageReview.setLayoutManager(new GridLayoutManager(context,2));
        }
}