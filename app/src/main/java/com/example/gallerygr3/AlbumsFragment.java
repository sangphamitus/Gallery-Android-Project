package com.example.gallerygr3;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Picture;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;


public class AlbumsFragment extends Fragment{
    Context context;
    int spanColumns;
    Gson gson = new Gson();
    ArrayList<Album> albumList;
    public static String filename = "AlbumData";
    FloatingActionButton fab_addNewAlbum;
    RecyclerView rcv_albumList;
    public static String folderPath="/Glr3/Albums";

    public AlbumsFragment() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static AlbumsFragment newInstance() {
        AlbumsFragment fragment = new AlbumsFragment();
        // put args
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            // get args
        } else {

        }
        context = getActivity();
        readData();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        CoordinatorLayout layout = (CoordinatorLayout) inflater.inflate(R.layout.fragment_albums, container, false);
        Toast.makeText(context, "album create", Toast.LENGTH_SHORT).show();
        // SAMPLE TEST
        spanColumns = 2;
        rcv_albumList = layout.findViewById(R.id.album_list);
        rcv_albumList.setLayoutManager(new LinearLayoutManager(context));
        rcv_albumList.setAdapter(new AlbumAdapter(albumList,context));
        rcv_albumList.addItemDecoration(new SpacesItemDecoration(20, 1));

//        InitSampleTest();
        //

        // Add new Button
        fab_addNewAlbum=layout.findViewById(R.id.album_fab_add);
        fab_addNewAlbum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showNewFolderDialog();
            }
        });
        return layout;
    }

    private void readData(){
        MainActivity ma= (MainActivity) getContext();
        albumList=new ArrayList<Album>();
        File path=new File(ma.Picture+folderPath);
        if(!path.isDirectory()){
            path.mkdirs();
        } else {
            File[] albumFolders=path.listFiles();
            for(File folder : albumFolders){
                if(!folder.isFile()){
                    albumList.add(new Album(folder.getAbsolutePath(),folder.getName(),imagePathInFolder(folder)));
                }
            }
        }
    }
    private ArrayList<String> imagePathInFolder(File folder){
        ArrayList<String> result=new ArrayList<String>();

        MainActivity mainActivity= (MainActivity) context;
        for (File file : folder.listFiles()) {
            if (file.isDirectory()) {

            } else {
                for (String extension : mainActivity.ImageExtensions) {

                    if (file.getAbsolutePath().toLowerCase().endsWith(extension)) {
                        // addImageView(file.getAbsolutePath());
                        result.add(file.getAbsolutePath());

                        break;
                    }

                }
            }
        }
        return result;
    }



    private void showNewFolderDialog(){
        NewFolderDialog dialog=new NewFolderDialog(context);
        dialog.show();
    }
    private boolean addNewFolder(String newAlbumName){
        MainActivity ma= (MainActivity) context;
        File path=new File(ma.Picture+folderPath+"/"+newAlbumName);
        if(path.isDirectory()){
            return false;
        } else {
            path.mkdirs();
        }
        ArrayList<String> imagePaths=new ArrayList<String>();
        albumList.add(new Album(path.getAbsolutePath(),newAlbumName,imagePaths));
        rcv_albumList.getAdapter().notifyItemChanged(albumList.size()-1);
        return true;
    }



    public class NewFolderDialog extends Dialog{

        public NewFolderDialog(@NonNull Context context) {
            super(context);
            LinearLayout layout= (LinearLayout) getLayoutInflater().inflate(R.layout.new_album_dialog,null);
            ImageButton newBtn=layout.findViewById(R.id.new_alubum_button);
            ImageButton cancleBtn=layout.findViewById(R.id.new_album_cancel);
            EditText nameFolder=layout.findViewById(R.id.new_album_name);
            newBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String newAlbumName= nameFolder.getText().toString();
                    if(addNewFolder(newAlbumName)){
                        dismiss();
                    } else {
                        showAlertDialog("Error creating Album","Album's name already exists!");
                    }
                }
            });
            cancleBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dismiss();
                }
            });
            setContentView(layout);
            WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
            layoutParams.copyFrom(getWindow().getAttributes());
            layoutParams.width = (int) (WindowManager.LayoutParams.MATCH_PARENT);
            layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
            getWindow().setAttributes(layoutParams);
        }
    }
    private void showAlertDialog (String title,String message){
        AlertDialog.Builder builder=new AlertDialog.Builder(context);
        builder.setTitle(title).setMessage(message);
        builder.setIcon(android.R.drawable.ic_dialog_alert);
        builder.create().show();
    }

}