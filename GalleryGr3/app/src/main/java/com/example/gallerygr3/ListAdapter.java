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
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.example.gallerygr3.R;

import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.util.ArrayList;

public class ListAdapter extends BaseAdapter {
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
        ListAdapter.ViewHolder finalViewHolder = viewHolder;

        Glide.with(context)
                .asBitmap()
                .load(imgFile.getAbsolutePath())
                .apply(new RequestOptions().placeholder(R.drawable.ic_baseline_arrow_back_24))
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