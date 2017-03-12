package com.example.marrenmatias.trynavdrawer;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

/**
 * Created by MarrenMatias on 3/6/2017.
 */

public class ImageAdapter extends BaseAdapter {
    private Context context;

    public Integer[] images = {
            R.drawable.dp1, R.drawable.dp2,
            R.drawable.dp3, R.drawable.dp4,
            R.drawable.dp5,R.drawable.dp6,
            R.drawable.dp7,R.drawable.dp8
    };

    public ImageAdapter(Context c)
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
        imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        imageView.setLayoutParams(new GridView.LayoutParams(240,240));
        return imageView;
    }
}
