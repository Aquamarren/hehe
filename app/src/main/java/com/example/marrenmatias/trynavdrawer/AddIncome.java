package com.example.marrenmatias.trynavdrawer;


import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
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
        i = (TextView) v.findViewById(R.id.Income);
        iv = (TextView) v.findViewById(R.id.IncomeVal);
        u = (TextView) v.findViewById(R.id.updatePopup);
        up = (ImageButton) v.findViewById(R.id.incomeUpdate);
        up.setOnClickListener(ibLis);
        Typeface myCustomFont = Typeface.createFromAsset(getContext().getAssets(), "fonts/BebasNeue.otf");
        i.setTypeface(myCustomFont);
        iv.setTypeface(myCustomFont);
        u.setTypeface(myCustomFont);
        AddIncomeAmount();
        return v;
    }

    private void AddIncomeAmount() {
        buttonAddIncome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(editTxtAddIncome.getText().toString().length() > 0) {
                    myDb.AddIncome(editTxtAddIncome.getText().toString());
                    Log.i("update", "Income Add/Updated");
                    Toast.makeText(getActivity(), "Income Added", Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(getActivity(),ViewExpense.class);
                    String frags = "ViewExpense";
                    intent.putExtra("to", frags);
                    startActivity(intent);

                }else{
                    Toast.makeText(getActivity(), "Fill up the field", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    
}
