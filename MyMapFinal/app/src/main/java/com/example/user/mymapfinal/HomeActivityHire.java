package com.example.user.mymapfinal;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by user on 7/24/2015.
 */
public class HomeActivityHire extends ActionBarActivity implements View.OnClickListener{

    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    private static final String STATUS_UPDATE_URL = "http://taxires.site90.com/taxi/updateRequestStatus.php";
    private static final String NOTIFY_DRIVER_URL = "http://taxires.site90.com/taxi/GetNearestDriverWithloop.php?push=true";
    TextView msgET, usertitleET;
    private ProgressDialog pDialog;
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";
    JSONParser jsonParser = new JSONParser();
    Button ok,can,tryA;
    String reqid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hire_home);
        ok=(Button)findViewById(R.id.btnOK);
        can=(Button)findViewById(R.id.btnCan);
        tryA=(Button)findViewById(R.id.btnTry);

        // Intent Message sent from Broadcast Receiver
        String str = getIntent().getStringExtra("msg");

        // Get Email ID from Shared preferences
        SharedPreferences prefs = getSharedPreferences("MyPref",
                Context.MODE_PRIVATE);
        String eMailId = prefs.getString("email", "");
        // Set Title
        usertitleET = (TextView) findViewById(R.id.usertitle);

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


            Log.e("string", str);
            String a[]=str.split("\n");

            if(a[1].equals("No available drivers"))
            {
                msgET.setText(a[1]);
             reqid=a[2];
               // tryA.setVisibility(View.VISIBLE);
                tryA.setEnabled(true);
             //   can.setVisibility(View.VISIBLE);
             can.setEnabled(true);
              //  ok.setVisibility(View.INVISIBLE);
                ok.setEnabled(false);
            }
            else
            {

                msgET.setText(a[0]+"\n"+a[1]+"\n"+a[2]+"\n"+a[3]+"\n"+a[4]);
               // tryA.setVisibility(View.INVISIBLE);
                //can.setVisibility(View.INVISIBLE);
               // ok.setVisibility(View.VISIBLE);

                tryA.setEnabled(false);
                can.setEnabled(false);
                ok.setEnabled(true);
            }
            }
         /*   new AlertDialog.Builder(this)
                    .setTitle("Hire Accepted !!")
                    .setMessage("Do you want to have a look at driver details ?\n")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // continue with delete
                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // do nothing
                        }
                    })
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
*/
        //}
    }

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
        getMenuInflater().inflate(R.menu.menu_home, menu);
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

    @Override
    public void onClick(View v) {
        switch(v.getId())
        {

            case R.id.btnOK:


                Intent intent = new Intent(this,ThankActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.btnCan:

                new UpdateRequestStatusC().execute();
                break;
            case R.id.btnTry:


                new NotifyDriver().execute();
                break;

        }
    }
    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    class UpdateRequestStatusC extends AsyncTask<String, String, String> {


        boolean failure = false;


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(HomeActivityHire.this);
            pDialog.setMessage("Canceling...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();

            // pDialog.dismiss();
        }


        private boolean isValidPassword(String pass) {
            if (pass != null && pass.length() >= 3) {
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

            try {

                SharedPreferences s = getSharedPreferences("MyPref", Context.MODE_PRIVATE);


                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("Request_ID", reqid));

                params.add(new BasicNameValuePair("status", "Canceled"));
                Log.d("request!", "starting");

                JSONObject json = jsonParser.makeHttpRequest(STATUS_UPDATE_URL, "POST", params);

                Log.d("Request attempt", json.toString());

                success = json.getInt(TAG_SUCCESS);

                if (success == 1) {


                    Log.d("Requested!", json.toString());
                    //  Intent intent = new Intent(getApplicationContext(), Reservation.class);
                    //Create a bundle object

                    //  startActivity(intent);


                    return json.getString(TAG_MESSAGE);


                } else {

                    Log.d("Request Failure!", json.getString(TAG_MESSAGE));

                    return json.getString(TAG_MESSAGE);

                }

            } catch (JSONException e) {

                e.printStackTrace();

            }

            return null;

        }

        @Override
        protected void onPostExecute(String file_url) {

            pDialog.dismiss();
            if (file_url != null) {
                // Toast.makeText(Reservation.this, file_url, Toast.LENGTH_LONG).show();
            }

        }
    }

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    class NotifyDriver extends AsyncTask<String, String, String> {


        boolean failure = false;



        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(HomeActivityHire.this);
            pDialog.setMessage("notifying..");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
//            pDialog.show();

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

                SharedPreferences s=getSharedPreferences("MyPref",Context.MODE_PRIVATE);

                Log.d("notify!", "starting");

                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("Request_ID", reqid));

                JSONObject json = jsonParser.makeHttpRequest(NOTIFY_DRIVER_URL, "POST",params);

                Log.d("Request attempt", json.toString());

                success = json.getInt(TAG_SUCCESS);

                if (success == 1) {

                    //    dbNic=json.getString("nic");
//                    dbReqId = json.getString("reqId");
                    Log.d("Selected!", json.toString());

                    return json.getString(TAG_MESSAGE);

                }else{

                    Log.d("Selection Failure!", json.getString(TAG_MESSAGE));

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

                // Toast.makeText(Reservation.this, file_url, Toast.LENGTH_LONG).show();
            }

        }
    }



}
