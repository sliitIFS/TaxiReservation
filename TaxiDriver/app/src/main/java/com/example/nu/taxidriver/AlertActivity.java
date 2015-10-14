package com.example.nu.taxidriver;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.AsyncTask;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.location.LocationServices;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;


public class AlertActivity extends ActionBarActivity implements View.OnClickListener{
    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    TextView msgET, usertitleET;
    JSONParser jsonParser = new JSONParser();
    private static final String NOTIFY_URL = "http://taxires.site90.com/taxi/notifyPassenger.php?push=true";
    private static final String DECLINE_URL = "http://taxires.site90.com/taxi/DeclineAndSelectNextDriver.php?push=true";
    private ProgressDialog pDialog;
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";
    String id;
    String rest;
    String nic;
    TextView mTextField;
    Button ACC,DEC;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alert);

        // Intent Message sent from Broadcast Receiver
        String str = getIntent().getStringExtra("msg");

        // Get Email ID from Shared preferences
        SharedPreferences prefs = getSharedPreferences("MyPref",
                Context.MODE_PRIVATE);
        String eMailId = prefs.getString("email", "");
        // Set Title
        usertitleET = (TextView) findViewById(R.id.usertitle);
        mTextField=(TextView)findViewById(R.id.txtCountDown);
        ACC=(Button)findViewById(R.id.btnAcc);
        DEC=(Button)findViewById(R.id.btnDec);
        // Check if Google Play Service is installed in Device
        // Play services is needed to handle GCM stuffs
        if (!checkPlayServices()) {
            Toast.makeText(
                    getApplicationContext(),
                    "This device doesn't support Play services, App will not work normally",
                    Toast.LENGTH_LONG).show();
        }

        usertitleET.setText("Hello " + eMailId + " !");
        // When Message sent from Broadcase Receiver is not empty
        if (str != null) {
            // Set the message
            msgET = (TextView) findViewById(R.id.message);
            Log.e("string",str);
            StringTokenizer tokens = new StringTokenizer(str, "$");
String a=tokens.nextToken();
            msgET.setText(a);
            id=tokens.nextToken();
            Log.e("string",a);
            if(a.startsWith("Passenger"))
            {
                mTextField.setEnabled(false);
                mTextField.setVisibility(View.INVISIBLE);
                ACC.setVisibility(View.INVISIBLE);
                ACC.setEnabled(false);
                DEC.setEnabled(false);
                DEC.setVisibility(View.INVISIBLE);
            }
            else if(a.startsWith("Pick"))
            {
                mTextField.setEnabled(true);
                ACC.setEnabled(true);
                DEC.setEnabled(true);
            }
            //StringTokenizer tokens1 = new StringTokenizer(rest, "$");
        //  id= tokens1.nextToken();
            //nic=tokens1.nextToken();

           /* new AlertDialog.Builder(this)
                    .setTitle("You got a New Hire!!")
                    .setMessage("Do you want to accept this hire?\n")
                    .setPositiveButton("Accept", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            new notifyPassenger().execute();
                        }
                    })
                    .setNegativeButton("Decline", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // do nothing
                        }
                    })
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
*/
        }
        int secondsDelayed = 30;
        new Handler().postDelayed(new Runnable() {
            public void run() {
                startActivity(new Intent(AlertActivity.this, SetStatusActivity.class));
                finish();
            }
        }, secondsDelayed * 1000);
        new CountDownTimer(30000, 1000) {

            public void onTick(long millisUntilFinished) {
                mTextField.setText(millisUntilFinished / 1000+ " seconds remaining to accept the hire");
            }

            public void onFinish() {
                mTextField.setText("Hire Ignored!");
            }
        }.start();
    }

    @Override
    public void onClick(View v) {
        switch(v.getId())
        {

            case R.id.btnAcc:

                new NotifyPassenger().execute();
                Intent intent = new Intent(this,SetStatusActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.btnDec:

                new DeclineHire().execute();
                Intent i = new Intent(this,SetStatusActivity.class);
                startActivity(i);
                finish();
                break;
        }

    }

    class NotifyPassenger extends AsyncTask<String, String, String> {


        boolean failure = false;



        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(AlertActivity.this);
            pDialog.setMessage("Notifying Passenger...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
           // pDialog.show();

            // pDialog.dismiss();
        }



        @Override
        protected String doInBackground(String... args) {

           /*
            TODO Auto-generated method stub
            Check for success tag
            */

            int success;


            try {
                SharedPreferences s=getSharedPreferences("MyPref", Context.MODE_PRIVATE);


                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("email", s.getString("email", "")));
                params.add(new BasicNameValuePair("reqid", id));
             //   params.add(new BasicNameValuePair("drivernic",nic));
                Log.e("id",id);
//                Log.e("nic",nic);

                Log.d("request!", "starting");



                JSONObject json = jsonParser.makeHttpRequest(NOTIFY_URL, "POST", params);



                Log.d("Status Change attempt", json.toString());



                success = json.getInt(TAG_SUCCESS);

                if (success == 1) {

                    Log.d("Status Changed!", json.toString());



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

           // pDialog.dismiss();
            if (file_url != null){
               // Toast.makeText(AlertActivity.this, file_url, Toast.LENGTH_LONG).show();
            }

        }
    }

    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////




    class DeclineHire extends AsyncTask<String, String, String> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(AlertActivity.this);
            pDialog.setMessage("Decline...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();

            // pDialog.dismiss();
        }



        @Override
        protected String doInBackground(String... args) {

           /*
            TODO Auto-generated method stub
            Check for success tag
            */

            int success;


            try {
                SharedPreferences s=getSharedPreferences("MyPref", Context.MODE_PRIVATE);


                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("email", s.getString("email", "")));
                params.add(new BasicNameValuePair("reqid", id));
                //   params.add(new BasicNameValuePair("drivernic",nic));
                Log.e("id",id);
//                Log.e("nic",nic);

                Log.d("request!", "starting");



                JSONObject json = jsonParser.makeHttpRequest(DECLINE_URL, "POST", params);



                Log.d("Status Change attempt", json.toString());



                success = json.getInt(TAG_SUCCESS);

                if (success == 1) {

                    Log.d("Status Changed!", json.toString());



                    return json.getString(TAG_MESSAGE);

                }else{

                    Log.d("Decline Failure!", json.getString(TAG_MESSAGE));

                    return json.getString(TAG_MESSAGE);

                }

            } catch (JSONException e) {

                e.printStackTrace();

            }

            return null;

        }

        @Override
        protected void onPostExecute(String file_url){

            // pDialog.dismiss();
            if (file_url != null){
                // Toast.makeText(AlertActivity.this, file_url, Toast.LENGTH_LONG).show();
            }

        }
    }










    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////





    private boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil
                .isGooglePlayServicesAvailable(this);
        // When Play services not found in device
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                // Show Error dialog to install Play serviceskl
                GooglePlayServicesUtil.getErrorDialog(resultCode, this,PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                Toast.makeText(
                        getApplicationContext(),
                        "This device doesn't support Play services, App will not work normally",
                        Toast.LENGTH_LONG).show();
                finish();
            }
            return false;
        } else {
            Toast.makeText(
                    getApplicationContext(),
                    "This device supports Play services, App will work normally",
                    Toast.LENGTH_LONG).show();
        }
        return true;
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_alert, menu);
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
}
