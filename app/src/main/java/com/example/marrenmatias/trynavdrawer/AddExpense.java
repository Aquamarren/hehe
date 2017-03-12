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
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by MarrenMatias on 2/20/2017.
 */

public class AddExpense extends Activity{
    SQLiteDatabase db;
    DatabaseHelper mydb;
    private EditText editTextExpenseAmount;
    private Button btnAddExpenses;
    private EditText editTextExpenseName;
    private ProgressBar progressBarExpense;
    private TextView textViewExpense_;
    private TextView textViewPercentIncrease;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_expense);
        openDatabase();
        mydb = new DatabaseHelper(this);

        //pop up
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int)(width*.7),(int)(height*.5));


        editTextExpenseAmount = (EditText)findViewById(R.id.editTextExpenseAmount);
        editTextExpenseName = (EditText)findViewById(R.id.editTextExpenseName);
        progressBarExpense = (ProgressBar)findViewById(R.id.progressBarExpense);
        textViewExpense_ = (TextView)findViewById(R.id.textViewExpense_);
        textViewPercentIncrease = (TextView)findViewById(R.id.textViewPercentIncrease);
        btnAddExpenses = (Button)findViewById(R.id.btnAddExpenses);

        InsertExpenses();
    }

    protected void openDatabase() {
        db = openOrCreateDatabase("THRIFTY.db", Context.MODE_PRIVATE, null);
    }

    public void InsertExpenses() {
        Intent i = getIntent();
        String categoryName = i.getStringExtra("categoryName");

        float budgetCost = Float.valueOf(i.getStringExtra("budgetCost"));
        float budget = Float.valueOf(i.getStringExtra("budget"));
        float difference = Math.abs(budgetCost - budget);
        textViewExpense_.setText(String.valueOf(difference) + "/" + budgetCost);
        editTextExpenseName.setText(categoryName);

        if(budget < 0) {
            float percentage = (difference/budgetCost) * 100;
            progressBarExpense.setProgress((int) difference);
            textViewPercentIncrease.setText(String.valueOf(percentage));
        }else{
            progressBarExpense.setProgress((int) difference);
        }

        btnAddExpenses.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = getIntent();
                String categoryName = i.getStringExtra("categoryName");
                String categoryID = i.getStringExtra("categoryID");

                String timestamp = new SimpleDateFormat("yyyy-MM-dd").format(new Date());

                Cursor cursor = db.rawQuery("SELECT count(*) FROM EXPENSE WHERE ExpenseDate = '" + timestamp + "' " +
                        "AND CategoryName = '" + categoryName + "' AND ACTIVE = 1", null);
                cursor.moveToFirst();
                int count = cursor.getInt(0);

                String expenseName = editTextExpenseName.getText().toString();

                Cursor curr = db.rawQuery("SELECT count(*),* FROM CATEGORY WHERE ACTIVE = 1 AND CategoryName = '" + expenseName+"'", null);
                curr.moveToFirst();
                int count2 = curr.getInt(0);

                if(count > 0){
                    if(count2 > 0) {
                        String categoryID_ = curr.getString(curr.getColumnIndex("ID"));
                        if(categoryID_ == categoryID) {
                            if ((editTextExpenseName.getText().toString()).length() > 0 && (editTextExpenseAmount.getText().toString()).length() > 0) {
                                mydb.UpdateExpenseName(editTextExpenseName.getText().toString(), categoryName);
                                mydb.UpdateExpenseAmount(editTextExpenseAmount.getText().toString(), timestamp, categoryName, categoryID);
                                Log.i("Expense Insert", "Expense Updated");

                                Intent intent = new Intent(AddExpense.this, MainActivity.class);
                                String frags = "ViewExpense";
                                intent.putExtra("to", frags);
                                startActivity(intent);

                            } else {
                                Toast.makeText(AddExpense.this, "Fill up the field", Toast.LENGTH_SHORT).show();
                            }
                        }else{
                            Toast.makeText(AddExpense.this,"Expense Name already exists!",Toast.LENGTH_SHORT).show();
                        }
                    }
                    else{
                        if ((editTextExpenseName.getText().toString()).length() > 0 && (editTextExpenseAmount.getText().toString()).length() > 0) {
                            mydb.UpdateExpenseName(editTextExpenseName.getText().toString(), categoryName);
                            mydb.UpdateExpenseAmount(editTextExpenseAmount.getText().toString(), timestamp, categoryName, categoryID);
                            Log.i("Expense Insert", "Expense Updated");

                            Intent intent = new Intent(AddExpense.this, MainActivity.class);
                            String frags = "ViewExpense";
                            intent.putExtra("to", frags);
                            startActivity(intent);

                        } else {
                            Toast.makeText(AddExpense.this, "Fill up the field", Toast.LENGTH_SHORT).show();
                        }
                    }
                }else {
                    if(count2 > 0) {
                        String categoryID_ = curr.getString(curr.getColumnIndex("ID"));
                        if(categoryID_ == categoryID) {
                            if ((editTextExpenseName.getText().toString()).length() > 0 && (editTextExpenseAmount.getText().toString()).length() > 0) {
                                mydb.UpdateExpenseName(editTextExpenseName.getText().toString(), categoryName);
                                mydb.AddExpense(editTextExpenseAmount.getText().toString(), timestamp, categoryName, categoryID);
                                Log.i("Expense Insert", "Expense Inserted");

                                Intent intent = new Intent(AddExpense.this, MainActivity.class);
                                String frags = "ViewExpense";
                                intent.putExtra("to", frags);
                                startActivity(intent);
                            } else {
                                Toast.makeText(AddExpense.this, "Fill up the field", Toast.LENGTH_SHORT).show();
                            }
                        }else{
                            Toast.makeText(AddExpense.this,"Expense Name already exists!",Toast.LENGTH_SHORT).show();
                        }
                    }else{
                        if ((editTextExpenseName.getText().toString()).length() > 0 && (editTextExpenseAmount.getText().toString()).length() > 0) {
                            mydb.UpdateExpenseName(editTextExpenseName.getText().toString(), categoryName);
                            mydb.AddExpense(editTextExpenseAmount.getText().toString(), timestamp, categoryName, categoryID);
                            Log.i("Expense Insert", "Expense Inserted");

                            Intent intent = new Intent(AddExpense.this, MainActivity.class);
                            String frags = "ViewExpense";
                            intent.putExtra("to", frags);
                            startActivity(intent);
                        } else {
                            Toast.makeText(AddExpense.this, "Fill up the field", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }
        });
    }
}
