package com.example.gallerygr3;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;

import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.util.ArrayList;

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
        ViewHolder finalViewHolder = viewHolder;
        Glide.with(context)
                .asBitmap()
                .load(imgFile.getAbsolutePath())
                .apply(new RequestOptions().placeholder(R.drawable.avatar4))
                .into(new CustomTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                        finalViewHolder.imageView.setImageBitmap(resource);
                    }
                    @Override
                    public void onLoadCleared(@Nullable Drawable placeholder) {
                    }
                });
        return view;
    }

}