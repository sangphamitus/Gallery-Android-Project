package com.example.gallerygr3;


import androidx.appcompat.app.AppCompatActivity;


import android.content.Context;
import android.graphics.drawable.ColorDrawable;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;

import android.os.Build;

import android.app.Dialog;

import android.content.Intent;

import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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
import androidx.core.content.FileProvider;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.fragment.app.Fragment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;

// reviewed code for task: https://ppsang.atlassian.net/browse/A2G-14
public class MainActivity extends AppCompatActivity  implements MainCallBack {

    String currentDirectory=null;

    String SD;
    String DCIM;
    String Picture;
    ArrayList<String> folderPaths=new ArrayList<String>();
    public ArrayList<String> FileInPaths=new ArrayList<String>();
    HashMap<Integer,Bitmap> hashMap= new HashMap<Integer, Bitmap>();
//    PhotosFragment photo;

    LinearLayout navbar;
    RelativeLayout chooseNavbar;
    RelativeLayout status;

    MainActivity context;
    FloatingActionButton deleteBtn;
    FloatingActionButton cancelBtn;
    FloatingActionButton selectAll;
    TextView informationSelected;

    FloatingActionButton createSliderBtn;
    FloatingActionButton shareMultipleBtn;
    FloatingActionButton addToAlbumBtn;


