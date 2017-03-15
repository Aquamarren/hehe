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
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class ViewReports extends Fragment {
    BaseAdapter adapter;
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
    String[] mProjection;

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



        cursor = db.rawQuery("SELECT EXPENSE.CategoryName, EXPENSE.ExpenseAmount,EXPENSE.ExpenseDate," +
                "EXPENSE.ID AS _idd," +
                "CATEGORY.ID AS _id," +
                "(EXPENSE.ExpenseAmount/CATEGORY.BudgetCost) * 100 AS ProgressPercent " +
                "FROM EXPENSE, CATEGORY " +
                "WHERE EXPENSE.ACTIVE = 1 AND CATEGORY.ACTIVE = 1 " +
                "GROUP BY EXPENSE.ExpenseDate ORDER BY EXPENSE.ExpenseDate DESC",null);  // parameters snipped*/


        mProjection = new String[] {
                "CategoryName",
                "ExpenseAmount","ExpenseDate"
        };

        int nameIndex = cursor.getColumnIndex(mProjection[0]);
        int numberIndex = cursor.getColumnIndex(mProjection[1]);
        int dateIndex = cursor.getColumnIndex(mProjection[2]);

        ArrayList contacts = new ArrayList();

        int position = 0;
        boolean isSeparator = false;
        while(cursor.moveToNext()) {
            isSeparator = false;

            String name = cursor.getString(nameIndex);
            String number = cursor.getString(numberIndex);
            String date = cursor.getString(dateIndex);

            char[] dateArray;

            // If it is the first item then need a separator
            if (position == 0) {
                isSeparator = true;
                dateArray = date.toCharArray();
            }
            else {
                // Move to previous
                cursor.moveToPrevious();

                // Get the previous contact's name
                String previousDate = cursor.getString(dateIndex);

                // Convert the previous and current contact names
                // into char arrays
                char[] previousDateArray = previousDate.toCharArray();
                dateArray = date.toCharArray();

                // Compare the first character of previous and current contact names
                if (dateArray[0] != previousDateArray[0]) {
                    isSeparator = true;
                }

                // Don't forget to move to next
                // which is basically the current item
                cursor.moveToNext();
            }

            // Need a separator? Then create a Contact
            // object and save it's name as the section
            // header while pass null as the phone number
           /* if (isSeparator) {
                Contact contact = new Contact(String.valueOf(dateArray[0]), null, isSeparator);
                contacts.add( contact );
            }*/

            // Create a Contact object to store the name/number details
            Contact contact = new Contact(name, number, date,false);
            contacts.add( contact );

            position++;
        }

        // Creating our custom adapter
        CustomAdapter adapter = new CustomAdapter(getActivity(), contacts);

        // Create the list view and bind the adapter
        ListView listView = (ListView)v.findViewById(R.id.listViewCategoryWithExpense);
        listView.setAdapter(adapter);



        return v;

    }

    protected void openDatabase() {
        db = getActivity().openOrCreateDatabase("THRIFTY.db",android.content.Context.MODE_PRIVATE,null);
    }

    public class Contact {
        public String mDate;
        public String mName;
        public String mNumber;
        public boolean mIsSeparator;

        public Contact(String name, String number, String date,boolean isSeparator) {
            mDate = date;
            mName = name;
            mNumber = number;
            mIsSeparator = isSeparator;
        }

        public void setName(String name) {
            mName = name;
        }

        public void setDate(String date) {
            mDate = date;
        }

        public void setNumber(String number) {
            mNumber = number;
        }

        public void setIsSection(boolean isSection) {
            mIsSeparator = isSection;
        }
    }


    public class CustomAdapter extends BaseAdapter {

        private Context mContext;
        private ArrayList<Contact> mList;

        // View Type for Separators
        private static final int ITEM_VIEW_TYPE_SEPARATOR = 0;
        // View Type for Regular rows
        private static final int ITEM_VIEW_TYPE_REGULAR = 1;
        // Types of Views that need to be handled
        // -- Separators and Regular rows --
        private static final int ITEM_VIEW_TYPE_COUNT = 2;

        public CustomAdapter(Context context, ArrayList list) {
            mContext = context;
            mList = list;
        }

        @Override
        public int getCount() {
            return mList.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public int getViewTypeCount() {
            return ITEM_VIEW_TYPE_COUNT;
        }

        @Override
        public int getItemViewType(int position) {
            boolean isSection = mList.get(position).mIsSeparator;

            if (isSection) {
                return ITEM_VIEW_TYPE_SEPARATOR;
            }
            else {
                return ITEM_VIEW_TYPE_REGULAR;
            }
        }

        @Override
        public boolean isEnabled(int position) {
            return getItemViewType(position) != ITEM_VIEW_TYPE_SEPARATOR;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            View view;

            Contact contact = mList.get(position);
            int itemViewType = getItemViewType(position);

            if (convertView == null) {
                LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

                if (itemViewType == ITEM_VIEW_TYPE_SEPARATOR) {
                    // If its a section ?
                    view = inflater.inflate(R.layout.header_only, null);
                }
                else {
                    // Regular row
                    view = inflater.inflate(R.layout.list_header, null);
                }
            }
            else {
                view = convertView;
            }


            if (itemViewType == ITEM_VIEW_TYPE_SEPARATOR) {
                // If separator

                TextView separatorView = (TextView) view.findViewById(R.id.separator);
                separatorView.setText(contact.mName);
            }
            else {
                // If regular

                // Set contact name and number
                TextView contactNameView = (TextView) view.findViewById(R.id.ExpenseCategoryName1);
                TextView phoneNumberView = (TextView) view.findViewById(R.id.ExpenseCategoryAmount1);

                contactNameView.setText( contact.mName );
                phoneNumberView.setText( contact.mNumber );
            }

            return view;
        }
    }



}
