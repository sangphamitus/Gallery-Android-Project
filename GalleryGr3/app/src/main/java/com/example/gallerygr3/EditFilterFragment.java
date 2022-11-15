package com.example.gallerygr3;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;

public class EditFilterFragment extends Fragment {
    static EditFilterFragment instance=null;
    EditImage main;
    Context context=null;
    String[] listName;
    ArrayList<Bitmap> listImage= new ArrayList<Bitmap>();

    LinearLayout   filterList;
    ImageButton backBtn,confirmBtn;
    Bitmap chooseFilter=null;
    EditFilterFragment(String[] listName,ArrayList<Bitmap> listImage)
    {
        this.listImage=listImage;
        this.listName=listName;
    }
    public static EditFilterFragment newInstance(String[] listName,ArrayList<Bitmap> listImage)
    {
        if(instance==null)
        {
            instance=new EditFilterFragment(listName,listImage);
        }
        return instance;
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
        LinearLayout layout = (LinearLayout) inflater.inflate(R.layout.edit_filter_fragment,null);

        filterList = (LinearLayout) layout.findViewById(R.id.filterList);
        backBtn=(ImageButton) layout.findViewById(R.id.backBtns);
        confirmBtn=(ImageButton) layout.findViewById(R.id.confirmBtns);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                main.BackFragment();

            }
        });

        confirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(chooseFilter!=null)
                main.ConfirmBlur(chooseFilter);
                main.BackFragment();
            }
        });

        for (Integer i=0;i<listImage.size();i++)
        {
             View frame=getLayoutInflater().inflate(R.layout.edit_filter_list_item,null);

            TextView caption=(TextView) frame.findViewById(R.id.filter_name);
            ImageView icon=(ImageView) frame.findViewById(R.id.filter_view);
            caption.setText(listName[i]);
            icon.setImageBitmap(listImage.get(i));
            Integer finalI = i;
            frame.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    main.BitmapFilterChoose(listImage.get(finalI),listName[finalI]);
                    chooseFilter=listImage.get(finalI);
                }
            });
            filterList.addView(frame);
        }

        return layout;
    }
}
