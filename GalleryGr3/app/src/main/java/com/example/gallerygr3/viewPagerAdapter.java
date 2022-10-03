package com.example.gallerygr3;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class viewPagerAdapter extends RecyclerView.Adapter<viewPagerAdapter.ViewHolder> {
    ArrayList<viewPagerItem> arrayItems;

    public viewPagerAdapter(ArrayList<viewPagerItem> arrayItems) {
        this.arrayItems = arrayItems;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.full_creen_picture,parent,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        viewPagerItem item= arrayItems.get(position);
        holder.img.setImageBitmap(item.getItemBitmap());
        holder.txtName.setText(item.getSelectedName());
    }

    @Override
    public int getItemCount() {
        return arrayItems.size();
    }

    public class ViewHolder  extends RecyclerView.ViewHolder{
        ImageView img;
        TextView txtName;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            img=itemView.findViewById(R.id.imageView);
            txtName=itemView.findViewById(R.id.tvName);

        }
    }
}