    public static String[] ImageExtensions = new String[] {
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

    Fragment[] arrFrag = new Fragment[3];


    public void askForPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            if (!Environment.isExternalStorageManager()) {
                Intent intent = new Intent(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION);
                startActivity(intent);
                return;
            }
        }
    }

    String deleteNotify="";

    public ArrayList<String> chooseToDeleteInList=new ArrayList<String>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
                .delayBeforeLoading(0)
                .resetViewBeforeLoading(true)
                .showImageOnLoading(R.drawable.placehoder)
                .showImageForEmptyUri(R.drawable.error_image)
                .showImageOnFail(R.drawable.error_image)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .build();
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this)
                .defaultDisplayImageOptions(defaultOptions)
                .build();
        ImageLoader.getInstance().init(config);



        context= this;

        ActivityCompat.requestPermissions(MainActivity.this,
                new String[]{
                        Manifest.permission.CAMERA,
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.INTERNET
                }, 1);




      //  SD = Environment.getExternalStorageDirectory().getAbsolutePath();
        DCIM = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).getAbsolutePath();
        Picture= Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath();

     //   arrFrag[0] = ImageDisplay.getInstance();
        arrFrag[1] = AlbumsFragment.getInstance();
        arrFrag[2] = new SettingsFragment();

        arrRoundLayout[0] = R.drawable.round_photos;
        arrRoundLayout[1] = R.drawable.round_albums;
        arrRoundLayout[2] = R.drawable.round_settings;

        navbar = (LinearLayout) findViewById(R.id.navbar);
        chooseNavbar =(RelativeLayout) findViewById(R.id.selectNavbar);
        status= (RelativeLayout) findViewById(R.id.status);

        deleteBtn=(FloatingActionButton) findViewById(R.id.deleteImageButton);
        cancelBtn=(FloatingActionButton) findViewById(R.id.clear);
        selectAll=(FloatingActionButton) findViewById(R.id.selectAll);
        createSliderBtn=(FloatingActionButton)findViewById(R.id.createSliderBtn);
        shareMultipleBtn=(FloatingActionButton)findViewById(R.id.shareMultipleBtn);
        addToAlbumBtn=(FloatingActionButton)findViewById(R.id.addToAlbumBtn);
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
                ImageDisplay ic= ImageDisplay.getInstance();
                clearChooseToDeleteInList();
                ic.clearClicked();
            }
        });
        selectAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               ImageDisplay ic= ImageDisplay.getInstance();
               if(chooseToDeleteInList.size()==ic.images.size())
               {
                   chooseToDeleteInList.clear();

               }
               else
               {
                   chooseToDeleteInList=new ArrayList<String >(ic.images);

               }
                ic.selectAllClicked();
            }
        });

        createSliderBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, "create sliderrrrr", Toast.LENGTH_SHORT).show();
                showSliderDiaglogBox();
            }
        });

        shareMultipleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, "Share multiple", Toast.LENGTH_SHORT).show();
                String[] select = chooseToDeleteInList.toArray
                        (new String[chooseToDeleteInList.size()]);
                ArrayList<String> paths= new ArrayList<String>();

                for(int i=0;i< select.length;i++){
                    paths.add(select[i]);
                }
                shareImages(paths);
            }
        });

        addToAlbumBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlbumChoosingDialog dialog=new AlbumChoosingDialog(context);
                dialog.show();
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


    @Override
    public void shareImages(ArrayList<String> paths){

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

                Uri uri = FileProvider.getUriForFile(this,
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




    private void showSliderDiaglogBox(){
        final Dialog customDialog = new Dialog( context );
        customDialog.setTitle("Create Slider with Music");
        customDialog.setContentView(R.layout.slider_diaglog_notify);
        customDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        ((ImageButton) customDialog.findViewById(R.id.cancelSlider))
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        customDialog.dismiss();
                    }
                });

        ((ImageButton) customDialog.findViewById(R.id.comfirmSlider))
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        RadioGroup radio= (RadioGroup) customDialog.findViewById(R.id.musicGroup);

                        int id = radio.getCheckedRadioButtonId();
                        RadioButton selectedRadionBtn= (RadioButton)customDialog.findViewById(id);
                        String name=selectedRadionBtn.getText().toString();

                        customDialog.dismiss();// ẩn diaglogbox

                        String[] select = chooseToDeleteInList.toArray
                                (new String[chooseToDeleteInList.size()]);

                        Intent intent = new Intent(context,SliderMusic.class)
                                .putExtra("images",select)
                                .putExtra("music", name);

                        startActivity(intent);
                    }
                });
        customDialog.show();

    }



    private void showCustomDialogBox()
    {
        final Dialog customDialog = new Dialog( context );
        customDialog.setTitle("Delete confirm");

        customDialog.setContentView(R.layout.delete_dialog_notify);
        customDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        ((TextView) customDialog.findViewById(R.id.deleteNotify))
                .setText("Do you want to delete "+deleteNotify+" image(s) permanently in your device ?");

        ((ImageButton) customDialog.findViewById(R.id.cancel_delete))
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                      //donothing
                        customDialog.dismiss();
                    }
                });

        ((ImageButton) customDialog.findViewById(R.id.confirmDelete))
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        ImageDisplay ic= ImageDisplay.getInstance();
                        String[] select = chooseToDeleteInList.toArray
                                (new String[chooseToDeleteInList.size()]);
                        String temp=select[0];
                        // String[] select= (String[]) selectedImages.toArray();
                        ImageDelete.DeleteImage(select);
                        removeImageUpdate(select);
                        clearChooseToDeleteInList(); // ??
                        ic.deleteClicked(); // xoá clicked
                        customDialog.dismiss();// ẩn diaglogbox
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

                askForPermissions();

                readFolder(Picture);
                readFolder(DCIM);

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
            ImageDisplay.getInstance().removeImage(name);
        }

    }

    @Override
    public void removeImageUpdate(String input)
    {
        FileInPaths.remove(input);
    }

    @Override
    public void renameImageUpdate(String oldName, String newName)
    {
        changeFileInFolder(Picture, oldName, newName);
        changeFileInFolder(DCIM, oldName, newName);
        //changeFileInFolder();
    }

    @Override
    public void addImageUpdate(String[] input)
    {
        for (String name:input)
        {
            filterImage(name);

        }

    }

    @Override
    public void Holding(boolean isHolding)
    {
        ImageDisplay instance=ImageDisplay.getInstance() ;
        if(isHolding)
        {
            chooseNavbar.setVisibility(View.VISIBLE);
            navbar.setVisibility(View.INVISIBLE);
            status.setVisibility(View.VISIBLE);
            if(instance.callback != null){instance.callback.onLongClick();}
        }
        else {
            chooseNavbar.setVisibility(View.INVISIBLE);
            navbar.setVisibility(View.VISIBLE);
            status.setVisibility(View.INVISIBLE);

            if(instance.callback !=null){instance.callback.afterLongClick();}


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


    public void filterImage(String name){
        Bitmap test= BitmapFactory.decodeFile(name);
        boolean have= false;
        int HashCode= ImageDelete.hashBitmap(test);
        if(!hashMap.containsKey(HashCode))
        {
            FileInPaths.add(name);
            hashMap.put(HashCode,test);
        }
        else {
            ImageDelete.DeleteImage(name);
        }
    }


    private void readFolder(String Dir) {


        File sdFile = new File(Dir);

        File[] foldersSD = sdFile.listFiles();

        try {
            for (File file : foldersSD) {
                if (file.isDirectory()) {
                    if(file.getName().equals("Albums"))
                    {
                        continue;
                    }
                    //get absolute
                    //do nothing
                    readFolder(file.getAbsolutePath());

                } else {
                    for (String extension : ImageExtensions) {
                        if (file.getAbsolutePath().toLowerCase().endsWith(extension)) {
                            // addImageView(file.getAbsolutePath());
                            if(!FileInPaths.contains(file.getAbsolutePath()))
                                //FileInPaths.add(file.getAbsolutePath());
                               filterImage(file.getAbsolutePath());

                            break;
                        }

                    }
                }
            }

        } catch (Exception e) {
            //do nothing
        }

    }

    private void changeFileInFolder(String Dir, String oldName, String newName) {

        File sdFile = new File(Dir);
        File[] foldersSD = sdFile.listFiles();

        try {
            for (File file : foldersSD) {
                if (file.isDirectory()) {
                    if(file.getName().equals("Albums"))
                    {
                        continue;
                    }
                    //get absolute
                    //do nothing
                    changeFileInFolder(file.getAbsolutePath(), oldName,newName);

                } else {
                    for (String extension : ImageExtensions) {

                        if(file.getAbsolutePath().toLowerCase().endsWith(extension)){
                      //      filterImage();
                            if (oldName.equals(file.getName())) {
                                File from = new File(Dir+"/"+oldName);
                                File to = new File(Dir+"/"+newName);
                                from.renameTo(to);
                                FileInPaths.remove(Dir+"/"+oldName);
                                FileInPaths.add(Dir+"/"+newName);
                                Toast.makeText(this, "doi xong", Toast.LENGTH_SHORT).show();

                                ImageDisplay ic= ImageDisplay.getInstance();
                                ic.notifyChangeGridLayout();
                                break;

                            }
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
    public void readAgain()
    {

        readFolder(Picture);
        readFolder(DCIM);
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
                    ImageDisplay.restoreINSTANCE();
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

    private class AlbumChoosingDialog extends Dialog{


        public AlbumChoosingDialog(@NonNull Context context) {
            super(context);
            RelativeLayout layout= (RelativeLayout) getLayoutInflater().inflate(R.layout.album_choosing,null);

            ImageButton backBtn=layout.findViewById(R.id.album_choosing_back);
            backBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dismiss();
                }
            });

            GridView imageList=layout.findViewById(R.id.album_choosing_list);
            imageList.setAdapter(new AlbumChoosingAdapter());

            imageList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    Drawable buttonDrawable = view.getBackground();
                    buttonDrawable = DrawableCompat.wrap(buttonDrawable);
                    //the color is a direct color int and not a color resource
                    DrawableCompat.setTint(buttonDrawable, context.getResources().getColor(R.color.fullScreenBtn));
                    view.setBackground(buttonDrawable);
                    MoveOrCopy dialog=new MoveOrCopy(context, new MoveOrCopy.MoveOrCopyCallBack() {
                        @Override
                        public void dismissCallback(String method) {
                            view.setBackgroundTintList(null);
                            TextView imgCount= view.findViewById(R.id.album_images_count);
                            imgCount.setText(String.format(context.getString(R.string.album_image_count),AlbumsFragment.albumList.get(i).imagePaths.size()));
                            if(method.equals("remove"))
                            {
                                ImageDisplay ic= ImageDisplay.getInstance();
                                clearChooseToDeleteInList();
                                ic.clearClicked();
                                dismiss();
                            }
                        }

                        @Override
                        public void copiedCallback(String newImagePath) {
                            AlbumsFragment.albumList.get(i).imagePaths.add(newImagePath);
                        }

                        @Override
                        public void removedCallback(String oldImagePath, String newImagePath) {
                            ImageDisplay.getInstance().removeImage(oldImagePath);
                            AlbumsFragment.albumList.get(i).imagePaths.add(newImagePath);
                        }


                    },AlbumsFragment.albumList.get(i), chooseToDeleteInList());
                    dialog.show();
                }
            });

            setContentView(layout);
            WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
            layoutParams.copyFrom(getWindow().getAttributes());
            layoutParams.width = (int) (WindowManager.LayoutParams.MATCH_PARENT);
            layoutParams.height = (int) (WindowManager.LayoutParams.MATCH_PARENT);
            getWindow().setAttributes(layoutParams);
        }
        private class AlbumChoosingAdapter extends BaseAdapter {
            ArrayList<Album> albumList;
            public AlbumChoosingAdapter(){
                this.albumList =AlbumsFragment.albumList;
            }

            @Override
            public int getCount() {
                return albumList.size();
            }

            @Override
            public Object getItem(int i) {
                return albumList.get(i);
            }

            @Override
            public long getItemId(int i) {
                return i;
            }

            @Override
            public View getView(int i, View view, ViewGroup viewGroup) {
                AlbumChoosingAdapter.ViewHolder viewHolder=null;
//            Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                if(view == null){
                    view =getLayoutInflater().inflate(R.layout.row_album,viewGroup,false);
                    viewHolder=new AlbumChoosingAdapter.ViewHolder();
                    viewHolder.albumName=view.findViewById(R.id.album_name);
                    viewHolder.albumImageCount=view.findViewById(R.id.album_images_count);
                    viewHolder.imageView=view.findViewById(R.id.album_image);
                    view.setTag(viewHolder);
                } else {
                    viewHolder=(AlbumChoosingAdapter.ViewHolder) view.getTag();
                }
                viewHolder.albumName.setText(albumList.get(i).name);
                viewHolder.albumImageCount.setText(String.format(context.getString(R.string.album_image_count),albumList.get(i).imagePaths.size()));
                view.setBackgroundTintList(null);
                if(albumList.get(i).name.equals(AlbumsFragment.favourite))
                {
                    viewHolder.imageView.setImageResource(R.drawable.heart);
                    view.setBackgroundResource(R.drawable.custom_row_album_favorite);
                }else{
                    viewHolder.imageView.setImageResource(R.drawable.ic_baseline_folder_24);
                    view.setBackgroundResource(R.drawable.custom_row_album);


                }
                return view;
            }

            private class ViewHolder{
                TextView albumName;
                TextView albumImageCount;
                ImageView imageView;
            }
        }
    }
}