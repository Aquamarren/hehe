package com.example.marrenmatias.trynavdrawer;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
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
    int cID;

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

        calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int min = calendar.get(Calendar.MINUTE);

        Intent i = getIntent();
        cID = i.getIntExtra("id",123);

        InsertCategory();

        /*
        NOTE: IF YUNG CATEGORY NAME AY EXISTING NA DI PDENG IINSERT
         */
    }

    /*
    String time = extras.getString("time");
String[] parts = time.split(":");
timeEt.setCurrentHour(Integer.valueOf(parts[0]));
timeEt.setCurrentMinute(Integer.valueOf(parts[1]));
and concat it, when inserting or updating

dbConnector.insertContact(nameEt.getText().toString(),
                          capEt.getText().toString(),
                          timeEt.getCurrentHour().toString() + ":"
                              + timeEt.getCurrentMinute().toString(),
                          codeEt.getText().toString());
     */
    protected void openDatabase() {
        db = openOrCreateDatabase("THRIFTY.db", Context.MODE_PRIVATE, null);
    }

    public void InsertCategory(){
        btnInsertCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int hour = timePicker.getCurrentHour();
                int min = timePicker.getCurrentMinute();

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

                String dueDate = df.format(new Date(datePickerDueDate.getYear() - 1900, datePickerDueDate.getMonth(), datePickerDueDate.getDayOfMonth()));
                String time = String.valueOf(hour) + ":" +String.valueOf(min)+ " " + format;

                Cursor curr = db.rawQuery("SELECT * FROM CATEGORY WHERE ACTIVE = 1", null);
                while(curr.moveToFirst()){
                    String categoryName_ = curr.getString(curr.getColumnIndex("CategoryName"));
                    if(categoryName_ != editTextCategoryName.getText().toString()) {
                        if(editTextCategoryName.getText().toString() != null) {
                            mydb.AddCategory(editTextCategoryName.getText().toString(), checkBox, dueDate,
                                    hour + ":" + min,String.valueOf(cID));
                            Log.i("insert", "Category Inserted");

                            Intent intent = new Intent(AddCategory.this,Main4Activity.class);
                            startActivity(intent);
                            Toast.makeText(AddCategory.this, "Category Inserted", Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(AddCategory.this,"Fill up the field",Toast.LENGTH_SHORT).show();
                        }
                    }
                    else{
                        Toast.makeText(AddCategory.this,"Expense Name already exists!",Toast.LENGTH_SHORT).show();
                    }
                }

            }
        });
    }
}
