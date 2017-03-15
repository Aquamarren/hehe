package com.example.marrenmatias.trynavdrawer;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by MarrenMatias on 2/15/2017.
 */
public class GoalDetails extends Activity {
    DatabaseHelper mydb;
    SQLiteDatabase db;
    private TextView textViewGoalRank_;
    private Button buttonForecastGoal;
    private Button buttonUpdateGoal;
    private EditText editTextGoalName_;
    private EditText editTextGoalCost_;
    Cursor data;
    private TextView textViewMoneySaved;
    private ImageButton imageButton;
    private static  final int PICK_IMAGE = 70;
    Uri imageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_goal_details);
        openDatabase();
        mydb = new DatabaseHelper(this);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int) (width * .9), (int) (height * .9));

        textViewGoalRank_ = (TextView)findViewById(R.id.textViewGoalRank_);
        editTextGoalName_ = (EditText)findViewById(R.id.editTextGoalName_);
        editTextGoalCost_ = (EditText)findViewById(R.id.editTextGoalCost_);
        buttonUpdateGoal = (Button)findViewById(R.id.buttonUpdateGoal);
        buttonForecastGoal = (Button)findViewById(R.id.buttonForecastGoal);
        textViewMoneySaved = (TextView)findViewById(R.id.textViewMoneySaved);
        imageButton = (ImageButton)findViewById(R.id.imageButton);

        viewGoalDetails();
        openGallery();
    }

    protected void openDatabase() {
        db = openOrCreateDatabase("THRIFTY.db", Context.MODE_PRIVATE, null);
    }

    public  void openGallery(){
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
                startActivityForResult(gallery, PICK_IMAGE);
            }
        });

    }

    public void viewGoalDetails() {
        Bundle bundle = getIntent().getExtras();
        final String GoalRank = bundle.getString("goalRank");

        data = db.rawQuery("SELECT * FROM GOALS WHERE GoalAccomplished = 1 AND GoalRank = " + GoalRank, null);
        data.moveToFirst();
        final String textGoalRank = data.getString(4);
        String textGoalName = data.getString(1);
        final String textGoalCost = data.getString(2);
        final String textMoneySaved = data.getString(7);

        textViewGoalRank_.setText(textGoalRank);
        textViewMoneySaved.setText(textMoneySaved);
        editTextGoalName_.setText(textGoalName);
        editTextGoalCost_.setText(textGoalCost);

        try {
            byte[] bytes = mydb.retreiveImageFromDB();
            imageButton.setImageBitmap(Utils.getImage(bytes));
        } catch (Exception e) {

        }

        buttonUpdateGoal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String goalName = editTextGoalName_.getText().toString();
                String goalCost = editTextGoalCost_.getText().toString();

                float moneySaved = Float.valueOf(textMoneySaved);
                float costOfGoal = Float.valueOf(textGoalCost);
                Toast.makeText(GoalDetails.this, String.valueOf(moneySaved) + " " +String.valueOf(costOfGoal), Toast.LENGTH_SHORT).show();

                try {
                    InputStream iStream = getContentResolver().openInputStream(imageUri);
                    byte[] inputData = Utils.getBytes(iStream);

                    if (goalName.length() > 0 && goalCost.length() > 0) {
                        mydb.updateGoal(goalName, goalCost, GoalRank,inputData);
                        if (moneySaved >= costOfGoal) {
                            float difference = moneySaved - costOfGoal;
                            mydb.updateSavings(String.valueOf(difference));
                        }

                        Intent intent = new Intent(GoalDetails.this,MainActivity.class);
                        String frags = "ViewGoals";
                        intent.putExtra("to", frags);
                        startActivity(intent);
                    } else {
                        Toast.makeText(GoalDetails.this, "Fill up the field", Toast.LENGTH_SHORT).show();
                    }
                } catch (IOException ioe) {}
            }
        });

        buttonForecastGoal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent2 = new Intent(GoalDetails.this, GoalForecast.class);
                intent2.putExtra("goalRank", GoalRank);
                startActivity(intent2);
            }
        });
    }

}
