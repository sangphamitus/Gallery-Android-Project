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
import com.example.gallerygr3.ImageDelete;
import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.app.DownloadManager;
import android.app.Notification;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.BitmapFactory;

import android.graphics.Color;
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
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.webkit.DownloadListener;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
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
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Timer;

/*
* File imgFile= new File(Images.get(position));
        Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
        holder.imageItem.setImageBitmap(myBitmap);
*
* */


public class ImageDisplay extends Fragment implements chooseAndDelete{

    private static ImageDisplay INSTANCE = null;

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

    boolean isHolding=false;

    ArrayList<String> selectedImages=new ArrayList<>();


    private static final int CAMERA_REQUEST = 1888;


    //universal-image-loader
    // Create default options which will be used for every
//  displayImage(...) call if no options will be passed to this method

    private ImageDisplay() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static ImageDisplay newInstance() {
        if(INSTANCE==null)
        {
            synchronized (ImageDisplay.class) {
                if (INSTANCE == null) {
                    INSTANCE = new ImageDisplay();
                }
            }
        }

        return INSTANCE;
    }

    public class CustomAdapter extends BaseAdapter {
        private ArrayList<String> imageNames;
        private ArrayList<String> imagePhotos;

        private Context context;
        private LayoutInflater layoutInflater;

        private class ViewHolder{
            ImageView imageView;
            CheckBox check;
        }

        public CustomAdapter(ArrayList<String> imageNames, ArrayList<String> imagePhotos, @NonNull Context context) {
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
                viewHolder.check=view.findViewById(R.id.checkImage);
                view.setTag(viewHolder);
            } else {
                viewHolder=(ViewHolder) view.getTag();

            }
            if(isHolding)
            {
                int currentView=i;
                viewHolder.check.setVisibility(View.VISIBLE);

                if(selectedImages.contains(imagePhotos.get(i))) {
                    viewHolder.check.setChecked(true);
                }else
                {
                    viewHolder.check.setChecked(false);
                }

                viewHolder.check.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                        if(compoundButton.isPressed())
                        {
                            if(currentView<imagePhotos.size()) {

                                if (b) {
                                    if (!selectedImages.contains(imagePhotos.get(currentView))) {
                                        selectedImages= ((MainActivity)getContext()).adjustChooseToDeleteInList(imagePhotos.get(currentView),"choose");
                                        //selectedImages.add(imagePhotos.get(currentView));
                                    }

                                } else {
                                    if (selectedImages.contains(imagePhotos.get(currentView))) {
                                        selectedImages=  ((MainActivity)getContext()).adjustChooseToDeleteInList(imagePhotos.get(currentView),"unchoose");

                                        //selectedImages.remove(imagePhotos.get(currentView));
                                    }


                                }
                                ((MainActivity) getContext()).SelectedTextChange();
                                customAdapter.notifyDataSetChanged();
                                listAdapter.notifyDataSetChanged();

                            }
                        }

                    }
                });
            }
            else
            {
                viewHolder.check.setVisibility(View.INVISIBLE);
            }
//            viewHolder.imageView.setImageBitmap(myBitmap);
//            if(isHolding)
//            {
//               //viewHolder.check.setVisibility(View.VISIBLE);
//                viewHolder.check.setChecked(checkPhoto.get(i));
//                viewHolder.imageView.setClickable(true);
//
//            }
//            else
//            {
//               // viewHolder.check.setVisibility(View.INVISIBLE);
//
//            }
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
            CheckBox check;
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
                viewHolder.check=view.findViewById(R.id.checkImage);
                view.setTag(viewHolder);
            } else {
                viewHolder=(ViewHolder) view.getTag();
            }
            TextView tvName = viewHolder.textView;
            tvName.setText(imageNames.get(i));
            if(isHolding)
            {
                int currentView=i;
                viewHolder.check.setVisibility(View.VISIBLE);

                if(selectedImages.contains(imagePhotos.get(i))) {
                    viewHolder.check.setChecked(true);
                }else
                {
                    viewHolder.check.setChecked(false);
                }
                viewHolder.check.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                        if(compoundButton.isPressed())
                        {
                            if(currentView<imagePhotos.size()) {

                                if (b) {
                                    if (!selectedImages.contains(imagePhotos.get(currentView))) {
                                        selectedImages= ((MainActivity)getContext()).adjustChooseToDeleteInList(imagePhotos.get(currentView),"choose");
                                        //selectedImages.add(imagePhotos.get(currentView));
                                    }

                                } else {
                                    if (selectedImages.contains(imagePhotos.get(currentView))) {
                                        selectedImages=  ((MainActivity)getContext()).adjustChooseToDeleteInList(imagePhotos.get(currentView),"unchoose");

                                        //selectedImages.remove(imagePhotos.get(currentView));
                                    }


                                }
                                ((MainActivity) getContext()).SelectedTextChange();
                                customAdapter.notifyDataSetChanged();
                                listAdapter.notifyDataSetChanged();

                            }
                        }

                    }
                });
            }
            else
            {
                viewHolder.check.setVisibility(View.INVISIBLE);
            }
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
        //checkPhoto=new ArrayList<Boolean>(Arrays.asList(new Boolean[images.size()]));
        //Collections.fill(checkPhoto, Boolean.FALSE);

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
                String selectedName = images.get(i);

