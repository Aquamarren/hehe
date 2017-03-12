package com.example.marrenmatias.trynavdrawer;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

/**
 * Created by MarrenMatias on 3/9/2017.
 */

public class ImageAdapter1 extends BaseAdapter{
    private Context context;

    public Integer[] images = {
            R.drawable.b1, R.drawable.b2,
            R.drawable.b3, R.drawable.b4,
            R.drawable.b5,R.drawable.b6,
            R.drawable.b7,R.drawable.b8,
            R.drawable.b9,R.drawable.b10,
            R.drawable.b11,R.drawable.b12
    };

    public ImageAdapter1(Context c)
    {
        context = c;
    }
    @Override
    public int getCount() {
        return images.length;
    }

    @Override
    public Object getItem(int position) {
        return images[position];
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView = new ImageView(context);
        imageView.setImageResource(images[position]);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        imageView.setLayoutParams(new GridView.LayoutParams(120,120));
        return imageView;
    }
}
