package com.example.marrenmatias.trynavdrawer;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by Andrea Mae on 12/18/2016.
 */
public class SignUp extends AppCompatActivity {
    EditText fname,lname,email,uname,pass1,pass2;
    DatabaseHelper helper;
    ImageButton dp;
    TextView tf;
    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_up);

        helper = new DatabaseHelper(this);

        int dppos =123;

        fname = (EditText)findViewById(R.id.editTextFName);
        lname = (EditText)findViewById(R.id.editTextLName);
        email = (EditText)findViewById(R.id.editTextEmail);
        uname = (EditText)findViewById(R.id.editTextUname);
        pass1 = (EditText)findViewById(R.id.editTextPass1);
        pass2 = (EditText)findViewById(R.id.editTextPass2);
        tf = (TextView) findViewById(R.id.thrift);
        Typeface myCustomFont = Typeface.createFromAsset(getAssets(), "fonts/BebasNeue.otf");
        Typeface sCustomFont = Typeface.createFromAsset(getAssets(),"fonts/King-Basil-Lite.otf");
        fname.setTypeface(myCustomFont);
        lname.setTypeface(myCustomFont);
        uname.setTypeface(myCustomFont);
        email.setTypeface(myCustomFont);
        pass1.setTypeface(myCustomFont);
        pass2.setTypeface(myCustomFont);
        tf.setTypeface(sCustomFont);
        dp = (ImageButton) findViewById(R.id.changeDP);
        dp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(SignUp.this, ChangePic.class);
                startActivity(i);
            }
        });

        Intent i = getIntent();
        dppos = i.getIntExtra("id",123);
        Log.d("Test1", "Test1" + dppos);
        if (dppos == 0) {
            dp.setImageResource(R.drawable.dp1);
        }
        else if (dppos == 1) {
            dp.setImageResource(R.drawable.dp2);
        }
        else if (dppos == 2) {
            dp.setImageResource(R.drawable.dp3);
        }
        else if (dppos == 3) {
            dp.setImageResource(R.drawable.dp4);
        }
        else if (dppos == 4) {
            dp.setImageResource(R.drawable.dp5);
        }
        else if (dppos == 5) {
            dp.setImageResource(R.drawable.dp6);
        }
        else if (dppos == 6) {
            dp.setImageResource(R.drawable.dp7);
        }
        else if (dppos == 7) {
            dp.setImageResource(R.drawable.dp8);
        }

    }

    public void onSignUpClick(View v) {
        if (v.getId() == R.id.buttonSignUp)
        {
            String fnamestr = fname.getText().toString();
            String lnametr = lname.getText().toString();
            String emailstr = email.getText().toString();
            String unamestr = uname.getText().toString();
            String pass1str = pass1.getText().toString();
            String pass2str = pass2.getText().toString();

            if (!pass1str.equals(pass2str))
            {
                //DISPLAY A POP UP MESSAGE :D
                Toast pass = Toast.makeText(SignUp.this, "Passwords do not match!", Toast.LENGTH_SHORT);
                pass.show();
                //Toast.makeText(getApplicationContext(), "Passwords do not match!", Toast.LENGTH_SHORT).show();
            }

            //TOAST KAPAG NAG-EEXIST NA YUNG USERNAME
            /*else if (!pass1str.equals(pass2str))
            {
                //IF UNAME EXIST, di napasok
                Toast pass = Toast.makeText(SignUp.this, "Username already exist.", Toast.LENGTH_SHORT);
                pass.show();
            }*/

            else {
                //insert the details in the database (from the signup form)
                Contact c = new Contact();
                c.setFname(fnamestr);
                c.setLname(lnametr);
                c.setEmail(emailstr);
                c.setUname(unamestr);
                c.setPass(pass1str);

                helper.insertContact(c);

                Intent i = new Intent(SignUp.this, SetIncome.class);
                startActivity(i);
            }
        }
    }
}