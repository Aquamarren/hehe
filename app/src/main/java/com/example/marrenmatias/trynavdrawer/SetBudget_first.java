package com.example.marrenmatias.trynavdrawer;


        import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;


public class SetBudget_first extends AppCompatActivity {
    SQLiteDatabase db;
    DatabaseHelper mydb;
    ImageButton btnCheck;
    TextView txtTotalBudget,sb;
    ImageButton btnAddCategory;
    Cursor c;
    SimpleCursorAdapter adapter;
    Cursor data;

    private static final String SELECT_SQL = "SELECT IncomeAmount FROM Income WHERE ACTIVE = 1";
    private ListView listViewFirstCategory;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.set_budget_first);
        openDatabase();
        mydb = new DatabaseHelper(this);


        btnCheck = (ImageButton) findViewById(R.id.btnSubmit);
        btnAddCategory = (ImageButton)findViewById(R.id.btnInsertCategory);
        txtTotalBudget = (TextView)findViewById(R.id.txtViewTotalBudget);
        sb = (TextView) findViewById(R.id.setBudget);
        listViewFirstCategory = (ListView)findViewById(R.id.listViewFirstCategory);

        Typeface myCustomFont = Typeface.createFromAsset(getAssets(), "fonts/BebasNeue.otf");
        sb.setTypeface(myCustomFont);
        txtTotalBudget.setTypeface(myCustomFont);

        c = db.rawQuery(SELECT_SQL, null);
        c.moveToFirst();
        TotalBudget();

        AddCategory();
        ShowFirstCategory();
    }

    @Override
    public void onBackPressed(){
        super.onBackPressed();
        finish();
    }

    protected void openDatabase() {
        db = openOrCreateDatabase("THRIFTY.db", Context.MODE_PRIVATE, null);
    }

    public void TotalBudget(){//String.format("%.2f",difference)
        float totalbudget = c.getFloat(0);
        txtTotalBudget.setText(String.format("%.2f", totalbudget));
    }

    public void ShowFirstCategory(){
        data = mydb.getListContentsCategory();
        String[] columns = new String[]{DatabaseHelper.KEY_TASK,"Budget"};
        int[] to = new int[]{R.id.firstCategoryName, R.id.firstCategoryAmount};

        adapter = new SimpleCursorAdapter(SetBudget_first.this,R.layout.firstcategory_row,data,columns,to,0);
        adapter.swapCursor(data);
        adapter.notifyDataSetChanged();
        listViewFirstCategory.setAdapter(adapter);

        listViewFirstCategory.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                data.moveToPosition(position);
                String rowId = data.getString(data.getColumnIndexOrThrow("_id"));

                Intent intent = new Intent(SetBudget_first.this, UpdateBudget_first.class);
                intent.putExtra("categoryBudgetID", rowId);
                startActivity(intent);
            }
        });
    }

    public void AddCategory(){
        btnAddCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SetBudget_first.this,IconCateg.class));
            }
        });

        btnCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SetBudget_first.this,MainActivity.class);
                String frags = "ViewExpense";
                intent.putExtra("to",frags);
                startActivity(intent);
            }
        });
    }


}

