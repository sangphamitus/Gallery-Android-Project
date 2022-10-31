package com.example.gallerygr3;

import android.graphics.Rect;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

public class SpacesItemDecoration extends RecyclerView.ItemDecoration {
    private int space,spanColumns;
    public SpacesItemDecoration(int space,int spanColumns) {
        this.spanColumns=spanColumns;
        this.space = space;
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
}
