package com.example.user.mymapfinal;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;


public class RateDriver extends Activity implements View.OnClickListener, AdapterView.OnItemSelectedListener{
    ArrayList<String> listItems = new ArrayList<String>();
    private ProgressDialog pDialog;
    ArrayAdapter<String> adapter;
    Spinner sp;
    RatingBar ratingbar1;
    Button button;
    TextView rateNo;
    EditText commentDriver;
    private static final String RATE_DRIVER_URL = "http://taxires.site90.com/taxi/rateDrivers.php";
    private static final String CAL_RATE_DRIVER_URL = "http://taxires.site90.com/taxi/calRating.php";
    JSONParser jsonParser = new JSONParser();
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";
    private int success;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rate_driver);

        addListenerOnButtonClick();
        sp = (Spinner) findViewById(R.id.spinner);
        commentDriver = (EditText) findViewById(R.id.editText);
        adapter = new ArrayAdapter<String>(this, R.layout.spinner_layout, R.id.txt, listItems);
        sp.setAdapter(adapter);


    }

    public void addListenerOnButtonClick() {
        ratingbar1 = (RatingBar) findViewById(R.id.ratingBar);
        button = (Button) findViewById(R.id.button);
        rateNo = (TextView) findViewById(R.id.textView6);
        //Performing action on Button Click
        button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                //Getting the rating and displaying it on the toast
                String rating = String.valueOf(ratingbar1.getRating());
                rateNo.setText(rating);
                //Toast.makeText(getApplicationContext(), rating, Toast.LENGTH_LONG).show();
                new InsertRating().execute();
                new CalRating().execute();

            }

        });
    }

    public void onStart() {
        super.onStart();
        BackTask bt = new BackTask();
        bt.execute();

    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.button:


        }


    }

    class InsertRating extends AsyncTask<String, String, String> {

        String name = sp.getSelectedItem().toString();
        String comment = commentDriver.getText().toString();
        String stars = rateNo.getText().toString();

        //capture values from EditText

        /** * Before starting background thread Show Progress Dialog * */
        @Override protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(RateDriver.this);
            pDialog.setMessage("Saving.....");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        protected String doInBackground(String... args) {

            // Building Parameters
            //NameValuePair is a special <Key, Value> pair which is used to represent parameters in http request
            List<NameValuePair> params = new ArrayList<NameValuePair>();

            if(!name.matches("") && !comment.matches("") && !stars.matches("")) {

                params.add(new BasicNameValuePair("name", name));
                params.add(new BasicNameValuePair("comment", comment));
                params.add(new BasicNameValuePair("stars", stars));
                params.add(new BasicNameValuePair("hits", "0"));

                Log.e("userPass", String.valueOf(params));
            }

            //parse json from url to object
            // getting JSON Object
            ServiceHandler jsonParser  = new ServiceHandler();

            String json = jsonParser.makeServiceCall(RATE_DRIVER_URL,
                    ServiceHandler.POST, params);

            JSONObject jsonObj = null;

            if (json != null) {
                try {
                    jsonObj = new JSONObject(json);
                    success = jsonObj.getInt(TAG_SUCCESS);
                    // checking for error node in json
                    if (success == 1) {



                        Log.d("Requested!", jsonObj.toString());

                        return jsonObj.getString(TAG_MESSAGE);


                    }else{

                        Log.d("Request Failure!", jsonObj.getString(TAG_MESSAGE));

                        return jsonObj.getString(TAG_MESSAGE);

                    }
                } catch (JSONException e) {
                    //e.printStackTrace();
                }

            } else {
                Log.e("JSON Data", "JSON data error!");
            }
            return null;
        }

        protected void onPostExecute(String file_url) {
            // dismiss the dialog once done
            pDialog.dismiss();

        }
    }

    class CalRating extends AsyncTask<String, String, String> {


        boolean failure = false;



        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(RateDriver.this);
            pDialog.setMessage("fulfilling..");
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

            List<NameValuePair> params = new ArrayList<NameValuePair>();


            ServiceHandler jsonParser  = new ServiceHandler();

            String json = jsonParser.makeServiceCall(CAL_RATE_DRIVER_URL,
                    ServiceHandler.POST, params);

            JSONObject jsonObj = null;

            if (json != null) {
                try {
                    jsonObj = new JSONObject(json);
                    success = jsonObj.getInt(TAG_SUCCESS);
                    // checking for error node in json
                    if (success == 1) {



                        Log.d("Requested!", jsonObj.toString());

                        return jsonObj.getString(TAG_MESSAGE);


                    }else{

                        Log.d("Request Failure!", jsonObj.getString(TAG_MESSAGE));

                        return jsonObj.getString(TAG_MESSAGE);

                    }
                } catch (JSONException e) {
                    //e.printStackTrace();
                }

            } else {
                Log.e("JSON Data", "JSON data error!");
            }
            return null;
        }

        protected void onPostExecute(String file_url) {
            // dismiss the dialog once done
            pDialog.dismiss();

        }
    }


    private class BackTask extends AsyncTask<Void, Void, Void> {
        ArrayList<String> list;

        protected void onPreExecute() {
            super.onPreExecute();
            list = new ArrayList<String>();
        }

        protected Void doInBackground(Void... params) {
            InputStream is = null;
            String result = "";
            try {
                HttpClient httpclient = new DefaultHttpClient();
                HttpPost httppost = new HttpPost("http://taxires.site90.com/taxi/loadDrivers.php");
                HttpResponse response = httpclient.execute(httppost);
                HttpEntity entity = response.getEntity();
                // Get our response as a String.
                is = entity.getContent();
            } catch (IOException e) {
                e.printStackTrace();
            }

            //convert response to string
            try {
                BufferedReader reader = new BufferedReader(new InputStreamReader(is, "utf-8"));
                String line = null;
                while ((line = reader.readLine()) != null) {
                    result += line;
                }
                is.close();
                //result=sb.toString();
            } catch (Exception e) {
                e.printStackTrace();
            }
            // parse json data
            try {
                JSONArray jArray = new JSONArray(result);
                for (int i = 0; i < jArray.length(); i++) {
                    JSONObject jsonObject = jArray.getJSONObject(i);
                    // add interviewee name to arraylist
                    list.add(jsonObject.getString("FName"));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute(Void result) {
            listItems.addAll(list);
            adapter.notifyDataSetChanged();
        }
    }
//////////////////////////////////////////////////

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_rate_driver, menu);
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
