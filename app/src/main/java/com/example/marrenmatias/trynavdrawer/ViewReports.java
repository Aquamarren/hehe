package com.example.marrenmatias.trynavdrawer;


import android.content.Context;
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
import android.widget.CursorAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 */
public class ViewReports extends Fragment {
    SQLiteDatabase db;
    DatabaseHelper mydb;
    Cursor cursor;
    Cursor data;
    private ListView mMessageListView;
    private ListAdapter mAdapter;
    private ListView listViewCategoryWithExpense;
    private Button btnGraphShow;
    private Button btnForecastExp;
    private Button btnCheckBudget;

    public ViewReports() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.view_reports, container, false);

        mydb = new DatabaseHelper(getActivity());
        openDatabase();
        mMessageListView = (ListView) v.findViewById(R.id.listViewCategoryWithExpense);
        listViewCategoryWithExpense = (ListView)v.findViewById(R.id.listViewCategoryWithExpense);
        btnGraphShow = (Button)v.findViewById(R.id.btnGraphShow);
        btnForecastExp = (Button)v.findViewById(R.id.btnForecastExp);
        btnCheckBudget = (Button)v.findViewById(R.id.btnCheckBudget);

        btnForecastExp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ForecastBudgetWeek.class);
                startActivity(intent);
            }
        });

        btnGraphShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), BarChartGraph.class);
                startActivity(intent);
            }
        });

        mMessageListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parnet, View view, int position, long id) {
                // display an activiting show item details
            }
        });

        cursor = db.rawQuery("SELECT *, " +
                "EXPENSE.ID AS _id," +
                "CATEGORY.ID AS _id," +
                "(EXPENSE.ExpenseAmount/CATEGORY.BudgetCost) * 100 AS ProgressPercent " +
                "FROM EXPENSE, CATEGORY " +
                "WHERE EXPENSE.ACTIVE = 1 AND CATEGORY.ACTIVE = 1 " +
                "GROUP BY EXPENSE.ExpenseDate ORDER BY EXPENSE.ExpenseDate DESC",null);  // parameters snipped
        mAdapter = new MessageAdapter(getActivity(), cursor);
        mMessageListView.setAdapter(mAdapter);


        return v;

    }

    protected void openDatabase() {
        db = getActivity().openOrCreateDatabase("THRIFTY.db",android.content.Context.MODE_PRIVATE,null);
    }


    private static final class MessageAdapter extends CursorAdapter {

        // We have two list item view types

        private static final int VIEW_TYPE_GROUP_START = 0;
        private static final int VIEW_TYPE_GROUP_CONT = 1;
        private static final int VIEW_TYPE_COUNT = 10;

        MessageAdapter(Context context, Cursor cursor) {
            super(context, cursor);

            // Get the layout inflater

            mInflater = LayoutInflater.from(context);

            // Get and cache column indices
            // while(cursor.moveToNext()) {
            mColSubject = cursor.getColumnIndex("CategoryName");
            mColFrom = cursor.getColumnIndex("ExpenseAmount");
            mColWhen = cursor.getColumnIndex("ExpenseDate");
            //percentage = cursor.getColumnIndex("ProgressPercent");

            // }
        }

        @Override
        public int getViewTypeCount() {
            return VIEW_TYPE_COUNT;
        }

        @Override
        public int getItemViewType(int position) {
            // There is always a group header for the first data item

            if (position == 0) {
                return VIEW_TYPE_GROUP_START;
            }

            // For other items, decide based on current data

            Cursor cursor = getCursor();
            cursor.moveToPosition(position);
            boolean newGroup = isNewGroup(cursor, position);

            // Check item grouping

            if (newGroup) {
                return VIEW_TYPE_GROUP_START;
            } else {
                return VIEW_TYPE_GROUP_CONT;
            }
        }

        @Override
        public View newView(Context context, Cursor cursor, ViewGroup parent) {

            int position = cursor.getPosition();
            int nViewType;

            if (position == 0) {
                // Group header for position 0

                nViewType = VIEW_TYPE_GROUP_START;
            } else {
                // For other positions, decide based on data

                boolean newGroup = isNewGroup(cursor, position);

                if (newGroup) {
                    nViewType = VIEW_TYPE_GROUP_START;
                } else {
                    nViewType = VIEW_TYPE_GROUP_CONT;
                }
            }

            View v;

            if (nViewType == VIEW_TYPE_GROUP_START) {
                // Inflate a layout to start a new group

                //v = mInflater.inflate(R.layout.message_list_item_with_header, parent, false);
                //vi = inf.inflate(R.layout.list_header, null);
                v = mInflater.inflate(R.layout.list_header, parent, false);

                // Ignore clicks on the list header

                View vHeader = v.findViewById(R.id.list_header_title);
                vHeader.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                    }
                });
            } else {
                // Inflate a layout for "regular" items

                v = mInflater.inflate(R.layout.list_header, parent, false);
            }
            return v;
        }

        @Override
        public void bindView(View view, Context context, Cursor cursor) {
            TextView tv;
            ProgressBar pB;

            tv = (TextView) view.findViewById(R.id.ExpenseCategoryName1);
            tv.setText(cursor.getString(mColSubject));

            tv = (TextView) view.findViewById(R.id.ExpenseCategoryAmount1);
            tv.setText(cursor.getString(mColFrom));

            tv = (TextView) view.findViewById(R.id.list_header_title);
            tv.setText(cursor.getString(mColWhen));

            //pB.setProgress(cursor.getString(progress));
            pB = (ProgressBar)view.findViewById(R.id.progressBar);
           // pB.setProgress(cursor.getColumnIndex("ProgressPercent"));
           // pB.setProgress(Integer.valueOf(cursor.getString(percentage)));

        }

//
        private boolean isNewGroup(Cursor cursor, int position) {
            // Get date values for current and previous data items

            String nWhenThis = cursor.getString(mColWhen);

            cursor.moveToPosition(position - 1);
            String nWhenPrev = cursor.getString(mColWhen);

            // Restore cursor position

            cursor.moveToPosition(position);

            // Compare date values, ignore time values

            return false;
        }

        LayoutInflater mInflater;

        private int mColSubject;
        private int mColFrom;
        private int mColWhen;
        private int percentage;

    }

}
