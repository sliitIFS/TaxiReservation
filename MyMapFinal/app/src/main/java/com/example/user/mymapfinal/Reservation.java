package com.example.user.mymapfinal;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;


public class Reservation extends Activity implements AdapterView.OnItemClickListener,AdapterView.OnItemSelectedListener {

    String dbNic, dbReqId;

    private static final String LOG_TAG = "Google Places Autocomplete";
    private static final String PLACES_API_BASE = "https://maps.googleapis.com/maps/api/place";
    private static final String TYPE_AUTOCOMPLETE = "/autocomplete";
    private static final String OUT_JSON = "/json";

    private static final String API_KEY = "AIzaSyDJqgBG9nIWSYbW9y7AZW8w40UM7_dnAt0";
    AutoCompleteTextView autoCompView;
    AutoCompleteTextView autoCompViewDES;
    String id;
    String address;
    private ProgressDialog pDialog;
    Spinner TypeSpinner;
    String lat,lng;
    private GoogleMap mMap;

    JSONParser jsonParser = new JSONParser();
    private static final String Request_URL = "http://taxires.site90.com/taxi/passengerRequest.php";
    private static final String STATUS_UPDATE_URL = "http://taxires.site90.com/taxi/updateRequestStatus.php";
    private static final String NEAREST_DRIVER_URL = "http://taxires.site90.com/taxi/GetNearestDriverWithloop.php";
    private static final String NOTIFY_DRIVER_URL = "http://taxires.site90.com/taxi/GetNearestDriverWithloop.php?push=true";
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";
    Spinner spinnerV;
    DBHelper db=new DBHelper(this);
    LocationManager lm;
    String provider;
    Location l;
    LatLng latLng;
    String dest;
    String latsorce,lngsorce;
    String source;
    String autoSourceAdd;
    @Override
        protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reservation);
        spinnerV = (Spinner) findViewById(R.id.spinner);
        autoCompView = (AutoCompleteTextView) findViewById(R.id.pick_places);

        autoCompView.setAdapter(new GooglePlacesAutocompleteAdapter(this, R.layout.list_item));
        autoCompView.setOnItemClickListener(this);

        SharedPreferences s=getSharedPreferences("Location", Context.MODE_PRIVATE);
        NavigationSource();

        autoCompView.setText(s.getString("sourceAdd", ""));


        autoCompViewDES = (AutoCompleteTextView) findViewById(R.id.des_places);

        autoCompViewDES.setAdapter(new GooglePlacesAutocompleteAdapterDestination(this, R.layout.list_item));
        autoCompViewDES.setOnItemClickListener(this);


        NavigationDes();

        autoCompViewDES.setText(s.getString("destAdd",""));


// Create an ArrayAdapter using the string array and a default spinner layout

        final ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.vehicle_array, android.R.layout.simple_spinner_item);

// Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// Apply the adapter to the spinner
        spinnerV.setAdapter(adapter);
        spinnerV.setOnItemSelectedListener(this);


       // lng= Double.parseDouble(autoCompView.getText().toString());


        Button favourites=(Button) findViewById(R.id.button2);
        favourites.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Intent intent = new Intent(Reservation.this,FavouriteList.class);


                //=================================================================================
                final AlertDialog.Builder alert = new AlertDialog.Builder(Reservation.this);
                final EditText input = new EditText(Reservation.this);
                alert.setView(input);
                alert.setTitle("Add To Favourites");
                alert.setMessage("Enter Name for Favourite!!");
                alert.setIcon(R.drawable.favouriteicon);

                alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        String value = input.getText().toString().trim();
                        db.open();
                        db.insertRecord(value, autoCompView.getText().toString(), autoCompViewDES.getText().toString(), spinnerV.getSelectedItem().toString());
                        db.close();
                        //intent.putExtra("fname",value);
                        Toast.makeText(getApplicationContext(), value, Toast.LENGTH_SHORT).show();
                        startActivity(intent);
                    }
                });

                alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        dialog.cancel();
                        startActivity(intent);
                    }
                });
                alert.show();
                //================================================================================
                //startActivity(intent);

            }
        });

        // check if you are connected or not



        Button look=(Button)findViewById(R.id.button);
        look.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch(v.getId()){
                    case R.id.button:
                        if(!validate())
                            Toast.makeText(getBaseContext(), "Enter some data!", Toast.LENGTH_LONG).show();
                        // call AsynTask to perform network operation on separate thread
                        //new HttpAsyncTask().execute("http://taxires.site90.com/taxi/passenger.php");



                        new PassengerRequest().execute();
                     //   new SelectNearestDriver().execute();
                       new NotifyDriver().execute();


                        //startActivity(new Intent(getApplicationContext(), MainActivityHire.class));

                        break;
                }

            }
        });


    }

    public void getLatLongFromAddress(String youraddress) {
        String uri = "http://maps.google.com/maps/api/geocode/json?address=" +
                URLEncoder.encode(youraddress) + "&sensor=false";
        HttpGet httpGet = new HttpGet(uri);
        HttpClient client = new DefaultHttpClient();
        HttpResponse response;
        StringBuilder stringBuilder = new StringBuilder();

        try {
            response = client.execute(httpGet);
            HttpEntity entity = response.getEntity();
            InputStream stream = entity.getContent();
            int b;
            while ((b = stream.read()) != -1) {
                stringBuilder.append((char) b);
            }
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject = new JSONObject(stringBuilder.toString());

            Double lng = ((JSONArray)jsonObject.get("results")).getJSONObject(0)
                    .getJSONObject("geometry").getJSONObject("location")
                    .getDouble("lng");

            Double lat = ((JSONArray)jsonObject.get("results")).getJSONObject(0)
                    .getJSONObject("geometry").getJSONObject("location")
                    .getDouble("lat");

            Log.d("latitude", String.valueOf(lat));
            Log.d("longitude", String.valueOf(lng));
            String autosource=String.valueOf(lat)+","+ String.valueOf(lng);
            Log.e("autosource", autosource);
            SharedPreferences pref = getApplicationContext().getSharedPreferences("Location", 0);
            SharedPreferences.Editor editor = pref.edit();

            editor.putString("source", autosource);

            editor.commit();



        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


    public void getLatLongFromAddressDes(String youraddress) {
        String uri = "http://maps.google.com/maps/api/geocode/json?address=" +
                URLEncoder.encode(youraddress) + "&sensor=false";
        //URLEncoder.encode(uri)
        HttpGet httpGet = new HttpGet(uri);
        HttpClient client = new DefaultHttpClient();
        HttpResponse response;
        StringBuilder stringBuilder = new StringBuilder();

        try {
            response = client.execute(httpGet);
            HttpEntity entity = response.getEntity();
            InputStream stream = entity.getContent();
            int b;
            while ((b = stream.read()) != -1) {
                stringBuilder.append((char) b);
            }
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject = new JSONObject(stringBuilder.toString());

            Double lng = ((JSONArray)jsonObject.get("results")).getJSONObject(0)
                    .getJSONObject("geometry").getJSONObject("location")
                    .getDouble("lng");

            Double lat = ((JSONArray)jsonObject.get("results")).getJSONObject(0)
                    .getJSONObject("geometry").getJSONObject("location")
                    .getDouble("lat");

            Log.d("latitude", String.valueOf(lat));
            Log.d("longitude", String.valueOf(lng));
            String autodes=String.valueOf(lat)+","+ String.valueOf(lng);
            Log.e("autodes", autodes);
            SharedPreferences pref = getApplicationContext().getSharedPreferences("Location", 0);
            SharedPreferences.Editor editor = pref.edit();

            editor.putString("dest", autodes);

            editor.commit();



        } catch (JSONException e) {
            e.printStackTrace();
        }

    }



    private boolean validate(){
        if(autoCompView.getText().toString().trim().equals(""))
            return false;
        else if(autoCompViewDES.getText().toString().trim().equals(""))
            return false;
            // else if(etTwitter.getText().toString().trim().equals(""))
            //   return false;
        else
            return true;
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_reservation, menu);
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
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {


    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public static ArrayList autocomplete(String input) {
        ArrayList resultList = null;

        HttpURLConnection conn = null;
        StringBuilder jsonResults = new StringBuilder();
        try {
            StringBuilder sb = new StringBuilder(PLACES_API_BASE + TYPE_AUTOCOMPLETE + OUT_JSON);
            sb.append("?key=" + API_KEY);
            sb.append("&components=country:lk");
            sb.append("&input=" + URLEncoder.encode(input, "utf8"));

            URL url = new URL(sb.toString());
            conn = (HttpURLConnection) url.openConnection();
            InputStreamReader in = new InputStreamReader(conn.getInputStream());

            // Load the results into a StringBuilder
            int read;
            char[] buff = new char[1024];
            while ((read = in.read(buff)) != -1) {
                jsonResults.append(buff, 0, read);
            }
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Error processing Places API URL", e);
            return resultList;
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error connecting to Places API", e);
            return resultList;
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }

        try {
            // Create a JSON object hierarchy from the results
            JSONObject jsonObj = new JSONObject(jsonResults.toString());
            JSONArray predsJsonArray = jsonObj.getJSONArray("predictions");

            // Extract the Place descriptions from the results
            resultList = new ArrayList(predsJsonArray.length());
            for (int i = 0; i < predsJsonArray.length(); i++) {
                System.out.println(predsJsonArray.getJSONObject(i).getString("description"));
                System.out.println("============================================================");
                resultList.add(predsJsonArray.getJSONObject(i).getString("description"));
            }
        } catch (JSONException e) {
            Log.e(LOG_TAG, "Cannot process JSON results", e);
        }

        return resultList;
    }

    class GooglePlacesAutocompleteAdapter extends ArrayAdapter implements Filterable {
        private ArrayList resultList;

        public GooglePlacesAutocompleteAdapter(Context context, int textViewResourceId) {
            super(context, textViewResourceId);
        }

        @Override
        public int getCount() {
            return resultList.size();
        }

        @Override
        public String getItem(int index) {
            return (String) resultList.get(index);
        }


        @Override
        public Filter getFilter() {
            Filter filter = new Filter() {
                @Override
                protected FilterResults performFiltering(CharSequence constraint) {
                    FilterResults filterResults = new Filter.FilterResults();
                    if (constraint != null) {
                        // Retrieve the autocomplete results.
                        resultList = autocomplete(constraint.toString());

                        // Assign the data to the FilterResults
                        filterResults.values = resultList;
                        filterResults.count = resultList.size();


                    }
                    return filterResults;
                }

                @Override
                protected void publishResults(CharSequence constraint, Filter.FilterResults results) {
                    if (results != null && results.count > 0) {
                        notifyDataSetChanged();
                    } else {
                        notifyDataSetInvalidated();
                    }
                }
            };
            return filter;
        }
    }

    public static ArrayList previewDetails(String input) {
        ArrayList resultList = null;

        HttpURLConnection conn = null;
        StringBuilder jsonResults = new StringBuilder();
        try {
            StringBuilder sb = new StringBuilder(PLACES_API_BASE + TYPE_AUTOCOMPLETE + OUT_JSON);
            sb.append("?key=" + API_KEY);
            sb.append("&components=country:lk");
            sb.append("&input=" + URLEncoder.encode(input, "utf8"));

            URL url = new URL(sb.toString());
            conn = (HttpURLConnection) url.openConnection();
            InputStreamReader in = new InputStreamReader(conn.getInputStream());

            // Load the results into a StringBuilder
            int read;
            char[] buff = new char[1024];
            while ((read = in.read(buff)) != -1) {
                jsonResults.append(buff, 0, read);
            }
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Error processing Places API URL", e);
            return resultList;
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error connecting to Places API", e);
            return resultList;
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }

        try {
            // Create a JSON object hierarchy from the results
            JSONObject jsonObj = new JSONObject(jsonResults.toString());
            JSONArray predsJsonArray = jsonObj.getJSONArray("predictions");

            // Extract the Place descriptions from the results
            resultList = new ArrayList(predsJsonArray.length());
            for (int i = 0; i < predsJsonArray.length(); i++) {
                System.out.println(predsJsonArray.getJSONObject(i).getString("description"));
                System.out.println("============================================================");
                resultList.add(predsJsonArray.getJSONObject(i).getString("description"));
            }
        } catch (JSONException e) {
            Log.e(LOG_TAG, "Cannot process JSON results", e);
        }

        return resultList;
    }

    class GooglePlacesAutocompleteAdapterDestination extends ArrayAdapter implements Filterable {
        private ArrayList resultList;

        public GooglePlacesAutocompleteAdapterDestination(Context context, int textViewResourceId) {
            super(context, textViewResourceId);
        }

        @Override
        public int getCount() {
            return resultList.size();
        }

        @Override
        public String getItem(int index) {
            return (String) resultList.get(index);
        }


        @Override
        public Filter getFilter() {


            Filter filter = new Filter() {
                @Override
                protected FilterResults performFiltering(CharSequence constraint) {
                    FilterResults filterResults = new Filter.FilterResults();
                    if (constraint != null) {
                        // Retrieve the autocomplete results.
                        resultList = previewDetails(constraint.toString());
                        // Assign the data to the FilterResults
                        filterResults.values = resultList;
                        filterResults.count = resultList.size();
                        //getLatLongFromAddressDes(constraint.toString());
                    }
                    return filterResults;

                }

                @Override
                protected void publishResults(CharSequence constraint, Filter.FilterResults results) {
                    if (results != null && results.count > 0) {
                        notifyDataSetChanged();

                    } else {
                        notifyDataSetInvalidated();
                    }
                }
            };
            return filter;
        }
    }



    @Override
    public void onItemClick(AdapterView adapterView, View view, int position, long id) {
        String str = (String) adapterView.getItemAtPosition(position);
        Toast.makeText(this, str, Toast.LENGTH_SHORT).show();

    }

    public void NavigationSource(){
        //ImageButton a = (ImageButton) findViewById(R.id.btn_help);
        ImageButton ab = (ImageButton)findViewById(R.id.imageButton);
        //Button a=(Button)findViewById(R.id.btn_help);
        ab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),MapsActivityFinal.class));
            }
        });
    }

    public void NavigationDes(){
        //ImageButton a = (ImageButton) findViewById(R.id.btn_help);
        ImageButton ab = (ImageButton)findViewById(R.id.imageButton2);
        //Button a=(Button)findViewById(R.id.btn_help);
        ab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),MapsActivity.class));
            }
        });
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////////////////////////////

    class NotifyDriver extends AsyncTask<String, String, String> {


        boolean failure = false;



        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(Reservation.this);
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
                params.add(new BasicNameValuePair("Request_ID", dbReqId));

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


    ////////////////////////////////////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////////////////////////////////////////////////////////



    ////////////////////////////////////////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////////////////////////////////////////////////////////


    class SelectNearestDriver extends AsyncTask<String, String, String> {


        boolean failure = false;



        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(Reservation.this);
            pDialog.setMessage("Selecting...");
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

                SharedPreferences s=getSharedPreferences("MyPref",Context.MODE_PRIVATE);

                Log.d("request!", "starting");

                JSONObject json = jsonParser.makeHttpRequest(NEAREST_DRIVER_URL, "POST",null);

                Log.d("Request attempt", json.toString());

                success = json.getInt(TAG_SUCCESS);

                if (success == 1) {

                    dbNic=json.getString("nic");
                    dbReqId = json.getString("reqId");
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

            pDialog.dismiss();
            if (file_url != null){

                // Toast.makeText(Reservation.this, file_url, Toast.LENGTH_LONG).show();
            }

        }
    }


    ////////////////////////////////////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////////////////////////////////////////////////////////


    class PassengerRequest extends AsyncTask<String, String, String> {




        boolean failure = false;



        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(Reservation.this);
            pDialog.setMessage("Requesting...");
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
                String autoSourceAdd=autoCompView.getText().toString();
                String autoDestAdd=autoCompViewDES.getText().toString();

                SharedPreferences prefAuto = getApplicationContext().getSharedPreferences("autoMe", 0);
                SharedPreferences.Editor editorAuto = prefAuto.edit();

                editorAuto.putString("sourceAdd",autoSourceAdd);

                editorAuto.commit();

                getLatLongFromAddress(autoSourceAdd);
                getLatLongFromAddressDes(autoDestAdd);

                SharedPreferences pref = getApplicationContext().getSharedPreferences("Location", 0);
                SharedPreferences.Editor editor = pref.edit();

                editor.putString("sourceAdd",autoSourceAdd);
                editor.putString("destAdd",autoDestAdd);

                editor.commit();


                SharedPreferences s=getSharedPreferences("MyPref", Context.MODE_PRIVATE);
                SharedPreferences s1=getSharedPreferences("Location", Context.MODE_PRIVATE);



                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("Email", s.getString("email", "")));
                Log.e("emai",s.getString("email", ""));
                params.add(new BasicNameValuePair("vehicle_type",spinnerV.getSelectedItem().toString()));
                params.add(new BasicNameValuePair("Pickup_Location", s1.getString("source", "") ));
                params.add(new BasicNameValuePair("Destination", s1.getString("dest", "")));
                params.add(new BasicNameValuePair("Pickup_Address", s1.getString("sourceAdd", "") ));
                params.add(new BasicNameValuePair("Dest_Address",  s1.getString("destAdd", "")));
                Log.d("request!", "starting"+s.getString("email", ""));

                JSONObject json = jsonParser.makeHttpRequest( Request_URL, "POST", params);

//                Log.d("Request attempt", json.toString());

                success = json.getInt(TAG_SUCCESS);

                if (success == 1) {


                    Log.d("Requested!", json.toString());

                    return json.getString(TAG_MESSAGE);


                }else{

                    Log.d("Request Failure!", json.getString(TAG_MESSAGE));

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
               // Toast.makeText(Reservation.this, file_url, Toast.LENGTH_LONG).show();
            }

        }
    }

    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    class UpdateRequestStatusF extends AsyncTask<String, String, String> {


        boolean failure = false;


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(Reservation.this);
            pDialog.setMessage("Fullfilling...");
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

                SharedPreferences s = getSharedPreferences("MyPref", Context.MODE_PRIVATE);


                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("Request_ID", "1"));

                params.add(new BasicNameValuePair("status", "requring"));
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
        ////////////////////////////////////////////////////////////////////////////////////////////////
        ///////////////////////////////////////////////////////////////////////////////////////////////

    class UpdateRequestStatusR extends AsyncTask<String, String, String> {


        boolean failure = false;


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(Reservation.this);
            pDialog.setMessage("Requesting...");
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

                SharedPreferences s = getSharedPreferences("MyPref", Context.MODE_PRIVATE);


                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("Request_ID", "1"));

                params.add(new BasicNameValuePair("status", "requring"));
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
        //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    }
    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    class UpdateRequestStatusC extends AsyncTask<String, String, String> {


        boolean failure = false;


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(Reservation.this);
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
                params.add(new BasicNameValuePair("Request_ID", "2"));

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
    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
}

