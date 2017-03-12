package com.example.marrenmatias.trynavdrawer;


        import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class SetIncome extends AppCompatActivity {
    DatabaseHelper myDb;
    SQLiteDatabase db;
    EditText txtIncomeAmount;
    ImageButton btnSetBudget;
    TextView si;

    private DatePicker datePickerTo;
    private DatePicker datePickerFrom;
    Cursor c;
    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");

    private static final String SELECT_SQL = "SELECT count(*) FROM Income WHERE ACTIVE = 1";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.set_income);
        myDb = new DatabaseHelper(this);
        openDatabase();

        si = (TextView) findViewById(R.id.setIncome);
        txtIncomeAmount = (EditText)findViewById(R.id.editTextIncomeAmount);
        btnSetBudget = (ImageButton)findViewById(R.id.btnSubmit);
        datePickerTo = (DatePicker) findViewById(R.id.datePickerTo);
        datePickerFrom = (DatePicker)findViewById(R.id.datePickerFrom);

        Typeface myCustomFont = Typeface.createFromAsset(getAssets(), "fonts/BebasNeue.otf");
        si.setTypeface(myCustomFont);
        txtIncomeAmount.setTypeface(myCustomFont);

        //Checking if there is Active Income
        c = db.rawQuery(SELECT_SQL, null);
        c.moveToFirst();
        int count = c.getInt(0);
        if(count > 0){
            //Checking if the set EndDate for Income is equal on the current date, so that Income not use will be savings already
            Cursor checkEndate = db.rawQuery("SELECT * FROM INCOME WHERE ACTIVE = 1", null);
            checkEndate.moveToFirst();
            String IncomeAmount = checkEndate.getString(1);
            String IncomeDateTo = checkEndate.getString(2);
            try{
                Date d = df.parse(IncomeDateTo);
                long endDateMs = d.getTime();
                long currentDate = new java.util.Date().getTime();
            /*    if(currentDate > endDateMs){
                    if(Float.valueOf(IncomeAmount) > 0) {
                        myDb.excessIncomeBudget(IncomeAmount);
                    }else{
                        myDb.deActivateIncome();
                    }
                }*/
            }catch (ParseException e){e.printStackTrace();}

            Intent intent = new Intent(SetIncome.this,MainActivity.class);
            String frags = "ViewExpense";
            intent.putExtra("to",frags);
            startActivity(intent);

        }else{
            AddData();
        }
    }

    @Override
    public void onBackPressed(){
        super.onBackPressed();
        Intent intent = new Intent(SetIncome.this,SetIncome.class);
        startActivity(intent);
        finish();
    }

    protected void openDatabase() {
        db = openOrCreateDatabase("THRIFTY.db", Context.MODE_PRIVATE, null);
    }

    public void AddData(){
        btnSetBudget.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                try{
                                                    String startDate = df.format(new Date(datePickerTo.getYear() - 1900, datePickerTo.getMonth(), datePickerTo.getDayOfMonth()));
                                                    String endDate = df.format(new Date(datePickerFrom.getYear() - 1900, datePickerFrom.getMonth(), datePickerFrom.getDayOfMonth()));
                                                    if (df.parse(startDate).before(df.parse(endDate))){
                                                        if(txtIncomeAmount.length() == 0)  {
                                                            Toast.makeText(SetIncome.this,"Fill up the field",Toast.LENGTH_SHORT).show();
                                                        }else {
                                                            Toast.makeText(SetIncome.this, "Start Date After End Date", Toast.LENGTH_LONG).show();
                                                        }
                                                    }else{
                                                        if(txtIncomeAmount.length() > 0)  {
                                                            myDb.insertData(txtIncomeAmount.getText().toString(), endDate, startDate);
                                                            Log.i("insert", "Data Inserted");
                                                            Intent intent = new Intent(SetIncome.this,SetSavings.class);
                                                            startActivity(intent);
                                                        }else{
                                                            Toast.makeText(SetIncome.this,"Fill up the field",Toast.LENGTH_SHORT).show();
                                                        }
                                                    }
                                                }catch (ParseException e) {e.printStackTrace();}
                                            }
                                        }
        );

    }


}
/*

     String str1 = "12/10/2013";
     Date date1 = formatter.parse(str1);

     String str2 = "13/10/2013";
     Date date2 = formatter.parse(str2);

     if (date1.compareTo(date2)<0)
      {
         System.out.println("date2 is Greater than my date1");
      }
 */