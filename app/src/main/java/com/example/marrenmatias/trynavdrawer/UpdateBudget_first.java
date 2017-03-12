package com.example.marrenmatias.trynavdrawer;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

    @Override
    public void onBackPressed(){
        super.onBackPressed();
        Intent intent = new Intent(UpdateBudget_first.this,UpdateBudget_first.class);
        startActivity(intent);
        finish();
    }

    protected void openDatabase() {
        db = openOrCreateDatabase("THRIFTY.db", Context.MODE_PRIVATE, null);
    }

    private void SaveBudget() {
        Bundle bundle = getIntent().getExtras();
        String categoryID = bundle.getString("categoryBudgetID");

        Cursor cur = db.rawQuery("SELECT count(*),* FROM CATEGORY WHERE ID = " + categoryID, null);
        cur.moveToFirst();
        String catName = cur.getString(cur.getColumnIndex("CategoryName"));
        expenseAmount = cur.getFloat(cur.getColumnIndex("Budget"));
        float catAmount = cur.getFloat(cur.getColumnIndex("Budget"));

        int count = cur.getInt(0);
        if(count > 0) {
            budgetCategoryName.setText(catName);
            editTextBudgetCategoryAmount.setText(String.valueOf(catAmount));
            budgetAmount = Float.parseFloat(editTextBudgetCategoryAmount.getText().toString());

            btnSaveBudgetAmount.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Cursor cursor = db.rawQuery("SELECT IncomeAmount FROM INCOME WHERE ACTIVE = 1", null);
                    cursor.moveToFirst();
                    float incomeAmount = Float.valueOf(cursor.getString(0));

                    if (budgetAmount > incomeAmount) {
                        Toast.makeText(UpdateBudget_first.this, "Income not enough", Toast.LENGTH_SHORT).show();
                    } else {
                        Bundle bundle = getIntent().getExtras();
                        String categoryIDd = bundle.getString("categoryBudgetID");

                        if(budgetAmount >= expenseAmount){
                            float getDifference = budgetAmount - expenseAmount;
                            mydb.updateBudget(editTextBudgetCategoryAmount.getText().toString(),categoryIDd);
                            mydb.calculateIncome(getDifference);
                            Log.i("insert", "Data Inserted");
                        }else if(expenseAmount > budgetAmount){
                            float add = budgetAmount - expenseAmount;
                            mydb.updateBudget(editTextBudgetCategoryAmount.getText().toString(),categoryIDd);
                            mydb.calculateIncome(add);
                            Log.i("insert", "Data Inserted");
                        }

                        Intent intent = new Intent(UpdateBudget_first.this,UpdateBudget_first.class);
                        startActivity(intent);
                    }
                }
            });
        }

    }
}
