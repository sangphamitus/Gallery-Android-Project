package com.example.gallerygr3;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.nostra13.universalimageloader.core.ImageLoader;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.app.DownloadManager;
import android.app.Notification;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;

import android.graphics.Matrix;

import android.media.ExifInterface;
import android.net.Uri;
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
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;


import java.io.FileOutputStream;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.io.File;
import java.util.Collections;
import java.util.Date;

/*
* File imgFile= new File(Images.get(position));
        Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
        holder.imageItem.setImageBitmap(myBitmap);
*
* */



public class ImageDisplay extends Fragment implements chooseAndDelete{
    Context context;

    private static ImageDisplay INSTANCE = null;
    private static ImageDisplay MAIN_INSTANCE=null;


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

    ArrayList<ImageDate> imgDates;
    ArrayList<String> dates;
    ArrayList<Integer> size;

    TableLayout header;
    LongClickCallback callback=null;


    boolean isHolding=false;
    public static boolean isMain=true;

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
    public static void changeINSTANCE(){
        if(isMain)
        {
            MAIN_INSTANCE=INSTANCE;
            isMain=false;
            INSTANCE=null;
        }
    }
    public static void restoreINSTANCE(){
        if(!isMain){
            INSTANCE=MAIN_INSTANCE;
            isMain=true;
        }
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
                                notifyChangeGridLayout();

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

    public interface LongClickCallback {
        void onLongClick();
        void afterLongClick();
    }
    public void setLongClickCallBack(LongClickCallback callback){
        this.callback=callback;
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
                                notifyChangeGridLayout();

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


        this.context= getActivity();

        if(images == null) {setImagesData (((MainActivity)context).getFileinDir());}

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //
        Toast.makeText(getContext(),"ImageDisplay oncreatview",Toast.LENGTH_SHORT).show();
        // Get images
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

        } else {
            customAdapter.notifyDataSetChanged();
        }
        if(listAdapter==null)
        {
            listAdapter = new ImageDisplay.ListAdapter(names,images,getActivity());
        } else {
            listAdapter.notifyDataSetChanged();
        }

        gridView.setAdapter(customAdapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

//                int selectedImage = images[i];
                if (isHolding == false) {
                    startActivity(new Intent(getActivity(), SelectedPicture.class)
                            .putExtra("size", size)//này là em bỏ qua cái selected nè
                            .putExtra("images", images)
                            .putExtra("dates", dates)
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
//                    notifyChangeGridLayout()
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

                notifyChangeGridLayout();

                if(callback != null){callback.onLongClick();}

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

        header=(TableLayout) view.findViewById(R.id.header);

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
                        notifyChangeGridLayout();
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
        notifyChangeGridLayout();
    }
    @Override
    public void deleteClicked(String file)
    {
        ((MainActivity)getContext()).removeImageUpdate(file);
        notifyChangeGridLayout();
    }
    @Override
    public void renameClicked(String file, String newFile)
    {
        ((MainActivity)getContext()).renameImageUpdate(file, newFile);
    }

    @Override
    public void  clearClicked()
    {
        isHolding =false;
        ((MainActivity)getContext()).Holding(isHolding);
       // Collections.fill(checkPhoto,Boolean.FALSE);


        notifyChangeGridLayout();

        customAdapter.notifyDataSetChanged();
        listAdapter.notifyDataSetChanged();

        if(callback !=null){callback.afterLongClick();}
    }

    public void notifyChanged()
    {
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
        notifyChangeGridLayout();
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
                        imageShoot=ImageUltility.rotateImage(imageShoot,90);
                        ImageDelete.saveImage(imageShoot,namePictureShoot);

                        images.add(namePictureShoot);
                        names.add(getDisplayName(namePictureShoot));

                        notifyChangeGridLayout();


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
    public static String getDisplayName(String path){
        int getPositionFolderName= path.lastIndexOf("/");
        String name= path.substring(getPositionFolderName + 1);

        return name;
    }

    public void notifyChangeGridLayout(){
        customAdapter.notifyDataSetChanged();
        listAdapter.notifyDataSetChanged();
    }


    public void setImagesData(ArrayList<String> images) {
        this.images = images;
        //get date
        ArrayList<Date> listDate = new ArrayList<Date>();
        size = new ArrayList<Integer>(this.images.size());
        for (int i = 0; i < this.images.size(); i++) {
            File file = new File(this.images.get(i));
            if (file.exists()) //Extra check, Just to validate the given path
            {
                ExifInterface intf = null;
                try {
                    intf = new ExifInterface(this.images.get(i));
                    if (intf != null) {
                        String dateString = intf.getAttribute(ExifInterface.TAG_DATETIME_ORIGINAL);
                        Date lastModDate = new Date(file.lastModified());
                        size.add(((Number) file.length()).intValue());
                        listDate.add(lastModDate);
                        Log.i("PHOTO DATE", "Dated : " + dateString); //Display dateString. You can do/use it your own way
                    }
                } catch (IOException e) {
                }
                if (intf == null) {
                    Date lastModDate = new Date(file.lastModified());
                    Log.i("PHOTO DATE", "Dated : " + lastModDate.toString());//Dispaly lastModDate. You can do/use it your own way
                }
            }
        }
        imgDates = new ArrayList<ImageDate>();
        dates = new ArrayList<String>();
        //get object
        for (int i = 0; i < this.images.size(); i++) {
            ImageDate temp = new ImageDate(this.images.get(i), listDate.get(i));
            imgDates.add(temp);
            dates.add(temp.dayToString());
        }

        //sort obj
        Collections.sort(imgDates);
        Collections.reverse(imgDates);

        //change images after sort
        for (int i = 0; i < imgDates.size(); i++) {
            this.images.set(i, imgDates.get(i).getImage());

        }

//        Collections.sort(images);
        //checkPhoto=new ArrayList<Boolean>(Arrays.asList(new Boolean[images.size()]));
        //Collections.fill(checkPhoto, Boolean.FALSE);

        //create name array
        names = new ArrayList<String>();

        for (int i = 0; i < this.images.size(); i++) {

            // get name from file ===================================
// t biet ly do roi
            String temp = this.images.get(i);
            String name = getDisplayName(this.images.get(i));
            // ba chaams thi van dc ma
// nhma problem la no ko hien len co 3 cham nhu z
            names.add(name);//thấy cái names không
            // ====================================================
        }
    }
}