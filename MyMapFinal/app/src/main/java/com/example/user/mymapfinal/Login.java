package com.example.user.mymapfinal;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.loopj.android.http.RequestParams;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class Login extends Activity implements View.OnClickListener {
    private EditText emailField,passwordField;

    private Button btnlogin;
    private Button btnforgotPwd;
    private ProgressDialog pDialog;
    String dbemail;

    JSONParser jsonParser = new JSONParser();
    private static final String LOGIN_URL = "http://taxires.site90.com/taxi/passengerLogin.php";
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";


    public Login(){}
    RequestParams params1 = new RequestParams();
    GoogleCloudMessaging gcmObj;
    Context applicationContext;
    String regId = "";
    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;

    AsyncTask<Void, Void, String> createRegIdTask;

    public static final String REG_ID = "regId";
    public static final String EMAIL_ID = "emailId";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        SharedPreferences pref = getApplicationContext().getSharedPreferences("Location", 0);
        pref.edit().clear().commit();

        SharedPreferences prefBook = getApplicationContext().getSharedPreferences("BookLocation", 0);
        prefBook.edit().clear().commit();

        btnlogin = (Button)findViewById(R.id.button);
        //btnlogin.setOnClickListener(this);

        btnlogin.setOnClickListener(this);
        applicationContext = getApplicationContext();
        //getSupportActionBar().hide();

        emailField = (EditText)findViewById(R.id.editTextEmail);
        passwordField = (EditText)findViewById(R.id.editTextPassword);
        btnforgotPwd = (Button)findViewById(R.id.button2);

        btnforgotPwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Login.this, ResetPassword.class);
                startActivity(intent);
            }
        });
        emailField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(emailField.getText().length()==0){

                    emailField.setError("Email Field Cannot be empty");

                }


                // int x = email.indexOf("@");
                //int y = email.indexOf(".");




            }
        });

        passwordField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String email = emailField.getText().toString();
                if(!(email.matches("[a-zA-Z0-9._-]+@[a-z]+.[a-z]+"))){

                    emailField.setError("Email is incorrect");
                }

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (passwordField.getText().length() == 0) {

                    passwordField.setError("Password Cannot be empty");

                }

                if (passwordField.getText().length() <= 2) {


                }

            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    class PassengerLogin extends AsyncTask<String, String, String> {


        boolean failure = false;



        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(Login.this);
            pDialog.setMessage("Attempting login...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();

            // pDialog.dismiss();
        }


        private boolean isValidPassword(String pass) {
            if (pass != null && pass.length() >= 2) {
                return true;
            }
            return false;
        }
        @Override
        protected String doInBackground(String... args) {

           /*
            TODO Auto-generated method stub
            Check for success tag
            */

            int success;

            String email= emailField.getText().toString();
            String password = passwordField.getText().toString();

            String msg = "";
            try {
                if (gcmObj == null) {
                    gcmObj = GoogleCloudMessaging
                            .getInstance(applicationContext);
                }
                regId = gcmObj.register(ApplicationConstants.GOOGLE_PROJ_ID);
                msg = "Registration ID :" + regId;

            } catch (IOException ex) {
                msg = "Error :" + ex.getMessage();
            }

            try {

                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("email", email));
                params.add(new BasicNameValuePair("password", password));
                params.add(new BasicNameValuePair("regId",regId));

                Log.d("request!", "starting");

                JSONObject json = jsonParser.makeHttpRequest(LOGIN_URL, "POST", params);
              //  httpPost.setHeader("User-Agent","Mozilla/5.0 (Macintosh; Intel Mac OS X 10_9_5) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/43.0.2357.81 Safari/537.36");

                Log.d("Login attempt", json.toString());

                success = json.getInt(TAG_SUCCESS);

                if (success == 1) {

                    dbemail=json.getString("email");
                    Log.d("Login Successful!", json.toString());
                    Intent intent = new Intent(getApplicationContext(), MyMain.class);
                    //Create a bundle object
                    Bundle b = new Bundle();

                    //Inserts a String value into the mapping of this Bundle
                    b.putString("email", email);
                    b.putString("password", password);

                    //Add the bundle to the intent.
                    intent.putExtras(b);

                    //start the DisplayActivity
                    startActivity(intent);




                    SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0);
                    SharedPreferences.Editor editor = pref.edit();

                    editor.putString("email", dbemail);
                    // editor.putString("nic",dbnic);
                    editor.commit();
                    Log.e("email",dbemail);
                    Intent i = new Intent(Login.this, MyMain.class);
                    i.putExtra("email",dbemail);
                    finish();

                    startActivity(i);

                    return json.getString(TAG_MESSAGE);


                }else{

                    Log.d("Login Failure!", json.getString(TAG_MESSAGE));

                    return json.getString(TAG_MESSAGE);

                }

            } catch (JSONException e) {

                e.printStackTrace();

            }

            return null;

        }

        @Override
        protected void onPostExecute(String file_url){

            pDialog.dismiss();
            if (file_url != null){
                Toast.makeText(Login.this, file_url, Toast.LENGTH_LONG).show();
            }

           // httpPost.setHeader("User-Agent","Mozilla/5.0 (Macintosh; Intel Mac OS X 10_9_5) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/43.0.2357.81 Safari/537.36");

        }
    }

    @Override
    public void onClick(View v) {
        switch(v.getId())
        {

            case R.id.button:

                new PassengerLogin().execute();
                break;
        }

    }



}

