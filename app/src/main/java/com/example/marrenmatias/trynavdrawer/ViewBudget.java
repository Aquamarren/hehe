package com.example.marrenmatias.trynavdrawer;


import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;


/**
 * A simple {@link Fragment} subclass.
 */
public class ViewBudget extends Fragment {
    DatabaseHelper mydb;
    SQLiteDatabase db;
    SimpleCursorAdapter adapter;
    Cursor data;
    private Button btnCheckBudget;
    private EditText categoryAmount;
    private ListView listView4;

    public ViewBudget() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.view_budget, container, false);
        mydb = new DatabaseHelper(getActivity());
        openDatabase();

        listView4 = (ListView) v.findViewById(R.id.listView4);
        btnCheckBudget = (Button) v.findViewById(R.id.btnCheckBudget);
        ShowCategoryList();
        UpdateBudget();

        return v;
    }




    protected void openDatabase() {
        db = getActivity().openOrCreateDatabase("THRIFTY.db",android.content.Context.MODE_PRIVATE,null);
    }

    public void ShowCategoryList() {
        data = mydb.getListContentsCategory();
        String[] columns = new String[]{DatabaseHelper.KEY_TASK,"BudgetCost"};
        int[] to = new int[]{R.id.firstCategoryName, R.id.firstCategoryAmount};

        adapter = new SimpleCursorAdapter(getActivity(), R.layout.firstcategory_row, data, columns, to, 0);
        listView4.setAdapter(adapter);

        listView4.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int pos, long id) {
                data.moveToPosition(pos);
                String rowId = data.getString(data.getColumnIndexOrThrow("_id"));

                Intent intent = new Intent(getActivity(), UpdateBudget.class);
                intent.putExtra("categoryBudgetID", rowId);
                startActivity(intent);

            }
        });
    }

    public void UpdateBudget(){
        btnCheckBudget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), MainActivity.class);
                startActivity(intent);
            }
        });
    }
}
