package com.example.marrenmatias.trynavdrawer;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Andrea Mae on 12/18/2016.
 */
public class SignUp extends AppCompatActivity implements View.OnClickListener {
    private static final String REGISTER_URL = "http://172.16.38.104/ThriftyAdmin/RegistrationAndLogin/volleyRegister.php";

    public static final String KEY_FIRSTNAME = "firstname";
    public static final String KEY_LASTNAME = "lastname";
    public static final String KEY_USERNAME = "username";
    public static final String KEY_EMAIL = "email";
    public static final String KEY_PASSWORD = "password";

    private EditText editTextFname;
    private EditText editTextLname;
    private EditText editTextUsername;
    private EditText editTextEmail;
    private EditText editTextPassword;
    private EditText editTextPassword1;

    private ImageButton buttonSignUp;

    DatabaseHelper helper;
    ImageButton dp;
    TextView tf;
    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_up);

        helper = new DatabaseHelper(this);

        int dppos =123;

        editTextFname = (EditText) findViewById(R.id.editTextFname);
        editTextLname = (EditText) findViewById(R.id.editTextLname);
        editTextUsername = (EditText) findViewById(R.id.editTextUsername);
        editTextPassword = (EditText) findViewById(R.id.editTextPassword);
        editTextEmail = (EditText) findViewById(R.id.editTextEmail);
        editTextPassword1 = (EditText) findViewById(R.id.editTextPassword1);

        buttonSignUp = (ImageButton) findViewById(R.id.buttonSignUp);

        buttonSignUp.setOnClickListener(this);

        tf = (TextView) findViewById(R.id.thrift);
        Typeface myCustomFont = Typeface.createFromAsset(getAssets(), "fonts/BebasNeue.otf");
        Typeface sCustomFont = Typeface.createFromAsset(getAssets(),"fonts/King-Basil-Lite.otf");
        /*fname.setTypeface(myCustomFont);
        lname.setTypeface(myCustomFont);
        uname.setTypeface(myCustomFont);
        email.setTypeface(myCustomFont);
        pass1.setTypeface(myCustomFont);
        pass2.setTypeface(myCustomFont);
        tf.setTypeface(sCustomFont);*/
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

    /*public void onSignUpClick(View v) {
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
            }

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
    }*/

    private void registerUser(){
        final String firstname = editTextFname.getText().toString().trim();
        final String lastname = editTextLname.getText().toString().trim();
        final String username = editTextUsername.getText().toString().trim();
        final String password = editTextPassword.getText().toString().trim();
        final String password1 = editTextPassword1.getText().toString().trim();
        final String email = editTextEmail.getText().toString().trim();


        StringRequest stringRequest = new StringRequest(Request.Method.POST, REGISTER_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(SignUp.this, response, Toast.LENGTH_LONG).show();
                        Intent i = new Intent(SignUp.this, SetIncome.class);
                        startActivity(i);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(SignUp.this,error.toString(),Toast.LENGTH_LONG).show();
                    }
                }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put(KEY_FIRSTNAME,firstname);
                params.put(KEY_LASTNAME,lastname);
                params.put(KEY_USERNAME,username);
                params.put(KEY_PASSWORD,password);
                params.put(KEY_EMAIL, email);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    @Override
    public void onClick(View v) {
        registerUser();
    }
}