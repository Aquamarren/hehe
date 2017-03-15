package com.example.marrenmatias.trynavdrawer;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class UpdateBudget_first extends AppCompatActivity {
    SQLiteDatabase db;
    DatabaseHelper mydb;
    private EditText editTextBudgetCategoryAmount;
    private Button btnSaveBudgetAmount;
    private TextView budgetCategoryName;
    float budgetAmount;
    float expenseAmount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.update_budget);
        openDatabase();
        mydb = new DatabaseHelper(this);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int)(width*.7),(int)(height*.5));


        editTextBudgetCategoryAmount = (EditText)findViewById(R.id.editTextBudgetCategoryAmount);
        btnSaveBudgetAmount = (Button)findViewById(R.id.btnSaveBudgetAmount);
        budgetCategoryName = (TextView)findViewById(R.id.budgetCategoryName);
        SaveBudget();
    }


    protected void openDatabase() {
        db = openOrCreateDatabase("THRIFTY.db", Context.MODE_PRIVATE, null);
    }

    private void SaveBudget() {
        Bundle bundle = getIntent().getExtras();
        final String categoryID = bundle.getString("categoryBudgetID");

        Cursor cur = db.rawQuery("SELECT count(*),* FROM CATEGORY WHERE ID = " + categoryID, null);
        cur.moveToFirst();
        String catName = cur.getString(cur.getColumnIndex("CategoryName"));
        final float expenseAmount = cur.getFloat(cur.getColumnIndex("BudgetCost"));
        final float catAmount = cur.getFloat(cur.getColumnIndex("Budget"));

        int count = cur.getInt(0);
        if(count > 0) {
            budgetCategoryName.setText(catName);
            editTextBudgetCategoryAmount.setText(String.format("%.2f",expenseAmount));

            Cursor cursor = db.rawQuery("SELECT IncomeAmount FROM INCOME WHERE ACTIVE = 1", null);
            cursor.moveToFirst();
            final float incomeAmount = Float.valueOf(cursor.getString(0));

            btnSaveBudgetAmount.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    float budg = Float.valueOf(editTextBudgetCategoryAmount.getText().toString());
                    if(budg > expenseAmount){
                        float getDifference =  budg - expenseAmount;
                        float totalIncome = incomeAmount - getDifference;
                        float totalBudget_ = catAmount + getDifference;

                        if(totalIncome < 0) {
                            Toast.makeText(UpdateBudget_first.this, "Income not enough", Toast.LENGTH_SHORT).show();
                        }else {
                            mydb.updateBudget(editTextBudgetCategoryAmount.getText().toString(), String.valueOf(totalBudget_), categoryID);
                            mydb.UpdateIncomeAmount(totalIncome);
                            Log.i("insert", "Data Inserted");
                        }

                    }
                    else if(expenseAmount > budg){
                        float add = expenseAmount - budg;
                        float totalIncome_ = incomeAmount + add;
                        float totalBudget = catAmount + add;

                        if(totalIncome_ < 0) {
                            Toast.makeText(UpdateBudget_first.this, "Income not enough", Toast.LENGTH_SHORT).show();
                        }else {
                            mydb.updateBudget(editTextBudgetCategoryAmount.getText().toString(), String.valueOf(totalBudget), categoryID);
                            mydb.UpdateIncomeAmount(totalIncome_);
                            Log.i("insert", "Data Inserted");
                        }

                    }else if(budg == expenseAmount){
                        float sameBudget = catAmount + 0;
                        mydb.updateBudget(editTextBudgetCategoryAmount.getText().toString(),String.valueOf(sameBudget),categoryID);
                    }
                }
            });
        }
    }

    public void Intent(){
        Intent intent = new Intent(UpdateBudget_first.this, SetBudget_first.class);
        startActivity(intent);
    }
}
