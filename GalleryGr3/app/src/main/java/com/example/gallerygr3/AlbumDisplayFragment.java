package com.example.gallerygr3;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.File;
import java.io.IOException;
import java.nio.file.CopyOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.zip.Inflater;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AlbumDisplayFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AlbumDisplayFragment extends Fragment {
    Context context;
    ImageButton back_button;
    TextView album_name;
    RecyclerView listView;
    Album album;
    FloatingActionButton add_images;
    ArrayList<String> addedPaths=new ArrayList<String>();
    int spanColumns=3;

    public AlbumDisplayFragment() {
        // Required empty public constructor
    }


    public static AlbumDisplayFragment newInstance(Album album) {
        AlbumDisplayFragment fragment = new AlbumDisplayFragment();
        fragment.album=album;
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            // get args
        }
        context=getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        CoordinatorLayout layout= (CoordinatorLayout) inflater.inflate(R.layout.fragment_album_display,container,false);
        back_button=layout.findViewById(R.id.album_display_back);
        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity)context).getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container,AlbumsFragment.class,null)
                        .setReorderingAllowed(true)
                        .commit();
            }
        });

        album_name=layout.findViewById(R.id.album_display_name);
        album_name.setText(album.name);

        listView=layout.findViewById(R.id.album_display_list);
        listView.setAdapter( new AlbumDisplayAdapter(album.imagePaths));
        listView.setLayoutManager(new GridLayoutManager(context,spanColumns));
        listView.addItemDecoration(new SpacesItemDecoration(20, spanColumns));

        add_images=layout.findViewById(R.id.add_image);
        add_images.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ImageChoosingDialog dialog=new ImageChoosingDialog(context);
                dialog.show();
            }
        });

        return layout;
    }

    private class AlbumDisplayAdapter extends RecyclerView.Adapter<AlbumDisplayAdapter.ViewHolder>{
        ArrayList<String> albumPaths;

        public AlbumDisplayAdapter(ArrayList<String> albumPaths){
            this.albumPaths=albumPaths;
        }
        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view=LayoutInflater.from(parent.getContext()).inflate(R.layout.row_item,parent,false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            File imgFile = new File(albumPaths.get(position));
            Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
            holder.imageView.setImageBitmap(myBitmap);
        }

        @Override
        public int getItemCount() {
            return albumPaths.size();
        }


        private class ViewHolder extends RecyclerView.ViewHolder{
            ImageView imageView;
            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                imageView=itemView.findViewById(R.id.imageView);
            }
        }
    }
    private class ImageChoosingDialog extends Dialog{
        public ImageChoosingDialog(@NonNull Context context) {
            super(context);
            addedPaths.clear();
            RelativeLayout layout= (RelativeLayout) getLayoutInflater().inflate(R.layout.image_choosing,null);

            RecyclerView imageList=layout.findViewById(R.id.image_choosing_imageList);
            imageList.setAdapter(new ImageChoosingAdapter(((MainActivity)context).getFileinDir()));
            imageList.setLayoutManager(new GridLayoutManager(context,spanColumns));
            imageList.addItemDecoration(new SpacesItemDecoration(20, spanColumns));

            Button add_btn=layout.findViewById(R.id.image_choosing_add);
            add_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dismiss();
                    MoveOrCopy dialog=new MoveOrCopy(context);
                    dialog.show();
                }
            });

            Button cancel_btn=layout.findViewById(R.id.image_choosing_cancel);
            cancel_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dismiss();
                }
            });

            setContentView(layout);
            WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
            layoutParams.copyFrom(getWindow().getAttributes());
            layoutParams.width = (int) (WindowManager.LayoutParams.MATCH_PARENT);
            layoutParams.height = (int) (WindowManager.LayoutParams.MATCH_PARENT);
            getWindow().setAttributes(layoutParams);
        }
        private class ImageChoosingAdapter extends RecyclerView.Adapter<ImageChoosingAdapter.ViewHolder>{
            ArrayList<String> allImagePaths;
            public ImageChoosingAdapter(ArrayList<String> allImagePaths){
                this.allImagePaths=allImagePaths;
            }
            @NonNull
            @Override
            public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view=LayoutInflater.from(parent.getContext()).inflate(R.layout.row_item_with_choose,parent,false);
                return new ViewHolder(view);
            }

            @Override
            public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
                File imgFile= new File(allImagePaths.get(position));
                Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                holder.imageView.setImageBitmap(myBitmap);
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int pos=holder.getAdapterPosition();
                        if(holder.checkBox.isChecked()){
                            holder.checkBox.setChecked(false);
                            addedPaths.remove(allImagePaths.get(pos));
                        } else {
                            holder.checkBox.setChecked(true);
                            addedPaths.add(allImagePaths.get(pos));
                        }
                    }
                });
            }

            @Override
            public int getItemCount() {
                return allImagePaths.size();
            }

            private class ViewHolder extends RecyclerView.ViewHolder{
                ImageView imageView;
                CheckBox checkBox;
                public ViewHolder(@NonNull View itemView) {
                    super(itemView);
                    imageView=itemView.findViewById(R.id.image_to_choose);
                    checkBox=itemView.findViewById(R.id.image_check_box);
                }
            }
        }
    }
    private class MoveOrCopy extends BottomSheetDialog{

        public MoveOrCopy(@NonNull Context context) {
            super(context);
            LinearLayout layout= (LinearLayout) getLayoutInflater().inflate(R.layout.move_or_copy_choosing,null);

            Button move_btn=layout.findViewById(R.id.move_option);
            move_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String folderPath=((MainActivity) context).Picture + AlbumsFragment.folderPath+"/"+album.name;
                    for (int i=0;i < addedPaths.size();i++){
                        String newFileName=moveFile(addedPaths.get(i),folderPath);
                        album.imagePaths.add(folderPath+"/"+newFileName);
                        listView.getAdapter().notifyItemChanged(album.imagePaths.size()-1);
                    }
                    dismiss();
                }
            });

            Button copy_btn=layout.findViewById(R.id.copy_option);
            copy_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String folderPath=((MainActivity) context).Picture + AlbumsFragment.folderPath+"/"+album.name;
                    int from=album.imagePaths.size();
                    int to=from+addedPaths.size()-1;
                    for (int i=0;i < addedPaths.size();i++){
                        String newFileName=copyFile(addedPaths.get(i),folderPath);
                        album.imagePaths.add(folderPath+"/"+newFileName);
                    }
                    if(from <= to){
                        listView.getAdapter().notifyItemRangeChanged(from,to);
                    }
                    dismiss();
                }
            });

            setContentView(layout);
        }
    }

    public String moveFile(String filePath, String newFolderLocation){
        Path from= Paths.get(filePath);
        String newFileName=ImageDisplay.generateFileName()+"."+getExtension(from.getFileName().toString());
        Path to=Paths.get(newFolderLocation+"/"+newFileName);
        try {
            Files.move(from,to,StandardCopyOption.REPLACE_EXISTING);
            return newFileName;
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }
    public String copyFile(String filePath,String newFolderLocation){
        Path from= Paths.get(filePath);
        String newFileName=ImageDisplay.generateFileName()+"."+getExtension(from.getFileName().toString());
        Path to=Paths.get(newFolderLocation+"/"+newFileName);
        try {
            Files.copy(from,to,StandardCopyOption.REPLACE_EXISTING);
            Toast.makeText(context,"copy "+newFileName,Toast.LENGTH_SHORT).show();
            return newFileName;
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }
    public String getExtension(String file){
        String[] splits=file.split("\\.");
        return splits[1];
    }
}