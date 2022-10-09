package com.example.gallerygr3;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.example.gallerygr3.SelectedPicture;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.BitmapFactory;

import android.graphics.drawable.Drawable;

import android.graphics.Matrix;

import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import android.graphics.Bitmap;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import org.jetbrains.annotations.Nullable;


import java.io.FileOutputStream;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.io.File;

/*
* File imgFile= new File(Images.get(position));
        Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
        holder.imageItem.setImageBitmap(myBitmap);
*
* */


public class ImageDisplay extends Fragment {

    ImageButton changeBtn;
    FloatingActionButton fab_camera,fab_expand,fab_url;
    GridView gridView;
    CardView cardView;
    ArrayList<String> names = new ArrayList<>();
    int numCol=2;
    ArrayList<String> images;
    String namePictureShoot="";
    Bundle myStateInfo;
    LayoutInflater myStateinflater;
    ViewGroup myStatecontainer;
    ImageDisplay.CustomAdapter customAdapter=null;
    ImageDisplay.ListAdapter listAdapter=null;
    private static final int CAMERA_REQUEST = 1888;


    //universal-image-loader
    // Create default options which will be used for every
//  displayImage(...) call if no options will be passed to this method

    public ImageDisplay() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static ImageDisplay newInstance(String param1, String param2) {
        ImageDisplay fragment = new ImageDisplay();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public class CustomAdapter extends BaseAdapter {
        private ArrayList<String> imageNames;
        private ArrayList<String> imagePhotos;
        private Context context;
        private LayoutInflater layoutInflater;
        private class ViewHolder{
            ImageView imageView;
        }

        public CustomAdapter(ArrayList<String> imageNames, ArrayList<String> imagePhotos, Context context) {
            this.imageNames = imageNames;
            this.imagePhotos = imagePhotos;
            this.context = context;
            this.layoutInflater = (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);

        }

        @Override
        public int getCount() {
            return imagePhotos.size();
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            ViewHolder viewHolder=null;
//            Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
            if(view == null){
                view =layoutInflater.inflate(R.layout.row_item,viewGroup,false);
                viewHolder=new ViewHolder();
                viewHolder.imageView=view.findViewById(R.id.imageView);
                view.setTag(viewHolder);
            } else {
                viewHolder=(ViewHolder) view.getTag();
            }
//            viewHolder.imageView.setImageBitmap(myBitmap);
            File imgFile= new File(imagePhotos.get(i));
            ImageLoader.getInstance().displayImage(String.valueOf(Uri.parse("file://"+imgFile.getAbsolutePath().toString())),viewHolder.imageView);

            return view;
        }



    }

    public class ListAdapter extends BaseAdapter{
        private ArrayList<String> imageNames;
        private ArrayList<String> imagePhotos;
        private Context context;
        private LayoutInflater layoutInflater;
        private class ViewHolder{
            TextView textView;
            ImageView imageView;
        }

        public ListAdapter(ArrayList<String> imageNames, ArrayList<String> imagePhotos, Context context) {
            this.imageNames = imageNames;
            this.imagePhotos = imagePhotos;
            this.context = context;
            this.layoutInflater = (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);

        }

        @Override
        public int getCount() {
            return imagePhotos.size();
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            ViewHolder viewHolder=null;
            if(view == null){
                view =layoutInflater.inflate(R.layout.list_item,viewGroup,false);
                viewHolder=new ViewHolder();
                viewHolder.imageView=view.findViewById(R.id.imageView);
                viewHolder.textView=view.findViewById(R.id.tvName);
                view.setTag(viewHolder);
            } else {
                viewHolder=(ViewHolder) view.getTag();
            }
            TextView tvName = viewHolder.textView;
            tvName.setText(imageNames.get(i));

            File imgFile= new File(imagePhotos.get(i));
            ImageLoader.getInstance().displayImage(String.valueOf(Uri.parse("file://"+imgFile.getAbsolutePath().toString())),viewHolder.imageView);
            return view;
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Sử dụng thư viện univeral-image-loader
        DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
                .delayBeforeLoading(0)
                .resetViewBeforeLoading(true)
                .showImageOnLoading(R.drawable.placehoder)
                .showImageForEmptyUri(R.drawable.error_image)
                .showImageOnFail(R.drawable.error_image)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .build();
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getActivity())
                .defaultDisplayImageOptions(defaultOptions)
                .build();
        ImageLoader.getInstance().init(config);

        Context context= getActivity();
        images =((MainActivity)context).getFileinDir();
        //create name array
        names= new ArrayList<String>();

        for(int i=0;i<images.size();i++){

            // get name from file ===================================

            String name = getDisplayName(images.get(i));
            names.add(name);

            // ====================================================
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        myStateinflater=inflater;
        myStatecontainer=container;
        myStateInfo = savedInstanceState;
        //      myStateInfo = savedInstanceState;
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_image_display, container, false);

        gridView = (GridView) view.findViewById(R.id.gridView);
        changeBtn = (ImageButton)view.findViewById(R.id.resizeView);
        cardView = (CardView) view.findViewById(R.id.cardView);
        fab_camera=(FloatingActionButton) view.findViewById(R.id.fab_Camera);
        fab_expand=(FloatingActionButton) view.findViewById(R.id.fab_expand);
        fab_url=(FloatingActionButton) view.findViewById(R.id.fab_url);

        if(customAdapter==null)
        {
            customAdapter = new ImageDisplay.CustomAdapter(names,images,getActivity());

        }
        if(listAdapter==null)
        {
            listAdapter = new ImageDisplay.ListAdapter(names,images,getActivity());
        }

        gridView.setAdapter(customAdapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String selectedName= images.get(i);

//                int selectedImage = images[i];

                startActivity(new Intent(getActivity(), SelectedPicture.class)
                        .putExtra("name", selectedName)
                        .putExtra("images",images));
            }

        });

        changeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                numCol=numCol%5+1;
                if(numCol==1){
//                    numCol=2;
                    gridView.setAdapter(listAdapter);

                } else if(numCol == 2) {
                    gridView.setAdapter(customAdapter);
                }
                gridView.setNumColumns(numCol);
            }
        });

        fab_camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openCamera();
            }
        });
        fab_url.setVisibility(View.INVISIBLE);
        fab_camera.setVisibility(View.INVISIBLE);
        fab_expand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (fab_camera.getVisibility() == View.INVISIBLE){
                    fab_url.setVisibility(View.VISIBLE);
                    fab_camera.setVisibility(View.VISIBLE);
                } else {
                    fab_url.setVisibility(View.INVISIBLE);
                    fab_camera.setVisibility(View.INVISIBLE);
                }
            }
        });

        return view;
//        return inflater.inflate(R.layout.fragment_image_display, container, false);
    }



    // You can do the assignment inside onAttach or onCreate, i.e, before the activity is displayed
    ActivityResultLauncher<Intent> someActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        // There are no request codes

                        File imgFile= new File(namePictureShoot);
                        Bitmap imageShoot= BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                        imageShoot=rotateImage(imageShoot,90);
                        saveImage(imageShoot,namePictureShoot);

                        images.add(namePictureShoot);
                        names.add(getDisplayName(namePictureShoot));

                        customAdapter.notifyDataSetChanged();
                        listAdapter.notifyDataSetChanged();


                        Toast.makeText(getContext(), "Taking picture", Toast.LENGTH_SHORT).show();
                    }
                }
            });
    private void saveImage(Bitmap finalBitmap,String imagePath) {

        File myFile = new File(imagePath);

        if (myFile.exists()) myFile.delete ();
        try {
            FileOutputStream out = new FileOutputStream(myFile);
            finalBitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static Bitmap rotateImage(Bitmap bmpSrc, float degrees) {
        int w = bmpSrc.getWidth();
        int h = bmpSrc.getHeight();
        Matrix mtx = new Matrix();
        mtx.postRotate(degrees);
        Bitmap bmpTrg = Bitmap.createBitmap(bmpSrc, 0, 0, w, h, mtx, true);
        return bmpTrg;
    }
    private void openCamera()  {
        // Ask permission
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(),new String[]{
                    Manifest.permission.CAMERA
            },100);
        }

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        intent.putExtra(MediaStore.EXTRA_OUTPUT,getUri(Environment.DIRECTORY_PICTURES));
        // startActivity(intent);
        //startActivityForResult(intent,CAMERA_REQUEST);
        someActivityResultLauncher.launch(intent);
    }
    public static String generateFileName(){
        LocalDateTime now=LocalDateTime.now();
        DateTimeFormatter myFormat=DateTimeFormatter.ofPattern("yyyyMMdd_HHmmssSSS");
        return now.format(myFormat);
    }
    // Android 10+
    private Uri getUri(String path){

        ContentValues values = new ContentValues();
        String tempName=generateFileName()+".jpg";
        namePictureShoot= ((MainActivity)getContext()).getCurrentDirectory()+'/'+tempName;
        values.put(MediaStore.Images.Media.DISPLAY_NAME,tempName );
        values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
        values.put(MediaStore.Images.Media.RELATIVE_PATH, path);

        Uri uri = getActivity().getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,values);

        return uri;
    }
    private String getDisplayName(String path){
        int getPositionFolderName= path.lastIndexOf("/");
        String name= path.substring(getPositionFolderName + 1);

        String[] ArrayName= name.split("\\.");
        String displayName="";

        if (ArrayName[0].length() > 10)
        {
            displayName = ArrayName[0].substring(0, 5);
            displayName+="...";
            displayName += ArrayName[0].substring(ArrayName[0].length()-5);
        }
        else
        {
            displayName = ArrayName[0];
        }
        displayName+="."+ArrayName[1];
        return displayName;
    }
}