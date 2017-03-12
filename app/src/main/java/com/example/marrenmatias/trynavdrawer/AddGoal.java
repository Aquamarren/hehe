package com.example.marrenmatias.trynavdrawer;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by MarrenMatias on 2/15/2017.
 */
public class AddGoal extends Activity {
    DatabaseHelper mydb;
    private EditText editTextGoalName;
    private Button btnSaveGoal;
    private EditText editTextGoalCost;
    private ImageView imageView;
    private Button button;
    private static  final int PICK_IMAGE = 50;
    Uri imageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_goal);
        mydb = new DatabaseHelper(this);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int)(width*.7),(int)(height*.5));

        editTextGoalName = (EditText)findViewById(R.id.editTextGoalName);
        editTextGoalCost = (EditText)findViewById(R.id.editTextGoalCost);
        btnSaveGoal = (Button)findViewById(R.id.btnSaveGoal);
        imageView = (ImageView)findViewById(R.id.imageView);
        button = (Button) findViewById(R.id.button);
        insertGoal();
        openGallery();
    }

    public  void openGallery(){
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
                startActivityForResult(gallery, PICK_IMAGE);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK && requestCode == PICK_IMAGE){
            imageUri = data.getData();
            imageView.setImageURI(imageUri);
        }
    }

    private void insertGoal() {
        btnSaveGoal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String timestamp = new SimpleDateFormat("MMMM dd, yyyy").format(new Date());
                Bundle bundle = getIntent().getExtras();
                int GoalRank = bundle.getInt("goalRank");

                String GoalName = editTextGoalName.getText().toString();
                String GoalCost = editTextGoalCost.getText().toString();

                if(GoalName.trim().equals("") && GoalCost.trim().equals("")){
                    Toast.makeText(AddGoal.this, "Cannot Add Goal!", Toast.LENGTH_SHORT).show();
                }else{
                    mydb.insertGoal(GoalName, GoalCost, timestamp, GoalRank);
                    Log.i("insert", "Goal Inserted");
                    int difference = GoalRank - 1;
                    if (GoalRank == 1) {
                        mydb.updateGoalRank(5);
                    } else if (GoalRank > 1) {
                        mydb.updateGoalRank(difference);
                    }

                    Intent intent = new Intent(AddGoal.this, MainActivity.class);
                    String frags = "ViewGoals";
                    intent.putExtra("to", frags);
                    startActivity(intent);
                }
            }
        });
    }
}