//                int selectedImage = images[i];
                if (isHolding == false) {
                    startActivity(new Intent(getActivity(), SelectedPicture.class)
                            .putExtra("name", selectedName)
                            .putExtra("images", images)
                            .putExtra("pos", i));
                }
              else {

//                    //view.setFocusable(true);
//                    if(!selectedImages.contains(selectedName))
//                    {
//                      //  selectedImages.add(selectedName) ;
//                        selectedImages= ((MainActivity)getContext()).adjustChooseToDeleteInList(selectedName,"choose");
//                    }
//                    else {
//                       // selectedImages.remove(selectedName) ;
//                        selectedImages= ((MainActivity)getContext()).adjustChooseToDeleteInList(selectedName,"unchoose");
//
//                    }
//                    ((MainActivity)getContext()).SelectedTextChange();
//                    customAdapter.notifyDataSetChanged();
//                    listAdapter.notifyDataSetChanged();
//                }
//                Toast.makeText((MainActivity)getContext(), "choosed ", Toast.LENGTH_SHORT).show();
                }
            }

        });

        gridView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                isHolding =true;
                ((MainActivity)getContext()).Holding(isHolding);

                String selectedName= images.get(i);

                selectedImages= ((MainActivity)getContext()).adjustChooseToDeleteInList(selectedName,"choose");

                ((MainActivity)getContext()).SelectedTextChange();

                customAdapter.notifyDataSetChanged();
                listAdapter.notifyDataSetChanged();

                return true;
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

        fab_url.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showInputDialogBox();
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

    private void showInputDialogBox()
    {
        final String[] url_input = {"",""};
        final Dialog customDialog = new Dialog( getContext());
        customDialog.setTitle("Delete confirm");

        customDialog.setContentView(R.layout.url_download_diagbox);

        ((Button) customDialog.findViewById(R.id.download_url_cancel))
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //donothing
                        customDialog.dismiss();
                    }
                });

        ((Button) customDialog.findViewById(R.id.download_url_confirm))
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        url_input[0] = ((EditText) customDialog.findViewById(R.id.download_url_input)).getText().toString();
                        url_input[1] =((EditText) customDialog.findViewById(R.id.download_url_rename)).getText().toString();
                        Toast.makeText(INSTANCE.getContext(), url_input[0], Toast.LENGTH_SHORT).show();
                        String[] ArrInput=DownloadImageFromURL(url_input[0].trim(),url_input[1].trim());
                        ((MainActivity)getContext()).addImageUpdate(ArrInput);
                        customAdapter.notifyDataSetChanged();
                        listAdapter.notifyDataSetChanged();
                        customDialog.dismiss();
                    }
                });

        customDialog.show();
    }

    private String[] DownloadImageFromURL(String input,String fileName)
    {
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(input));
        String[] result=new String[1];
        String fileExtension=input.substring(input.lastIndexOf("."));
        while ( fileExtension.charAt(fileExtension.length() - 1) == '\n') {
            fileExtension = fileExtension.substring(0, fileExtension.length() - 1);
        }

        if (fileName.length()==0){
            fileName= (new Date()).toString();

        }
        String fullNameFile=((MainActivity)getContext()).getPictureDirectory() + "/" + fileName + "." + fileExtension;
        request.setDescription("Downloading " + input + "...");
        request.setTitle(input);
       // request.allowScanningByMediaScanner();
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setDestinationUri(Uri.fromFile(new File(fullNameFile)));
        DownloadManager manager = (DownloadManager) INSTANCE.getActivity().getSystemService(Context.DOWNLOAD_SERVICE);
        manager.enqueue(request);
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE);
        Notification noti = new NotificationCompat.Builder((MainActivity)getContext(),"Download " +fullNameFile )
                .setContentText("Downloaded item")
                .setSmallIcon(R.drawable.ic_launcher_background)

                .build();



        result[0]=fullNameFile;
        return result;
    }


    @Override
    public  void deleteClicked()
    {

        isHolding =false;
        ((MainActivity)getContext()).Holding(isHolding);
        selectedImages = ((MainActivity)getContext()).chooseToDeleteInList();
        customAdapter.notifyDataSetChanged();
        listAdapter.notifyDataSetChanged();
    }

    @Override
    public void  clearClicked()
    {
        isHolding =false;
        ((MainActivity)getContext()).Holding(isHolding);
       // Collections.fill(checkPhoto,Boolean.FALSE);

        customAdapter.notifyDataSetChanged();
        listAdapter.notifyDataSetChanged();
    }
    @Override
    public void  selectAllClicked()
    {
        isHolding =true;
        ((MainActivity)getContext()).Holding(isHolding);
//        if(selectedImages.size()==images.size())
//        {
//            //selectedImages.clear();
//           // Collections.fill(checkPhoto,Boolean.FALSE);
//
//        }
//        else
//        {
//            //Collections.fill(checkPhoto,Boolean.TRUE);
//
//        }
//        else
//        {
//            selectedImages.clear();
//            for(String name:images)
//            {
//                selectedImages.add(name);
//            }
//
//        }
        selectedImages= ((MainActivity)getContext()).chooseToDeleteInList();
        ((MainActivity)getContext()).SelectedTextChange();
        customAdapter.notifyDataSetChanged();
        listAdapter.notifyDataSetChanged();
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