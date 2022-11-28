package com.example.gallerygr3;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class EditTransformFragment extends Fragment {
    EditImage main;
    Context context=null;
    String message="";

    ImageButton verticalBtn;
    ImageButton horizontalBtn;
    ImageButton backBtn;


    public static EditTransformFragment newInstance()
    {
        EditTransformFragment fragment= new EditTransformFragment();
//        Bundle args = new Bundle();
//        args.putString("strArg1", strArg);
//        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            context = getContext(); // use this reference to invoke main callbacks
            main = (EditImage) getActivity();
        }
        catch (IllegalStateException e) {
            throw new IllegalStateException("MainActivity must implement callbacks");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        LinearLayout layout = (LinearLayout) inflater.inflate(R.layout.edit_transform_fragment,null);

        verticalBtn = (ImageButton) layout.findViewById(R.id.vertical_flip);
        verticalBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                main.TransformVertical();

            }
        });

        horizontalBtn = (ImageButton) layout.findViewById(R.id.horizontal_flip);
        horizontalBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                main.TransformHorizontal();
            }
        });

        backBtn =(ImageButton) layout.findViewById(R.id.back_edit_btn);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                main.BackFragment();
            }
        });

        return layout;
    }
}
