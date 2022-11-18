package com.example.gallerygr3;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.icu.number.Scale;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Optional;

public class viewPagerAdapter extends RecyclerView.Adapter<viewPagerAdapter.ViewHolder> implements ZoomCallBack{
    ArrayList<viewPagerItem> arrayItems;
    SelectedPicture main;
    //Zoom =====================
    private static final String TAG = "Touch";
    @SuppressWarnings("unused")
    Context context;
    private static final float MIN_ZOOM = 1f,MAX_ZOOM = 1f;

    static viewPagerAdapter intance=null;

    // These matrices will be used to scale points of the image
    Matrix matrix = new Matrix();
    Matrix savedMatrix = new Matrix();
    Matrix initMatrix=null;
    // The 3 states (events) which the user is trying to perform
    static final int NONE = 0;
    static final int DRAG = 1;
    static final int ZOOM = 2;
    int mode = NONE;

    // these PointF objects are used to record the point(s) the user is touching
    PointF start = new PointF();
    PointF mid = new PointF();
    float oldDist = 1f;

    //Zoom =============

    ImageView view;
    public viewPagerAdapter(ArrayList<viewPagerItem> arrayItems ,SelectedPicture main) {
        this.main=main;
        this.arrayItems = arrayItems;
        intance=this;
    }

    float totalRotate=0;
    int rotatePos=-1;
    ArrayList<ViewHolder> TemplateView= new ArrayList<ViewHolder>();

    // handle zoom event and swipe event var
    private static final long DOUBLE_PRESS_INTERVAL = 250; // in millis
    private long lastPressTime;
    boolean mHasDoubleClicked = false;
    boolean allowSwipe = true;
    boolean isZoom=false;

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.full_creen_picture,parent,false);

        return new ViewHolder(view);
    }



    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        viewPagerItem item= arrayItems.get(position);

        holder.img.setImageBitmap(item.getItemBitmap());
        TemplateView.add( holder);
     //   holder.txtName.setText(item.getSelectedName());

        holder.img.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                long pressTime = System.currentTimeMillis();

                view = (ImageView) v;
                view.setScaleType(ImageView.ScaleType.MATRIX);
                float scale;


                switch (event.getAction() & MotionEvent.ACTION_MASK)
                {
                    case MotionEvent.ACTION_DOWN:   // first finger down only
                        matrix.set(view.getImageMatrix());
                        if(initMatrix==null)
                        {
                            initMatrix=new Matrix();
                            initMatrix.set(matrix);
                        }
                        savedMatrix.set(matrix);
                        start.set(event.getX(), event.getY());
                        //Log.d(TAG, "mode=DRAG"); // write to LogCat

                        mode = DRAG;
                        if (pressTime - lastPressTime <= DOUBLE_PRESS_INTERVAL) {
                            mHasDoubleClicked = true;

                        }
                        else {     // If not double click....
//                            BackToInit();
                            mHasDoubleClicked = false;
                            main.showNav();
                        };
                        // record the last time the menu button was pressed.
                        lastPressTime = pressTime;

                        if(mHasDoubleClicked){
                            isZoom=false;
                            allowSwipe=true;
//                            Toast.makeText(main, "swipe: "+allowSwipe, Toast.LENGTH_SHORT).show();
                        }

                        break;

                    case MotionEvent.ACTION_UP: // first finger lifted

                    case MotionEvent.ACTION_POINTER_UP: // second finger lifted

                        mode = NONE;
                      //  Log.d(TAG, "mode=NONE");
                        break;

                    case MotionEvent.ACTION_POINTER_DOWN: // first and second finger down

                        oldDist = spacing(event);
                        Log.d(TAG, "oldDist=" + oldDist);
                        if (oldDist > 5f) {
                            savedMatrix.set(matrix);
                            midPoint(mid, event);
                            mode = ZOOM;
                            Log.d(TAG, "mode=ZOOM");
                        }
                        break;

                    case MotionEvent.ACTION_MOVE:

                        if (mode == DRAG)
                        {
                            if(isZoom){
                                matrix.set(savedMatrix);
                                matrix.postTranslate(event.getX() - start.x, event.getY() - start.y);
                            }
//                            matrix.set(savedMatrix);
//                            matrix.postTranslate(event.getX() - start.x, event.getY() - start.y); // create the transformation in the matrix  of points
                        }
                        else if (mode == ZOOM)
                        {
                            isZoom=true;
                            allowSwipe=false;
                            // pinch zooming
                            float newDist = spacing(event);
                            Log.d(TAG, "newDist=" + newDist);
                            if (newDist > 5f)
                            {
                                matrix.set(savedMatrix);
                                scale = newDist / oldDist; // setting the scaling of the
                                // matrix...if scale > 1 means
                                // zoom in...if scale < 1 means
                                // zoom out
                                matrix.postScale(scale, scale, mid.x, mid.y);
                            }
                        }
                        break;
                }

                if(allowSwipe){
                    main.allowSwipe();
                    BackToInit();
//                    Toast.makeText(main, "allow", Toast.LENGTH_SHORT).show();
                }else{
                    main.preventSwipe();
                    view.setImageMatrix(matrix); // display the transformation on screen

                }

                return true; // indicate event was handled
            }
        });
    }
    @Override
    public void BackToInit()
    {
        if(view==null) return;
        view.setScaleType(ImageView.ScaleType.MATRIX);

        view.setImageMatrix(initMatrix);
        view.setScaleType(ImageView.ScaleType.FIT_CENTER);
        initMatrix=null;
    }
    @Override
    public Bitmap RotateDegree(String currentImg,float degree,int pos)
    {

        if(rotatePos!=pos)
        {
            totalRotate=0;
            rotatePos= pos;
        }
        if(degree==0)
        {
            totalRotate=degree;
        }
        else
        {
            totalRotate+=degree;
        }

        ImageView setimg=null;
        for (int i=0;i<TemplateView.size();i++)
        {
            if(TemplateView.get(i).getAdapterPosition()==pos)
            {
                setimg= (ImageView) TemplateView.get(i).itemView.findViewById(R.id.imageView);
            }
        }


        File imgFile= new File(currentImg);
        Bitmap imageShoot= BitmapFactory.decodeFile(imgFile.getAbsolutePath());
        imageShoot=ImageUltility.rotateImage(imageShoot,totalRotate);

        setimg.setImageBitmap(imageShoot);
        //Toast.makeText(main, ""+pos+": "+currentImg, Toast.LENGTH_SHORT).show();
        return imageShoot;
    }
