package com.example.marrenmatias.trynavdrawer;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.TextView;

public class ViewBadges extends AppCompatActivity {
    TextView tf,badge;
    ImageButton back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_badges);

        tf= (TextView) findViewById(R.id.thrift);
        badge= (TextView) findViewById(R.id.txtBadges);
        final Typeface myCustomFont = Typeface.createFromAsset(getAssets(), "fonts/BebasNeue.otf");
        badge.setTypeface(myCustomFont);
        Typeface sCustomFont = Typeface.createFromAsset(getAssets(),"fonts/King-Basil-Lite.otf");
        tf.setTypeface(sCustomFont);
        back = (ImageButton) findViewById(R.id.backButton);

        GridView gridView = (GridView) findViewById(R.id.gridd);
        gridView.setAdapter(new ImageAdapter1(this));


    }
}
