package com.example.marrenmatias.trynavdrawer;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by May Galang on 1/16/2017.
 */
public class AddCategory extends Activity {
    DatabaseHelper mydb;
    SQLiteDatabase db;
    private EditText editTextCategoryName;
    private Button btnInsertCategory;
    private DatePicker datePickerDueDate;
    private TimePicker timePicker;
    private Calendar calendar;
    private String format = "";
    private CheckBox checkBoxDueDate;
    private String checkBox = "";
    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
    String cID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_category);
        openDatabase();
        mydb = new DatabaseHelper(this);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int)(width*.9),(int)(height*.9));

        editTextCategoryName = (EditText)findViewById(R.id.editTextCategoryName);
        btnInsertCategory = (Button)findViewById(R.id.btnInsertCategory);
        datePickerDueDate = (DatePicker)findViewById(R.id.datePickerDueDate);
        timePicker = (TimePicker)findViewById(R.id.timePicker);
        checkBoxDueDate = (CheckBox)findViewById(R.id.checkBoxDueDate);

        Intent i = getIntent();
        cID = i.getStringExtra("id");

        InsertCategory();

    }


    protected void openDatabase() {
        db = openOrCreateDatabase("THRIFTY.db", Context.MODE_PRIVATE, null);
    }

    public void InsertCategory(){
        btnInsertCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String hour = timePicker.getCurrentHour().toString();
                String min = timePicker.getCurrentMinute().toString();


                /*
                if (hour == 0) {
                    hour += 12;
                    format = "AM";
                } else if (hour == 12) {
                    format = "PM";
                } else if (hour > 12) {
                    hour -= 12;
                    format = "PM";
                } else {
                    format = "AM";
                }

                String time = hour + ":"+ min + " " +format;*/

                if(checkBoxDueDate.isChecked()){
                    checkBox = "1";
                }else{
                    checkBox = "0";
                }

                String dueDate = df.format(new Date(datePickerDueDate.getYear(), datePickerDueDate.getMonth(), datePickerDueDate.getDayOfMonth()));
                String time = hour + ":" + min;

                Cursor curr = db.rawQuery("SELECT * FROM CATEGORY WHERE ACTIVE = 1", null);
                while(curr.moveToFirst()){
                    String categoryName_ = curr.getString(curr.getColumnIndex("CategoryName"));
                    String CategoryName = editTextCategoryName.getText().toString();
                    if(CategoryName.equals(categoryName_)) {
                        Toast.makeText(AddCategory.this,"Expense Name already exists!",Toast.LENGTH_SHORT).show();
                    }
                    else{
                        if(CategoryName.length() > 0) {
                            mydb.AddCategory(editTextCategoryName.getText().toString(), checkBox, dueDate,time,String.valueOf(cID));

                            Intent intent = new Intent(AddCategory.this,MainActivity.class);
                            String frags = "ViewExpense";
                            intent.putExtra("to", frags);
                            startActivity(intent);

                            Toast.makeText(AddCategory.this, "Category Inserted", Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(AddCategory.this,"Fill up the field",Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }
        });
    }
}
