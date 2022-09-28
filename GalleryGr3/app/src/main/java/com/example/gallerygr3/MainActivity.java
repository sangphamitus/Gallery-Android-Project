package com.example.gallerygr3;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        gridView = findViewById(R.id.gridView);
        changeBtn = (ImageButton)findViewById(R.id.resizeView);
        cardView = (CardView) findViewById(R.id.cardView);

        GridCustomAdapter gridAdapter = new GridCustomAdapter(names,images,this);
        ListCustomAdapter listAdapter = new ListCustomAdapter(names,images,this);
        gridView.setAdapter(gridAdapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String selectedName= names[i];
                int selectedImage = images[i];
                startActivity(new Intent(MainActivity.this,SelectedPicture.class).putExtra("name", selectedName).putExtra("image",selectedImage));
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
                    gridView.setAdapter(gridAdapter);
                }
                gridView.setNumColumns(numCol);
            }
        });
    }

    public class GridCustomAdapter extends BaseAdapter{
        private String[] imageNames;
        private int[] imagePhotos;
        private Context context;
        private LayoutInflater layoutInflater;

        public GridCustomAdapter(String[] imageNames, int[] imagePhotos, Context context) {
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
                view =layoutInflater.inflate(R.layout.row_items,viewGroup,false);
            }
//            TextView tvName = view.findViewById(R.id.tvName);
            ImageView imageView = view.findViewById(R.id.imageView);

//            tvName.setText(imageNames[i]);
            imageView.setImageResource(imagePhotos[i]);
            return view;
        }
    }


    public class ListCustomAdapter extends BaseAdapter{
        private String[] imageNames;
        private int[] imagePhotos;
        private Context context;
        private LayoutInflater layoutInflater;

        public ListCustomAdapter(String[] imageNames, int[] imagePhotos, Context context) {
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
}