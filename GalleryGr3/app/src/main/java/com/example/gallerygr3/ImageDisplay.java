
package com.example.gallerygr3;
import com.example.gallerygr3.SelectedPicture;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

import android.Manifest;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import android.graphics.Bitmap;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.io.File;
import java.util.Arrays;



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
    ArrayList<String> names;
    int numCol=2;
    ArrayList<String> images;
    String namePictureShoot="";
    Bundle myStateInfo;
    LayoutInflater myStateinflater;
    ViewGroup myStatecontainer;
    ImageDisplay.CustomAdapter customAdapter=null;
    ImageDisplay.ListAdapter listAdapter=null;
    private static final int CAMERA_REQUEST = 1888;

//    int[] images ={R.drawable.avatar1,R.drawable.avatar2, R.drawable.avatar3,
//            R.drawable.avatar4, R.drawable.avatar5, R.drawable.avatar6,
//            R.drawable.avatar7, R.drawable.avatar8, R.drawable.avatar9,
//            R.drawable.avatar10, R.drawable.avatar11, R.drawable.avatar12,
//    };


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
            File imgFile= new File(imagePhotos.get(i));
            Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
            if(view == null){
                view =layoutInflater.inflate(R.layout.row_item,viewGroup,false);
                viewHolder=new ViewHolder();
                viewHolder.imageView=view.findViewById(R.id.imageView);
                view.setTag(viewHolder);
            } else {
                viewHolder=(ViewHolder) view.getTag();
            }
            viewHolder.imageView.setImageBitmap(myBitmap);

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
            ImageView imageView = viewHolder.imageView;

            tvName.setText(imageNames.get(i));


            File imgFile= new File(imagePhotos.get(i));
            Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
            imageView.setImageBitmap(myBitmap);

            return view;
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        Context context= getActivity();
        images =((MainActivity)context).getFileinDir();

        //create name array

        for(int i=0;i<images.size();i++){

            // get name from file===================================


            names.add(getDisplayName(images.get(i)));

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
        fab_expand=(FloatingActionButton) view.findViewById(R.id.fab_Expand);
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
                        .putExtra("name", selectedName));
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
                        Intent data = result.getData();
                        images.add(namePictureShoot);
                        names.add(getDisplayName(namePictureShoot));
                        customAdapter.notifyDataSetChanged();
                        listAdapter.notifyDataSetChanged();


                        Toast.makeText(getContext(), "Taking picture", Toast.LENGTH_SHORT).show();
                    }
                }
            });


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
    private String generateFileName(){
        LocalDateTime now=LocalDateTime.now();
        DateTimeFormatter myFormat=DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss");
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