package com.example.gallerygr3;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.util.ArrayList;

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ViewHolder> {

    Context context;
    ArrayList<String> Images;

    public ImageAdapter(Context context,ArrayList<String> Images)
    {

        this.context=context;
        this.Images=Images;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.picture_view,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        int getPositionFolderName= Images.get(position).lastIndexOf("/");
        String name= Images.get(position).substring(getPositionFolderName + 1);
       holder.imageText.setText(name);

        File imgFile= new File(Images.get(position));
        Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());

        holder.imageItem.setImageBitmap(myBitmap);

        holder.imageItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int pos =holder.getAdapterPosition();

                Toast.makeText((MainActivity)context,"Image Choose: " +name,Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public int getItemCount() {
        return Images.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final ImageView imageItem;
        private TextView imageText;

        public ViewHolder(@NonNull View itemView)
        {
            super(itemView);
            imageItem=(ImageView) itemView.findViewById(R.id.fileImage);
            imageText=(TextView) itemView.findViewById(R.id.imageText);
        }
    }

}


