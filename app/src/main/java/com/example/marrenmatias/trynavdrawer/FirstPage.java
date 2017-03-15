package com.example.marrenmatias.trynavdrawer;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import org.json.JSONException;
import org.json.JSONObject;

public class FirstPage extends AppCompatActivity {

    private TextView m,t,f,o;
    private Button li;
    private ImageButton si;
    DatabaseHelper helper;
    SQLiteDatabase db;
    Cursor c;

    String data;
    String returnString;
    CallbackManager callbackManager;
    LoginButton login;

    private static final String SELECT_SQL = "SELECT count(*) FROM contact WHERE ACTIVE = 1";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        helper = new DatabaseHelper(this);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.first__page);

        openDatabase();
        ContentValues values = new ContentValues();
        callbackManager = CallbackManager.Factory.create();
        login = (LoginButton) findViewById(R.id.login_button);
        login.setReadPermissions("public_profile email user_birthday");

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (AccessToken.getCurrentAccessToken() != null) {
                    storeData();
                    Intent i = new Intent(FirstPage.this, SetIncome.class);
                    startActivity(i);
                }
            }
        });

        login.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {

                if (AccessToken.getCurrentAccessToken() != null) {
                    storeData();
                    Intent i = new Intent(FirstPage.this, SetIncome.class);
                    startActivity(i);
                }
            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException exception) {
            }
        });

        m= (TextView)findViewById(R.id.manage);
        Typeface myCustomFont =Typeface.createFromAsset(getAssets(),"fonts/BebasNeue.otf");
        m.setTypeface(myCustomFont);
        t= (TextView) findViewById(R.id.thrifty);
        Typeface sCustomFont = Typeface.createFromAsset(getAssets(),"fonts/King-Basil-Lite.otf");
        t.setTypeface(sCustomFont);
        f= (TextView) findViewById(R.id.forgotPass);
        f.setTypeface(myCustomFont);
        o= (TextView) findViewById(R.id.or);
        o.setTypeface(myCustomFont);
        li = (Button) findViewById(R.id.loginButton);
        li.setTypeface(myCustomFont);
        si = (ImageButton) findViewById(R.id.signUpButton);


        openDatabase();
        c = db.rawQuery(SELECT_SQL, null);
        c.moveToFirst();
        int count = c.getInt(0);
        if(count > 0)
        {
            Cursor check = db.rawQuery("SELECT * FROM contact WHERE ACTIVE = 1", null);
            check.moveToFirst();

            Intent i = new Intent(FirstPage.this, SetIncome.class);
            startActivity(i);
        }

        si.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(FirstPage.this, SignUp.class);
                startActivity(i);
            }
        });
    }

    public void onButtonClick(View v)
    {
        if (v.getId() == R.id.loginButton)
        {

            EditText a = (EditText)findViewById(R.id.editTextUsername);
            String str = a.getText().toString();

            EditText b = (EditText)findViewById(R.id.editTextPassword);
            String pass = b.getText().toString();

            String password = helper.searchPass(str);
            if (pass.equals(password))
            {
                helper.activate();
                Intent i = new Intent(FirstPage.this, display.class);
                i.putExtra("Username", str);
                startActivity(i);
            }
            else
            {
                Toast temp = Toast.makeText(FirstPage.this, "Username and Password do not match!", Toast.LENGTH_SHORT);
                temp.show();
            }

        }
      /* else if (v.getId() == R.id.signUpButton)
        {
            Intent i = new Intent(FirstPage.this, SignUp.class);
            startActivity(i);
        }*/
    }

    protected void openDatabase() {
        db = openOrCreateDatabase("THRIFTY.db", Context.MODE_PRIVATE, null);
    }

    public void storeData() {
        GraphRequest request2 = GraphRequest.newMeRequest(AccessToken.getCurrentAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
            @Override
            public void onCompleted(JSONObject object, GraphResponse response) {

                JSONObject json = response.getJSONObject();
                try {
                    if (json != null) {
                        String fname = json.getString("first_name");
                        String lname = json.getString("last_name");
                        String email = json.getString("email");
                        String birthday = json.getString("birthday");
                        String id = json.getString("id");
                        helper.insertFB(id, fname,lname, email, birthday);

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,email,birthday, first_name, last_name");
        request2.setParameters(parameters);
        request2.executeAsync();
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }
}
