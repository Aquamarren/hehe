package com.example.marrenmatias.trynavdrawer;

import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class ViewSettings extends AppCompatActivity {
    TextView tf,sett;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_settings);

        tf= (TextView) findViewById(R.id.thrift);
        sett= (TextView) findViewById(R.id.txtBadges);
        final Typeface myCustomFont = Typeface.createFromAsset(getAssets(), "fonts/BebasNeue.otf");
        sett.setTypeface(myCustomFont);
        Typeface sCustomFont = Typeface.createFromAsset(getAssets(),"fonts/King-Basil-Lite.otf");
        tf.setTypeface(sCustomFont);
    }
}
