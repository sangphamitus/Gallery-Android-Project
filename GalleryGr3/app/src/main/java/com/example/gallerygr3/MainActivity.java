package com.example.gallerygr3;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    LinearLayout[] arrNavLinearLayouts = new LinearLayout[3];
    ImageView[] arrNavImageViews = new ImageView[3];
    TextView[] arrNavTextViews = new TextView[3];
    private int selectedTab=0;
    int[] arrRoundLayout = new int[3];
    int[] arrIcon = new int[3];
    int[] arrSelectedIcon = new int[3];

    Class[] arrFrag = new Class[3];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        arrFrag[0]=PhotosFragment.class;
        arrFrag[1]=AlbumsFragment.class;
        arrFrag[2]=SettingsFragment.class;

        arrRoundLayout[0]=R.drawable.round_photos;
        arrRoundLayout[1]=R.drawable.round_albums;
        arrRoundLayout[2]=R.drawable.round_settings;

        arrIcon[0]=R.drawable.ic_baseline_photo;
        arrIcon[1]=R.drawable.ic_baseline_photo_library;
        arrIcon[2]=R.drawable.ic_baseline_settings;

        arrSelectedIcon[0]=R.drawable.ic_baseline_photo_selected;
        arrSelectedIcon[1]=R.drawable.ic_baseline_photo_library_selected;
        arrSelectedIcon[2]=R.drawable.ic_baseline_settings_selected;

        arrNavLinearLayouts[0]=(LinearLayout)findViewById(R.id.photosLayout);
        arrNavLinearLayouts[1]=(LinearLayout)findViewById(R.id.albumsLayout);
        arrNavLinearLayouts[2]=(LinearLayout)findViewById(R.id.settingsLayout);

        arrNavImageViews[0]=(ImageView)findViewById(R.id.photos_img);
        arrNavImageViews[1]=(ImageView)findViewById(R.id.albums_img);
        arrNavImageViews[2]=(ImageView)findViewById(R.id.settings_img);

        arrNavTextViews[0]=(TextView)findViewById(R.id.photos_txt);
        arrNavTextViews[1]=(TextView)findViewById(R.id.albums_txt);
        arrNavTextViews[2]=(TextView)findViewById(R.id.settings_txt);

        arrNavLinearLayouts[0].setOnClickListener(new NavLinearLayouts(0));
        arrNavLinearLayouts[1].setOnClickListener(new NavLinearLayouts(1));
        arrNavLinearLayouts[2].setOnClickListener(new NavLinearLayouts(2));

    }

    protected class NavLinearLayouts implements View.OnClickListener {
        public int thisIndex;

        NavLinearLayouts(int index){
            thisIndex=index;
        }

        @SuppressLint("ResourceAsColor")
        @Override
        public void onClick(View view) {
            if(selectedTab!=thisIndex) {

                //go to current fragment
                selectedTab=thisIndex;

                getSupportFragmentManager().beginTransaction()
                        .setReorderingAllowed(true)
                        .replace(R.id.fragment_container,arrFrag[thisIndex],null)
                        .commit();

                // change others icon
                for(int i =0;i<3;i++){
                    if(i!=thisIndex){
                        arrNavTextViews[i].setVisibility(View.GONE);
                        arrNavImageViews[i].setImageResource(arrIcon[i]);
                        arrNavLinearLayouts[i].setBackgroundColor(getResources().getColor(R.color.white,null));
                    }

                    //change this icon
                    arrNavTextViews[thisIndex].setVisibility(View.VISIBLE);
                    arrNavImageViews[thisIndex].setImageResource(arrSelectedIcon[thisIndex]);
                    arrNavLinearLayouts[thisIndex].setBackgroundResource(arrRoundLayout[thisIndex]);

                    //animation
                    ScaleAnimation scaleAnimation= new ScaleAnimation(0.8f,1.0f,1f,1f, Animation.RELATIVE_TO_SELF,0,Animation.RELATIVE_TO_SELF,0.0f);
                    scaleAnimation.setDuration(200);
                    scaleAnimation.setFillAfter(true);
                    arrNavLinearLayouts[thisIndex].startAnimation(scaleAnimation);

                }
            }
        }
    }
}