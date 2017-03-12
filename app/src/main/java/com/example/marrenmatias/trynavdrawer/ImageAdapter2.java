package com.example.marrenmatias.trynavdrawer;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

/**
 * Created by BrandonCarlo on 3/10/2017.
 */
public class ImageAdapter2 extends BaseAdapter {
    private Context context;

    public Integer[] images = {
            R.drawable.flight, R.drawable.food,
            R.drawable.cards, R.drawable.grocery,
            R.drawable.power,R.drawable.rent,
            R.drawable.water,R.drawable.b8
    };

    public ImageAdapter2(Context c)
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