@Override
    public void setImageView(String currentImg,int pos)
    {
        ImageView setimg=null;
        for (int i=0;i<TemplateView.size();i++)
        {
            if(TemplateView.get(i).getAdapterPosition()==pos)
            {
                setimg= (ImageView) TemplateView.get(i).itemView.findViewById(R.id.imageView);
            }
        }
        File imgFile= new File( currentImg);
        Bitmap imageShoot= BitmapFactory.decodeFile(imgFile.getAbsolutePath());
        setimg.setImageBitmap(imageShoot);
    }




    public static void NotifyChanged() {
        if(intance !=null)
        intance.notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return arrayItems.size();
    }

    public viewPagerItem getItem(int position) {
        return arrayItems.get(position);
    }

    public class ViewHolder  extends RecyclerView.ViewHolder{
        ImageView img;
    //    TextView txtName;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            img=itemView.findViewById(R.id.imageView);
           // txtName=itemView.findViewById(R.id.tvName);

        }
    }

    private float spacing(MotionEvent event)
    {
        float x = event.getX(0) - event.getX(1);
        float y = event.getY(0) - event.getY(1);
        return (float) Math.sqrt(x * x + y * y);
    }

    /*
     * --------------------------------------------------------------------------
     * Method: midPoint Parameters: PointF object, MotionEvent Returns: void
     * Description: calculates the midpoint between the two fingers
     * ------------------------------------------------------------
     */

    private void midPoint(PointF point, MotionEvent event)
    {
        float x = event.getX(0) + event.getX(1);
        float y = event.getY(0) + event.getY(1);
        point.set(x / 2, y / 2);
    }

    /** Show an event in the LogCat view, for debugging */

}
