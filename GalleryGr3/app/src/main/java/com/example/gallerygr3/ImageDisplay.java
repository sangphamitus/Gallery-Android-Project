
package com.example.gallerygr3;
import com.example.gallerygr3.SelectedPicture;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;


public class ImageDisplay extends Fragment {
    ImageButton changeBtn;
    GridView gridView;
    CardView cardView;
    String[] names ={"img1","img2","img3","img4","img5","img6","img7","img8","img9","img10","img11","img12"};
    int numCol=2;
    int[] images ={R.drawable.avatar1,R.drawable.avatar2, R.drawable.avatar3,
            R.drawable.avatar4, R.drawable.avatar5, R.drawable.avatar6,
            R.drawable.avatar7, R.drawable.avatar8, R.drawable.avatar9,
            R.drawable.avatar10, R.drawable.avatar11, R.drawable.avatar12,
    };

    public ImageDisplay() {
        // Required empty public constructor

    }

    // TODO: Rename and change types and number of parameters
    public static ImageDisplay newInstance(String param1, String param2) {
        ImageDisplay fragment = new ImageDisplay();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public class CustomAdapter extends BaseAdapter {
        private String[] imageNames;
        private int[] imagePhotos;
        private Context context;
        private LayoutInflater layoutInflater;

        public CustomAdapter(String[] imageNames, int[] imagePhotos, Context context) {
            this.imageNames = imageNames;
            this.imagePhotos = imagePhotos;
            this.context = context;
            this.layoutInflater = (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            return imagePhotos.length;
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            if(view == null){
                view =layoutInflater.inflate(R.layout.row_item,viewGroup,false);
            }
//            TextView tvName = view.findViewById(R.id.tvName);
            ImageView imageView = view.findViewById(R.id.imageView);

//            tvName.setText(imageNames[i]);
            imageView.setImageResource(imagePhotos[i]);
            return view;
        }
    }

    public class ListAdapter extends BaseAdapter{
        private String[] imageNames;
        private int[] imagePhotos;
        private Context context;
        private LayoutInflater layoutInflater;

        public ListAdapter(String[] imageNames, int[] imagePhotos, Context context) {
            this.imageNames = imageNames;
            this.imagePhotos = imagePhotos;
            this.context = context;
            this.layoutInflater = (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            return imagePhotos.length;
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            if(view == null){
                view =layoutInflater.inflate(R.layout.list_item,viewGroup,false);
            }
            TextView tvName = view.findViewById(R.id.tvName);
            ImageView imageView = view.findViewById(R.id.imageView);

            tvName.setText(imageNames[i]);
            imageView.setImageResource(imagePhotos[i]);
            return view;
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_image_display, container, false);

        gridView = (GridView) view.findViewById(R.id.gridView);
        changeBtn = (ImageButton)view.findViewById(R.id.resizeView);
        cardView = (CardView) view.findViewById(R.id.cardView);

        ImageDisplay.CustomAdapter customAdapter = new ImageDisplay.CustomAdapter(names,images,getActivity());
        ImageDisplay.ListAdapter listAdapter = new ImageDisplay.ListAdapter(names,images,getActivity());
        gridView.setAdapter(customAdapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String selectedName= names[i];
                int selectedImage = images[i];


                startActivity(new Intent(getActivity(), SelectedPicture.class)
                        .putExtra("name", selectedName).putExtra("image",selectedImage));
            }

        });

        changeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                numCol=numCol%5+1;
                if(numCol==1){
//                    numCol=2;
                    gridView.setAdapter(listAdapter);

                }
                else{
                    gridView.setAdapter(customAdapter);
                }
                gridView.setNumColumns(numCol);
            }
        });

        return view;
//        return inflater.inflate(R.layout.fragment_image_display, container, false);
    }


}