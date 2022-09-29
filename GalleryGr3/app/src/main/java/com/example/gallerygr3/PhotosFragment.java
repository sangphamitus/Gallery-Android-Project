package com.example.gallerygr3;

import android.content.Context;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PhotosFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PhotosFragment extends Fragment implements FolderCallBack {

    Button backBtn;
    Context context;
    String currentDirectory=null;

    ArrayList<String> DirInPaths;
    ArrayList<String> FileInPaths;
    RecyclerView folderPictureReview;
    RecyclerView imageReview;

    String SD;
    View currentView;

    TextView currDirDisplay;



    String[] ImageExtensions = new String[] {
            ".jpg",
            ".png",
            ".gif",
            ".jpeg"
    };

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public PhotosFragment() {

        // Required empty public constructor
    }



    // TODO: Rename and change types and number of parameters
    public static PhotosFragment newInstance(Context context) {
        PhotosFragment fragment = new PhotosFragment();
        return fragment;
    }
    private void initiateApp()
    {

        currentDirectory= ((MainActivity)context).getCurrentDirectory();
        if(currentDirectory==null)
        {
            ((MainActivity)context).setCurrentDirectory(SD);
            currentDirectory= ((MainActivity)context).getCurrentDirectory();

        }
        setTitleFolder();

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        context= getContext();
        SD= ((MainActivity)context).getSDDirectory();




    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        currentView= inflater.inflate(R.layout.fragment_photos, container, false);

        folderPictureReview = (RecyclerView) currentView.findViewById(R.id.folderPictureReview);
        imageReview =(RecyclerView) currentView.findViewById(R.id.imagePictureView);


        currDirDisplay =(TextView) currentView.findViewById(R.id.currentDir);

        backBtn =(Button) currentView.findViewById(R.id.backButton);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ArrayList<String> folderPaths=((MainActivity)context).getFolderPath();
                if(folderPaths.size()==1)
                {
                    Toast.makeText(context,"cannot back directory",Toast.LENGTH_SHORT).show();
                }
                else
                {
                   String outer= ((MainActivity)context).popFolderPath();
                    ((MainActivity)context).setCurrentDirectory(outer);
                   // currentDirectory=((MainActivity)context).getCurrentDirectory();
                    setTitleFolder();

                }
            }
        });

        initiateApp();

        return currentView;
    }



    public void setTitleFolder()
    {
        currentDirectory=((MainActivity)context).getCurrentDirectory();
        int getPositionFolderName=  currentDirectory.lastIndexOf("/");
        String name= currentDirectory.substring(getPositionFolderName + 1);
        TextView curDir=(TextView) currentView.findViewById(R.id.currentDir);

        curDir.setText(name);
        readFolder();
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
            //do nothing
        }
        loadFolders();
        readImagesInFolder();
    }

    private void readImagesInFolder()
    {
        try
        {
            String Dir=currentDirectory;

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

            loadImages();


    }

    private void loadFolders()
    {
        FolderAdapter folderAdapter =new FolderAdapter(context,DirInPaths,this);

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