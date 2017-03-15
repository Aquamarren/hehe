package com.example.marrenmatias.trynavdrawer;


import android.content.Intent;
import android.database.Cursor;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

/**
 * A simple {@link Fragment} subclass.
 */
public class AddIncome extends Fragment{
    DatabaseHelper myDb;
    private EditText editTxtAddIncome;
    private Button buttonAddIncome;
    private TextView IncomeVal;
    TextView i,iv,u;
    ImageButton up;

    private View.OnClickListener ibLis = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

        }
    };

    public AddIncome() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.add_income,container,false);
        myDb = new DatabaseHelper(getActivity());
        editTxtAddIncome = (EditText)v.findViewById(R.id.editTextAddIncome);
        buttonAddIncome = (Button) v.findViewById(R.id.btnAddIncome);
        IncomeVal = (TextView)v.findViewById(R.id.IncomeVal);
        i = (TextView) v.findViewById(R.id.Income);
        iv = (TextView) v.findViewById(R.id.IncomeVal);
        u = (TextView) v.findViewById(R.id.updatePopup);
        up = (ImageButton) v.findViewById(R.id.incomeUpdate);
        up.setOnClickListener(ibLis);
        Typeface myCustomFont = Typeface.createFromAsset(getContext().getAssets(), "fonts/BebasNeue.otf");
        i.setTypeface(myCustomFont);
        iv.setTypeface(myCustomFont);
        u.setTypeface(myCustomFont);

        Cursor cursor = myDb.Income();
        cursor.moveToFirst();
        final float incomeAmount = cursor.getFloat(cursor.getColumnIndex("IncomeAmount"));
        IncomeVal.setText(String.format("%.2f", incomeAmount));
        AddIncomeAmount();
        return v;
    }

    private void AddIncomeAmount() {
        buttonAddIncome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ((editTxtAddIncome.getText().toString()).length() > 0) {
                    float amount = Float.valueOf(editTxtAddIncome.getText().toString());

                    myDb.UpdateIncomeAmount(amount);
                    Toast.makeText(getActivity(), "Income Updated", Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(getActivity(), MainActivity.class);
                    String frags = "ViewIncome";
                    intent.putExtra("to", frags);
                    startActivity(intent);

                }else{
                    Toast.makeText(getActivity(), "Fill up the field", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    
}


//float difference = amount - incomeAmount;
                    /*

                    if(amount > incomeAmount) {
                        myDb.AddIncome(difference);
                        Toast.makeText(getActivity(), "Income Updated", Toast.LENGTH_SHORT).show();
                        Log.i("update", "Income Add/Updated");
                    }else if(amount < incomeAmount){
                        myDb.AddIncome(difference);
                        Toast.makeText(getActivity(), "Income Updated", Toast.LENGTH_SHORT).show();
                        Log.i("update", "Income Add/Updated");
                    }else if(amount == incomeAmount){
                        myDb.AddIncome(amount);
                        Toast.makeText(getActivity(), "Income Updated", Toast.LENGTH_SHORT).show();
                        Log.i("update", "Income Add/Updated");
                    }*/