package com.example.gallerygr3;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import org.w3c.dom.Text;

public class EditBlurFragment extends Fragment {
    static EditBlurFragment instance=null;
    EditImage main;
    Context context=null;

    Button backBtn,confirmBtn;
    SeekBar seekBar;
    TextView text;
    Bitmap bluredImage;
    public static EditBlurFragment newInstance()
    {
        if(instance==null)
        {
           instance=new EditBlurFragment();
        }
        return new EditBlurFragment();
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
        LinearLayout layout = (LinearLayout) inflater.inflate(R.layout.edit_blur_fragment,null);

        seekBar= (SeekBar) layout.findViewById(R.id.edit_blur_amount );
        backBtn= (Button)layout.findViewById(R.id.blur_back_btns);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                main.BackFragment();

            }
        });
        confirmBtn=(Button) layout.findViewById(R.id.blur_confirm_btns);
        confirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                main.ConfirmBlur(bluredImage);
                main.BackFragment();
            }
        });
        seekBar.setMax(100);
        seekBar.setProgress(0);
        text=(TextView) layout.findViewById(R.id.edit_blur_amount_num);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                text.setText(""+i);
                bluredImage=main.blurFast(i);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });





        return layout;
    }

}
