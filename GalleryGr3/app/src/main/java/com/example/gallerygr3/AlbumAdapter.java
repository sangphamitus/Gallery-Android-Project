package com.example.gallerygr3;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class AlbumAdapter extends RecyclerView.Adapter<AlbumAdapter.ViewHolder> {
    Context context;
    ArrayList<Album> albumList;

    public AlbumAdapter(ArrayList<Album> albumList,Context context){
        this.context=context;
        this.albumList=albumList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.row_album,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.albumName.setText(albumList.get(position).name);

        holder.albumImagesCount.setText(String.format(context.getString(R.string.album_image_count),albumList.get(position).imagePaths.size()));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int pos = holder.getAdapterPosition();
                ((MainActivity)context).getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container,AlbumDisplayFragment.newInstance(albumList.get(pos)),null)
                        .setReorderingAllowed(true)
                        .commit();
            }
        });
    }

    @Override
    public int getItemCount() {
        return albumList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView imageView;
        TextView albumName;
        TextView albumImagesCount;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView=itemView.findViewById(R.id.album_image);
            imageView.setImageResource(R.drawable.ic_baseline_folder_24);
            albumName =itemView.findViewById(R.id.album_name);
            albumImagesCount=itemView.findViewById(R.id.album_images_count);
        }
    }
}
