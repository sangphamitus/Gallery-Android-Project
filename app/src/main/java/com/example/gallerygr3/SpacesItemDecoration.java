package com.example.gallerygr3;

import android.graphics.Canvas;
import android.graphics.Rect;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class SpacesItemDecoration extends RecyclerView.ItemDecoration {
    private int space,spanColumns;
    private boolean isUsed;
    public SpacesItemDecoration(int space,int spanColumns) {
        this.spanColumns=spanColumns;
        this.space = space;
        isUsed=true;
    }
    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        outRect.right = space;
        outRect.bottom = space;
        // Add top margin only for the first item to avoid double space between items

        if (parent.getChildLayoutPosition(view) < spanColumns) {
            outRect.top = space;
        }
        if(parent.getChildLayoutPosition(view) % spanColumns ==0){
            outRect.left=space;
        }
    }

    public void setSpanColumns(int columns){
        this.spanColumns=columns;
    }
    public void setUsed(boolean used){
        this.isUsed=used;
    }

    @Override
    public void onDraw(@NonNull Canvas c, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        if(isUsed) {super.onDraw(c, parent, state);}
    }

    @Override
    public void onDrawOver(@NonNull Canvas c, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        if(isUsed) {super.onDrawOver(c, parent, state);}
    }

}
