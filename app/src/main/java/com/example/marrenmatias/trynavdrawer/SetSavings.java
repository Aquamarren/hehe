package com.example.marrenmatias.trynavdrawer;

        import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;

public class SetSavings extends AppCompatActivity {
    DatabaseHelper myDb;
    EditText txtSetSavings;
    ImageButton btnSaving;
    TextView ss;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.set_savings);
        myDb = new DatabaseHelper(this);

        ss = (TextView) findViewById(R.id.setSavings);
        txtSetSavings = (EditText)findViewById(R.id.editTextSetSavings);
        btnSaving = (ImageButton)findViewById(R.id.btnAddSaving);

        Typeface myCustomFont = Typeface.createFromAsset(getAssets(), "fonts/BebasNeue.otf");
        ss.setTypeface(myCustomFont);
        txtSetSavings.setTypeface(myCustomFont);

        AddSavings();
    }

    @Override
    public void onBackPressed(){
        super.onBackPressed();
        finish();
    }

    public void AddSavings(){
        btnSaving.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Float savingsAmount = Float.valueOf(txtSetSavings.getText().toString());
                        String timestamp = new SimpleDateFormat("MMMM dd, yyyy").format(new Date());
                        myDb.insertSavings(savingsAmount,timestamp);
                        Log.i("insert", "Data Inserted1");

                        myDb.calculateIncome(savingsAmount);
                        Log.i("update", "Data Updated");

                        Intent intent = new Intent(SetSavings.this,SetBudget_first.class);
                        startActivity(intent);
                    }
                }
        );
    }
}
