package com.example.marrenmatias.trynavdrawer;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

public class ChangePic extends AppCompatActivity {
    TextView tf,choose;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.change_pic);
        tf = (TextView) findViewById(R.id.thrift);
        choose = (TextView) findViewById(R.id.ch);
        Typeface sCustomFont = Typeface.createFromAsset(getAssets(),"fonts/King-Basil-Lite.otf");
        Typeface myCustomFont = Typeface.createFromAsset(getAssets(), "fonts/BebasNeue.otf");
        tf.setTypeface(sCustomFont);
        choose.setTypeface(myCustomFont);

        GridView gridView = (GridView) findViewById(R.id.gridView);
        gridView.setAdapter(new ImageAdapter(this));

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Intent i = new Intent(ChangePic.this, SignUp.class);
                int pos = position;
                i.putExtra("id",pos);
                startActivity(i);
            }
        });

    }
}
