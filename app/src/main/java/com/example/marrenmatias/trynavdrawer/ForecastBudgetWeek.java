package com.example.marrenmatias.trynavdrawer;


import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class ForecastBudgetWeek extends AppCompatActivity {
    DatabaseHelper myDb;
    SQLiteDatabase db;
    private ListView listViewForecastList;
    Cursor data;
    SimpleCursorAdapter adapter;
    SimpleDateFormat df = new SimpleDateFormat("MMMM dd, yyyy");
    private TextView textView12;
    private Button btnForecastNextMonth;
    private TextView textViewForecastRangeDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forecast_budget);
        openDatabase();

        listViewForecastList = (ListView)findViewById(R.id.listViewForecastList);
        textViewForecastRangeDate = (TextView)findViewById(R.id.textViewForecastRangeDate);
        btnForecastNextMonth = (Button)findViewById(R.id.btnForecastNextMonth);
        btnForecastNextMonth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ForecastBudgetWeek.this, ForecastBudgetMonth.class);
                startActivity(intent);
            }
        });

        showForecastExpense();
    }

    protected void openDatabase() {
        db = openOrCreateDatabase("THRIFTY.db", Context.MODE_PRIVATE, null);
    }


    public void showForecastExpense() {
        Calendar date = Calendar.getInstance();
        String currentDate = df.format(date.getTime());
        date.add(Calendar.DATE, 7);
        String sevenDays = df.format(date.getTime());
        textViewForecastRangeDate.setText(currentDate + " - "+sevenDays);


        Cursor cur = db.rawQuery("SELECT *, EXPENSE.CategoryName, " +
                "INCOME.ID AS _id, " +
                "EXPENSE.ID AS _id, " +
                "(SUM(EXPENSE.ExpenseAmount)/(JulianDay('now') - JulianDay(MIN(EXPENSE.ExpenseDate)))) * 7 AS ExpenseForecast " +
                "FROM INCOME, EXPENSE WHERE EXPENSE.ACTIVE = 1 AND INCOME.ACTIVE = 1 " +
                "AND EXPENSE.ExpenseDate BETWEEN INCOME.IncomeDateTo AND INCOME.IncomeDateFrom " +
                "GROUP BY EXPENSE.CategoryName ORDER BY EXPENSE.ExpenseDate DESC", null);


        String[] columns = new String[]{"CategoryName", "ExpenseForecast"};
        int[] to = new int[]{R.id.textViewExpense, R.id.textViewExpenseAmountForecast};

        adapter = new SimpleCursorAdapter(ForecastBudgetWeek.this, R.layout.forecast_budget_row, cur, columns, to, 0);
        listViewForecastList.setAdapter(adapter);

    }
}

/*
        Expense Forecast for the Next Week = ((Total Expense recorded / (Date today – Date of first Expense)) * 7)
        1 week = 7 days
        Total Expense = 875; Date Today = November 14, 2016;
        Date of 1st Expense = November 01, 2016

        Expense Forecast for the Next Week = ((875 / (14 - 1)) * 7)
        Expense Forecast for the Next Week = 471.15
        The user average expense for the next week is 471.15.
     */

