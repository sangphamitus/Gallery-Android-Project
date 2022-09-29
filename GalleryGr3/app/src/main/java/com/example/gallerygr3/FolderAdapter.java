package com.example.gallerygr3;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;



import java.util.ArrayList;

public class FolderAdapter extends RecyclerView.Adapter<FolderAdapter.ViewHolder> {

    Context context;
    ArrayList<String> Dirs;
    PhotosFragment mCallbacks;


    public FolderAdapter(Context context,ArrayList<String> Dirs,PhotosFragment mCallbacks)
    {
        this.context=context;
        this.Dirs=Dirs;
        this.mCallbacks=mCallbacks;

    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.folder_view,parent,false);
       return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        int getPositionFolderName=  Dirs.get(position).lastIndexOf("/");
        String name= Dirs.get(position).substring(getPositionFolderName + 1);
        holder.textFolder.setText(name);

        holder.folderItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int pos =holder.getAdapterPosition();

                ((MainActivity)context).setCurrentDirectory(Dirs.get(pos));
                mCallbacks.setTitleFolder();
            }
        });

    }

    @Override
    public int getItemCount() {
        return Dirs.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final ImageView folderItem;
        private TextView textFolder;
        public ViewHolder(@NonNull View itemView)
        {
            super(itemView);
            folderItem=(ImageView) itemView.findViewById(R.id.folderImage);
            textFolder=(TextView) itemView.findViewById(R.id.folderText);
        }
    }
}
