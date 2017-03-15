package com.example.marrenmatias.trynavdrawer;


import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class ViewGoalsPage extends Fragment {
    DatabaseHelper mydb;
    SQLiteDatabase db;
    private ListView listViewGoalsList;
    Cursor data;
    Cursor detail;

    public ViewGoalsPage() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.goals_page, container, false);
        openDatabase();
        mydb = new DatabaseHelper(getActivity());
        listViewGoalsList = (ListView) v.findViewById(R.id.listViewGoalsList);
        showGoalsList();
        goalAccomplished();
        return v;
    }

    protected void openDatabase() {
        db = getActivity().openOrCreateDatabase("THRIFTY.db",android.content.Context.MODE_PRIVATE,null); //RIGHT
    }

    public void goalAccomplished(){
        Cursor cursor = db.rawQuery("SELECT * FROM GOALS WHERE GoalAccomplished = 1 AND MoneySaved = GoalCost", null);
        while(cursor.moveToNext()){
            String goalName_ = cursor.getString(1);
            String goalCost = cursor.getString(2);
            int goalRank = Integer.valueOf(cursor.getString(4));
            float goalPoints = Math.round(goalRank / 5);
            float moneySaved = Float.valueOf(cursor.getString(7));
            if (Float.valueOf(goalCost) == moneySaved) {
                String SQL2 = "UPDATE GOALS SET GoalAccomplished = 0, GoalPoints = " + goalPoints + " WHERE GoalRank = "+ goalRank;
                db.execSQL(SQL2);
                String SQL3 = "INSERT INTO GOALS (GoalRank, GoalAccomplished, MoneySaved) VALUES (" + goalRank + ", 2, 0)";
                db.execSQL(SQL3);
                Toast.makeText(getActivity(), "Congratulations! Goal " + goalRank + " Accomplished! Enjoy your "+ goalName_ + "!", Toast.LENGTH_SHORT).show();
            }
        }

        //Magaadd ng badge
            /*
            yung badge nasa database na. tapos everytime na accomplsh magiging visible ung badge prng listbiew lng
            na inooutput pa horizontal
             */
    }

    public void showGoalsList(){
        final ArrayList<String> theList = new ArrayList<>();

        data = db.rawQuery("SELECT GoalRank FROM GOALS WHERE  GoalAccomplished > 0 ORDER BY GoalRank ASC",null);
        while(data.moveToNext()) {
            theList.add(data.getString(0));
            ListAdapter listAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, theList);
            listViewGoalsList.setAdapter(listAdapter);

            listViewGoalsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    data.moveToPosition(position);
                    String result = data.getString(data.getColumnIndex("GoalRank"));
                    Toast.makeText(getActivity(), result, Toast.LENGTH_SHORT).show();
                    detail = db.rawQuery("SELECT *, count(*) FROM GOALS WHERE GoalAccomplished = 1 AND GoalRank = " + result, null);
                    detail.moveToFirst();
                    int count = detail.getInt(0);

                    final String goalName = detail.getString(detail.getColumnIndex("GoalName"));

                    if(count > 0){
                        if(goalName == null){
                            Intent intent = new Intent(getActivity(), AddGoal.class);
                            intent.putExtra("goalRank", result);
                            startActivity(intent);
                        }else{
                            Intent intent2 = new Intent(getActivity(), GoalDetails.class);
                            intent2.putExtra("goalRank",result);
                            startActivity(intent2);
                        }
                    }else{
                        Toast.makeText(getActivity(), "Cannot Add Goal!", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
        Log.i("View", "CategoryList");
    }

}
